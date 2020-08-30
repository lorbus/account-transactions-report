package com.lorbush.trx.service;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.entities.User;
import com.lorbush.trx.exceptions.ErrorMessages;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.repository.CurrencyRepository;
import com.lorbush.trx.repository.TransactionRepository;
import com.lorbush.trx.repository.AccountRepository;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.helper.HelperImpl;
import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

/**
 * AccountService tests
 *
 */
@RunWith(SpringRunner.class)
public class AccountServiceTest {
	@TestConfiguration
	static class AccountServiceImplTestContextConfiguration {
		@Bean
		public AccountService accountService() {
			return new AccountServiceImpl();
		}

		@Bean
		public Helper validator() {
			return new HelperImpl();
		}

		@Bean
		public MethodValidationPostProcessor methodValidationPostProcessor() {
			return new MethodValidationPostProcessor();
		}
	}

	public static final String UPDATED_BY = "testAccountService";
	public static final Long USER_ID = 1L;
	public static final String CURRENCY_ID = "GBP";
	public static final String GBP_CURRENCY = "GBP";
	public static final String ACCOUNT_IBAN_1 = "CH93-0000-0000-0000-0000-1";
	public static final String ACCOUNT_IBAN_2 = "CH93-0000-0000-0000-0000-2";
	public static final BigDecimal BALANCE_0 = new BigDecimal(0);
	public static final BigDecimal BALANCE_20 = new BigDecimal(20);
	public static final String USERNAME = "username";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";


	@Value("${db.updated_by}")
	String updatedBy;

	@Autowired
	private AccountService accountService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private CurrencyRepository currencyRepository;

	private User user;
	private Currency currency;
	private Account account1;
	private Account account2;

	@Before
	public void setUp() {
		user = new User(USERNAME, FIRST_NAME, LAST_NAME);
		currency = new Currency(CURRENCY_ID, GBP_CURRENCY, UPDATED_BY);
		account1 = new Account(ACCOUNT_IBAN_1, user, currency, BALANCE_0, UPDATED_BY);
		account1.setId(1);
		account2 = new Account(ACCOUNT_IBAN_2, user, currency, BALANCE_20, UPDATED_BY);
		account2.setId(2);

		Mockito.when(accountRepository.findAllByOrderByIdAsc()).thenReturn(Arrays.asList(account1, account2));

		Mockito.when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
		Mockito.when(accountRepository.findById(1111)).thenReturn(Optional.empty());

		Mockito.when(accountRepository.findByUserId(USER_ID)).thenReturn(Arrays.asList(account1, account2));
		Mockito.when(accountRepository.findByUserId(1L)).thenReturn(new ArrayList<Account>());

		Currency wrong = new Currency("GBP", "Wrong", UPDATED_BY);
		Mockito.when(currencyRepository.findByName("Wrong")).thenReturn(wrong);
		Mockito.when(accountRepository.save(new Account(ACCOUNT_IBAN_1, user, wrong, BALANCE_0, UPDATED_BY)))
				.thenThrow(new ObjectNotFoundException("", ""));
		Mockito.when(currencyRepository.findByName(GBP_CURRENCY)).thenReturn(currency);
		Mockito.when(accountRepository.save(account1)).thenReturn(account1);
		Mockito.when(accountRepository.save(account2)).thenReturn(account2);
	}

	@Test
	public void testFindAll() throws CustomException {
		List<Account> found = accountService.findAll();
		assertNotNull(found);
		assertTrue(found.size() == 2);
		assertTrue(found.get(0).getId().equals(account1.getId()));
		assertTrue(found.get(1).getId().equals(account2.getId()));
	}

	@Test
	public void testFindById_Success() throws CustomException {
		Account found = accountService.findById(account1.getId());
		assertNotNull(found);
		assertTrue(found.getId().equals(account1.getId()));
	}

	@Test(expected = ConstraintViolationException.class)
	public void testFindById_Null() throws CustomException {
		Account found = accountService.findById(null);
	}

	@Test
	public void testFindById_DoesntExist() throws CustomException {
		try {
			Account found = accountService.findById(12345);
			assertNull(found);
			fail();
		} catch (CustomException e) {
			assertEquals(e.getMessage(), String.format(ErrorMessages.NO_ACCOUNT_FOUND, "12345"));
			assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testFindByUserId_Success() throws CustomException {
		List<Account> found = accountService.findByUserId(account1.getUser());
		assertNotNull(found);
		assertTrue(found.size() == 2);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testFindByUserId_Null() throws CustomException {
		accountService.findByUserId(null);
	}

	@Test
	public void testFindByUserId_DoesntExist() throws CustomException {
		User user = new User();
		user.setId(-1L);
		List<Account> found = accountService.findByUserId(user);
		assertNotNull(found);
		assertTrue(found.size() == 0);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testCreateAccount_Null() throws CustomException {
		Account found = accountService.createAccount(ACCOUNT_IBAN_1, user, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testCreateAccount_Blank() throws CustomException {
		Account found = accountService.createAccount(ACCOUNT_IBAN_2, user, "");
	}

	@Test
	public void testCreateAccount_CurrencyNotFound() throws CustomException {
		try {
			Account found = accountService.createAccount(ACCOUNT_IBAN_2, user, "Wrong");
		} catch (CustomException e) {
			assertEquals(e.getMessage(), String.format(ErrorMessages.NO_CURRENCY_PRESENT, "Wrong"));
			assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testCreateAccount_Success() throws CustomException {
		Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account1);
		Account found = accountService.createAccount(ACCOUNT_IBAN_1, user, GBP_CURRENCY);
		assertEquals(found.getId(), account1.getId());
	}

	@Test
	public void testUpdateAccountAmount_isCredit() throws CustomException {
		int amount = 30;
		Account found = accountService.updateAccountAmount(account1, String.valueOf(amount), true);
		assertEquals(found.getId(), account1.getId());
		assertEquals(found.getBalance(), new BigDecimal(amount));
	}

	@Test
	public void testUpdateAccountAmount_isDebitSuccess() throws CustomException {
		int amount = 10;
		Account found = accountService.updateAccountAmount(account2, String.valueOf(amount), false);
		assertEquals(found.getId(), account2.getId());
		assertEquals(found.getBalance(), new BigDecimal(10));
	}

	@Test
	public void testUpdateAccountAmount_isDebitSuccess2() throws CustomException {
		int amount = -10;
		Account found = accountService.updateAccountAmount(account2, String.valueOf(amount), false);
		assertEquals(found.getId(), account2.getId());
		assertEquals(found.getBalance(), new BigDecimal(10));
	}

	@Test
	public void testUpdateAccountAmount_isDebitFailure() throws CustomException {
		int amount = 100;
		try {
			Account found = accountService.updateAccountAmount(account2, String.valueOf(amount), false);
			fail();
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(),
					String.format(ErrorMessages.NO_ENOUGH_FUNDS, account2.getId(), String.valueOf(amount)));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testUpdateAccountAmount_AmountNotANumber() throws CustomException {
		String badAmount = "badAmount";
		try {
			Account found = accountService.updateAccountAmount(account2, badAmount, false);
			fail();
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(), String.format(ErrorMessages.NUMBER_FORMAT_MISMATCH, badAmount));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

}