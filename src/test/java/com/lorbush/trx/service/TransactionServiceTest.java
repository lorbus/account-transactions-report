package com.lorbush.trx.service;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.entities.TransactionType;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.ErrorMessages;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.repository.CurrencyRepository;
import com.lorbush.trx.repository.TransactionRepository;
import com.lorbush.trx.repository.TransactionTypeRepository;
import com.lorbush.trx.repository.AccountRepository;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * TransactionService tests
 *
 */
@RunWith(SpringRunner.class)
public class TransactionServiceTest {
	@TestConfiguration
	static class TransactionServiceImplTestContextConfiguration {
		@Bean
		public TransactionService transactionService() {
			return new TransactionServiceImpl();
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

	public static final String UPDATED_BY = "user";
	public static final String USER = "user";
	public static final String GBP_CURRENCY = "GBP";
	public static final String CURRENCY_ID = "GBP";
	public static final String ACCOUNT_ID_1 = "CH93-0000-0000-0000-0000-1";
	public static final String ACCOUNT_ID_2 = "CH93-0000-0000-0000-0000-2";

	@Value("${application.transactionCredit.type.credit}")
	String credit;

	@Value("${application.transactionCredit.type.debit}")
	String debit;

	@Autowired
	private TransactionService transactionService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private CurrencyRepository currencyRepository;

	@MockBean
	private TransactionTypeRepository transactionTypeRepository;

	@MockBean
	private AccountService accountService;

	private Currency currency;
	private Account account1;
	private Account account2;
	private TransactionType typeCredit;
	private TransactionType typeDebit;
	private Transaction transactionCredit;
	private Transaction transactionDebit;

	@Before
	public void setUp() throws CustomException {
		currency = new Currency(CURRENCY_ID, GBP_CURRENCY, UPDATED_BY);
		account1 = new Account(ACCOUNT_ID_1, USER, currency, new BigDecimal(0), UPDATED_BY);
		account1.setId(1);
		account2 = new Account(ACCOUNT_ID_2, USER, currency, new BigDecimal(40), UPDATED_BY);
		account2.setId(2);
		typeCredit = new TransactionType(credit, "credit trx", UPDATED_BY);
		typeDebit = new TransactionType(debit, "debit trx", UPDATED_BY);
		transactionCredit = new Transaction(typeCredit, new BigDecimal(20), account1, currency, "Credit transaction");
		transactionCredit.setId(5);
		transactionDebit = new Transaction(typeDebit, new BigDecimal(20), account2, currency, "Debit transaction");
		transactionDebit.setId(6);

		Mockito.when(accountService.findById(account1.getId())).thenReturn(account1);
		Mockito.when(transactionRepository.findByAccount(account1)).thenReturn(Arrays.asList(transactionCredit));

		Mockito.when(transactionRepository.findByAccount(account2)).thenReturn(Arrays.asList(transactionCredit));

		Currency wrong = new Currency("GBP;", "Wrong", UPDATED_BY);
		Mockito.when(currencyRepository.findByName("Wrong")).thenReturn(wrong);
		Mockito.when(accountRepository.save(new Account(ACCOUNT_ID_1, USER, wrong, new BigDecimal(0), UPDATED_BY)))
				.thenThrow(new ObjectNotFoundException("", ""));
		Mockito.when(currencyRepository.findByName(GBP_CURRENCY)).thenReturn(currency);
		Mockito.when(transactionTypeRepository.getOne(typeCredit.getId())).thenReturn(typeCredit);
		Mockito.when(transactionTypeRepository.getOne(typeDebit.getId())).thenReturn(typeDebit);
		Mockito.when(accountService.findById(account1.getId())).thenReturn(account1);
		Mockito.when(accountService.findById(account2.getId())).thenReturn(account2);
		Mockito.when(accountService.findById(222)).thenReturn(null);
	}

	@Test
	public void testGetTransactionsByAccountId_Success() throws CustomException {
		List<Transaction> found = transactionService.getTransactionsByAccountId(account1.getId());
		assertNotNull(found);
		assertTrue(found.size() == 1);
		assertTrue(found.get(0).getId().equals(transactionCredit.getId()));
	}

	@Test
	public void testGetTransactionsByAccountId_Failed() throws CustomException {
		String error = String.format(ErrorMessages.NO_ACCOUNT_FOUND, account2.getId().toString());
		Mockito.when(accountService.findById(account2.getId()))
				.thenThrow(new CustomException(error, HttpStatus.BAD_REQUEST.value()));
		try {
			List<Transaction> found = transactionService.getTransactionsByAccountId(account2.getId());
			fail();
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(), String.format(ErrorMessages.NO_ACCOUNT_FOUND, account2.getId().toString()));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testCreateTransaction_SuccessCredit() throws CustomException {
		int amount = 100;
		Mockito.when(accountService.updateAccountAmount(account1, String.valueOf(amount), true)).thenReturn(account1);
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionCredit);
		Transaction found = transactionService.createTransaction(currency.getName(), account1.getId().toString(),
				typeCredit.getId(), String.valueOf(amount), "Success trx");
		assertNotNull(found);
		assertTrue(found.getId().equals(transactionCredit.getId()));
	}

	@Test
	public void testCreateTransaction_SuccessDebit() throws CustomException {
		int amount = -10;
		Mockito.when(accountService.updateAccountAmount(account2, String.valueOf(amount), false)).thenReturn(account2);
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionDebit);
		Transaction found = transactionService.createTransaction(currency.getName(), account2.getId().toString(),
				typeDebit.getId(), String.valueOf(amount), "Success trx");
		assertNotNull(found);
		assertTrue(found.getId().equals(transactionDebit.getId()));
	}

	@Test
	public void testCreateTransaction_DebitFailure() throws CustomException {
		int amount = -1000;
		String error = String.format(ErrorMessages.NO_ENOUGH_FUNDS, account2.getId(), String.valueOf(amount));
		Mockito.when(accountService.updateAccountAmount(account2, String.valueOf(amount), false))
				.thenThrow(new CustomException(error, HttpStatus.BAD_REQUEST.value()));
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionDebit);
		try {
			Transaction found = transactionService.createTransaction(currency.getName(), account2.getId().toString(),
					typeDebit.getId(), String.valueOf(amount), "Success trx");
			fail();
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(),
					String.format(ErrorMessages.NO_ENOUGH_FUNDS, account2.getId(), String.valueOf(amount)));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testCreateTransaction_AccountNotFound() throws CustomException {
		int amount = 100;
		String notFoundAccountId = "1001";
		Mockito.when(accountService.updateAccountAmount(account1, String.valueOf(amount), true)).thenReturn(account1);
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionCredit);
		try {
			Transaction found = transactionService.createTransaction(currency.getName(), notFoundAccountId,
					typeCredit.getId(), String.valueOf(amount), "No account");
			fail();
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(), String.format(ErrorMessages.NO_ACCOUNT_FOUND, notFoundAccountId));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

	@Test
	public void testCreateTransaction_AmountNotNumber() throws CustomException {
		String wrongAmount = "AAAee";
		Mockito.when(accountService.updateAccountAmount(account1, String.valueOf(wrongAmount), true))
				.thenReturn(account1);
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transactionCredit);
		try {
			Transaction found = transactionService.createTransaction(currency.getName(), account1.getId().toString(),
					typeCredit.getId(), wrongAmount, "Fail trx");
		} catch (CustomException ex) {
			assertEquals(ex.getMessage(), String.format(ErrorMessages.NUMBER_FORMAT_MISMATCH, wrongAmount));
			assertEquals(ex.getErrorCode(), HttpStatus.BAD_REQUEST.value());
		}
	}

}
