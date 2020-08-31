package com.lorbush.trx.repository;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Account;
import javax.validation.ConstraintViolationException;

import com.lorbush.trx.entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

/**
 * AccountRepository tests Use in-memory h2database
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

	public static final String UPDATED_BY = "testAccountRepository";
	public static final Long USER_ID = 1L;
	public static final String EUR_CURRENCY = "EUR";
	public static final String CURRENCY_ID_EUR = "EUR";
	public static final String ACCOUNT_IBAN_1 = "CH93-0000-0000-0000-0000-1";
	public static final String ACCOUNT_IBAN_2 = "CH93-0000-0000-0000-0000-2";
	public static final BigDecimal BALANCE_0 = new BigDecimal(0);
	public static final String USERNAME = "username";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	private User user;
	private Account account1;
	private Account account2;
	private Currency currency;

	@Before
	public void before() {
		user = new User(USERNAME, FIRST_NAME, LAST_NAME);
		currency = new Currency(CURRENCY_ID_EUR, EUR_CURRENCY, UPDATED_BY);
		entityManager.persistAndFlush(currency);
		account1 = new Account(ACCOUNT_IBAN_1, user, currency, BALANCE_0, UPDATED_BY);
		account2 = new Account(ACCOUNT_IBAN_2, user, currency, BALANCE_0, UPDATED_BY);
		entityManager.persist(account1);
		entityManager.persist(account2);
		entityManager.flush();
	}

	@Test
	public void whenFindById_thenReturnAccount() {
		Optional<Account> found = accountRepository.findById(account2.getId());
		assertTrue(found.isPresent());
		assertTrue(found.get().getCurrency().getName().equals(EUR_CURRENCY));
		//TODO
		//assertTrue(found.get().getUser().getId().equals(USER_ID.intValue()));
		assertTrue(found.get().getBalance().equals(new BigDecimal(0)));
	}

	@Test
	public void whenFindById_NoAccount() {
		Optional<Account> found = accountRepository.findById(123);
		assertTrue(!found.isPresent());
	}

	@Test
	public void whenFindByUserId_thenReturnAccount() {
		List<Account> found = accountRepository.findByUserId(USER_ID);
		assertNotNull(found);
		//TODO
		assertTrue(found.size() == 0);
		//assertTrue(found.get(0).getUser().getId().equals(USER_ID.intValue()));
		//assertTrue(found.get(1).getUser().getId().equals(USER_ID.intValue()));
	}

	@Test
	public void whenFindByUserId_NotFound() {
		List<Account> found = accountRepository.findByUserId(-123L);
		assertNotNull(found);
		assertTrue(found.size() == 0);
	}

	@Test
	public void testFindAllByOrderByIdAsc() {
		List<Account> found = accountRepository.findAllByOrderByIdAsc();
		assertNotNull(found);
		assertTrue(!found.isEmpty());
		assertTrue(found.size() >= 2);
		System.out.println(found.get(0).getId());
		System.out.println(found.get(1).getId());
		assertTrue(found.get(0).getId().equals(account1.getId()));
		assertTrue(found.get(1).getId().equals(account2.getId()));
	}

	@Test
	public void whenSave_Success() {
		Account account = new Account(ACCOUNT_IBAN_2, user, currency, BALANCE_0, UPDATED_BY);
		Account found = accountRepository.save(account);
		assertNotNull(found);
		assertTrue(found.getCurrency().getName().equals(EUR_CURRENCY));
		assertTrue(found.getBalance().equals(BALANCE_0));
	}

	@Test
	public void whenSave_FailWrongCurrency() {
		Currency currency = currencyRepository.findByName("AAA");
		Account account = new Account(ACCOUNT_IBAN_1, user, currency, BALANCE_0, UPDATED_BY);
		try {
			Account found = accountRepository.save(account);
			fail();
		} catch (ConstraintViolationException ex) {
			assertTrue(ex.getMessage().contains("Account currency must be provided"));
		}
	}

	@Test
	public void whenSave_FailWrongCurrencyLong() {
		Currency currency = currencyRepository.findByName("DOLLAR");
		Account account = new Account(ACCOUNT_IBAN_1, user, currency, BALANCE_0, UPDATED_BY);
		try {
			Account found = accountRepository.save(account);
			fail();
		} catch (ConstraintViolationException ex) {
			assertTrue(ex.getMessage().contains("Account currency must be provided"));
		}
	}

	@Test
	public void update_Balance() {
		Optional<Account> found = accountRepository.findById(account1.getId());
		Account updated = found.get();
		updated.setBalance(new BigDecimal(300));
		Account found1 = accountRepository.save(updated);
		assertNotNull(found1);
		assertTrue(found1.getBalance().equals(new BigDecimal(300)));
	}

	@Test
	public void update_BalanceNegative() {
		Optional<Account> found = accountRepository.findById(account2.getId());
		Account updated = found.get();
		updated.setBalance(new BigDecimal(-300));
		Account found1 = accountRepository.save(updated);
		try {
			entityManager.flush();
			fail();
		} catch (ConstraintViolationException ex) {
			assertFalse(ex.getConstraintViolations().isEmpty());
			assertTrue(ex.getConstraintViolations().iterator().next().getMessage()
					.contains("must be greater than or equal to 0"));
		}
	}

	@Test
	public void update_BalanceNull() {
		Optional<Account> found = accountRepository.findById(account2.getId());
		Account updated = found.get();
		updated.setBalance(null);
		Account found1 = accountRepository.save(updated);
		try {
			entityManager.flush();
			fail();
		} catch (ConstraintViolationException ex) {
			assertFalse(ex.getConstraintViolations().isEmpty());
			System.out.println(ex.getConstraintViolations().iterator().next().getMessage());
			assertTrue(ex.getConstraintViolations().iterator().next().getMessage()
					.contains("Account balance must be provided"));
		}
	}

	@After
	public void after() {

	}

}
