package com.lorbush.trx.repository;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.entities.TransactionType;
import com.lorbush.trx.entities.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TransactionRepository tests Use in-memory h2database
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application.properties")
public class TransactionRepositoryTest {

	public static final String UPDATED_BY = "user";
	public static final String USER = "user";
	public static final String CURRENCY_EUR = "EUR";
	public static final String ACCOUNT_IBAN_1 = "CH93-0000-0000-0000-0000-1";
	public static final String ACCOUNT_IBAN_2 = "CH93-0000-0000-0000-0000-2";

	@Value("${application.transaction.type.credit}")
	String credit;

	@Value("${application.transaction.type.debit}")
	String debit;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	private Account account1;
	private Account account2;
	private Currency currency;
	private TransactionType typeCredit;
	private TransactionType typeDebit;
	private Transaction transaction;
	public static final String EUR_CURRENCY = "EUR";

	@Before
	public void before() {
		currency = new Currency(CURRENCY_EUR, EUR_CURRENCY, UPDATED_BY);
		entityManager.persistAndFlush(currency);

		account1 = new Account(ACCOUNT_IBAN_1, USER, new Currency(CURRENCY_EUR, EUR_CURRENCY, UPDATED_BY),
				new BigDecimal(0), UPDATED_BY);
		account2 = new Account(ACCOUNT_IBAN_2, USER, new Currency(CURRENCY_EUR, EUR_CURRENCY, UPDATED_BY),
				new BigDecimal(0), UPDATED_BY);

		entityManager.persist(account1);
		entityManager.persist(account2);
		entityManager.flush();

		typeCredit = new TransactionType(credit, "credit trx", UPDATED_BY);
		typeDebit = new TransactionType(debit, "debit trx", UPDATED_BY);
		entityManager.persist(typeCredit);
		entityManager.persist(typeDebit);
		entityManager.flush();

		transaction = new Transaction(typeCredit, new BigDecimal(20), account1, currency, "Credit transaction");
		entityManager.persist(transaction);
		entityManager.flush();
	}

	@Test
	public void testFindByAccount() {
		List<Transaction> trx = transactionRepository.findByAccount(account1);
		assertTrue(trx.size() > 0);
		assertTrue(trx.get(0).getAccount().getId().equals(account1.getId()));
		assertTrue(trx.get(0).getId().equals(transaction.getId()));
	}

	@Test
	public void testSave_Credit() {
		Transaction transaction = new Transaction(typeCredit, new BigDecimal(20), account2, currency,
				"Credit transaction");
		Transaction found = transactionRepository.save(transaction);
		assertNotNull(found);
		assertTrue(found.getCurrency().getName().equals(CURRENCY_EUR));
		assertTrue(found.getAmount().equals(new BigDecimal(20)));
		assertTrue(found.getType().getId().equals(credit));
		assertTrue(found.getAccount().getId().equals(account2.getId()));
	}

	@Test
	public void testSave_Debit() {
		Transaction transactionDebit = new Transaction(typeCredit, new BigDecimal(-10), account1, currency,
				"Credit transaction");
		Transaction found = transactionRepository.save(transactionDebit);
		assertNotNull(found);
		assertTrue(found.getCurrency().getName().equals(CURRENCY_EUR));
		assertTrue(found.getAmount().equals(new BigDecimal(-10)));
		assertTrue(found.getType().getId().equals(credit));
		assertTrue(found.getAccount().getId().equals(account1.getId()));
	}

	@Test
	public void whenSave_FailWrongCurrency() {
		Currency currency = currencyRepository.findByName("AAA");
		Transaction transaction = new Transaction(typeCredit, new BigDecimal(20), account2, currency,
				"Credit transaction");
		try {
			Transaction found = transactionRepository.save(transaction);
			fail();
		} catch (ConstraintViolationException ex) {
			assertTrue(ex.getMessage().contains("Transaction currency must be provided"));
		}
	}

	@Test
	public void whenSave_NoBalance() {
		Transaction transaction = new Transaction(typeCredit, null, account2, currency, "Credit transaction");
		try {
			Transaction found = transactionRepository.save(transaction);
			entityManager.flush();
			fail();
		} catch (ConstraintViolationException ex) {
			assertFalse(ex.getConstraintViolations().isEmpty());
			assertTrue(ex.getConstraintViolations().iterator().next().getMessage()
					.contains("Transaction amount must be provided"));
		}
	}

	@Test
	public void whenSave_FailWrongAccount() {
		Account account = accountRepository.getOne(111);
		Transaction transaction = new Transaction(typeCredit, new BigDecimal(20), account, currency,
				"Credit transaction");
		try {
			Transaction found = transactionRepository.save(transaction);
			entityManager.flush();
			fail();
		} catch (DataIntegrityViolationException ex) {
			assertTrue(ex.getMessage().contains("could not execute statement"));
		}
	}

	@Test
	public void whenSave_FailWrongType() {
		TransactionType type = transactionTypeRepository.getOne("wrong");
		Transaction transaction = new Transaction(type, new BigDecimal(20), account2, currency, "Credit transaction");
		try {
			Transaction found = transactionRepository.save(transaction);
			entityManager.flush();
			fail();
		} catch (DataIntegrityViolationException ex) {
			assertTrue(ex.getMessage().contains("could not execute statement"));
		}
	}
}
