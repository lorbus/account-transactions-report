package com.lorbush.trx.controller;

import com.lorbush.trx.entities.*;
import com.lorbush.trx.service.TransactionService;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.helper.HelperImpl;
import com.google.gson.GsonBuilder;
import com.lorbush.trx.exceptions.ErrorMessages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TransactionController tests
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
	@TestConfiguration
	static class AccountControllerTestContextConfiguration {
		@Bean
		public Helper validator() {
			return new HelperImpl();
		}
	}

	public static final String UPDATED_BY = "testTransactionController";
	public static final String CREDIT = "C";
	public static final String CURRENCY_ID = "CHF";
	public static final String CHF_CURRENCY = "CHF";
	public static final String ACCOUNT_IBAN_1 = "CH93-0000-0000-0000-0000-1";
	public static final BigDecimal BALANCE_0 = new BigDecimal(0);
	public static final BigDecimal BALANCE_20 = new BigDecimal(20);
	public static final String USERNAME = "username";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransactionService service;

	private User user;
	private Currency currency;
	private Account account;
	private Transaction transactionCredit;
	private TransactionType typeCredit;

	@Before
	public void before() {
		user = new User(USERNAME, FIRST_NAME, LAST_NAME);
		currency = new Currency(CURRENCY_ID, CHF_CURRENCY, UPDATED_BY);
		account = new Account(ACCOUNT_IBAN_1, user, currency, BALANCE_0, UPDATED_BY);
		account.setId(1);
		String trxTypeCreditDesc = "credit trx";
		typeCredit = new TransactionType(CREDIT, trxTypeCreditDesc, UPDATED_BY);
		String trxDesc = "Credit transaction";
		transactionCredit = new Transaction(typeCredit, BALANCE_20, account, currency, trxDesc);
		transactionCredit.setId(5);
	}

	@Test
	public void testGetAccountTransactionsById_whenGetTransaction_thenReturnJsonArray() throws Exception {
		List<Transaction> allTransactions = Arrays.asList(transactionCredit);
		given(service.getTransactionsByAccountId(account.getId())).willReturn(allTransactions);

		mvc.perform(get("/api/v1/accounts/" + account.getId() + "/transactions").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.transactions", hasSize(1)))
				.andExpect(jsonPath("$.poundTotalAmount", is(0)))
				.andExpect(jsonPath("$.transactions[0].id", is(transactionCredit.getId())))
				.andExpect(jsonPath("$.transactions[0].type.id", is(CREDIT)))
				.andExpect(jsonPath("$.transactions[0].type.description", is(transactionCredit.getType().getDescription())))
				.andExpect(jsonPath("$.transactions[0].amount", is(transactionCredit.getAmount().intValue())))
				.andExpect(jsonPath("$.transactions[0].currency.name", is(CHF_CURRENCY)))
				.andExpect(jsonPath("$.transactions[0].description", is(transactionCredit.getDescription())));
	}

	@Test
	public void testCreateTransaction_thenReturnJson() throws Exception {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("currency", transactionCredit.getCurrency().getId());
		dataMap.put("accountId", transactionCredit.getAccount().getId().toString());
		dataMap.put("transactionTypeId", transactionCredit.getType().getId());
		dataMap.put("amount", transactionCredit.getAmount().toString());
		dataMap.put("description", transactionCredit.getDescription());

		given(service.createTransaction(dataMap.get("currency"), dataMap.get("accountId"),
				dataMap.get("transactionTypeId"), dataMap.get("amount"), dataMap.get("description")))
						.willReturn(transactionCredit);
		String validJson = new GsonBuilder().create().toJson(dataMap);

		mvc.perform(post("/api/v1/transaction").content(validJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(transactionCredit.getId())))
				.andExpect(jsonPath("$.type.id", is(CREDIT)))
				.andExpect(jsonPath("$.type.description", is(transactionCredit.getType().getDescription())))
				.andExpect(jsonPath("$.amount", is(transactionCredit.getAmount().intValue())))
				.andExpect(jsonPath("$.currency.name", is(CHF_CURRENCY)))
				.andExpect(jsonPath("$.description", is(transactionCredit.getDescription())));
	}

	@Test
	public void testCreateTransaction_NoCurrency() throws Exception {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("accountId", transactionCredit.getAccount().getId().toString());
		dataMap.put("transactionTypeId", transactionCredit.getType().getId());
		dataMap.put("amount", transactionCredit.getAmount().toString());
		dataMap.put("description", transactionCredit.getDescription());

		given(service.createTransaction(dataMap.get("currency"), dataMap.get("accountId"),
				dataMap.get("transactionTypeId"), dataMap.get("amount"), dataMap.get("description")))
						.willReturn(transactionCredit);
		String json = new GsonBuilder().create().toJson(dataMap);
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "currency");

		mvc.perform(post("/api/v1/transaction").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/v1/transaction")));
	}

	@Test
	public void testCreateTransaction_NoAccountId() throws Exception {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("currency", transactionCredit.getCurrency().getId());
		dataMap.put("transactionTypeId", transactionCredit.getType().getId());
		dataMap.put("amount", transactionCredit.getAmount().toString());
		dataMap.put("description", transactionCredit.getDescription());

		given(service.createTransaction(dataMap.get("currency"), dataMap.get("accountId"),
				dataMap.get("transactionTypeId"), dataMap.get("amount"), dataMap.get("description")))
						.willReturn(transactionCredit);
		String json = new GsonBuilder().create().toJson(dataMap);
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "accountId");

		mvc.perform(post("/api/v1/transaction").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/v1/transaction")));
	}

	@Test
	public void testCreateTransaction_NoTransactionTypeIdId() throws Exception {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("currency", transactionCredit.getCurrency().getId());
		dataMap.put("accountId", transactionCredit.getAccount().getId().toString());
		dataMap.put("amount", transactionCredit.getAmount().toString());
		dataMap.put("description", transactionCredit.getDescription());

		given(service.createTransaction(dataMap.get("currency"), dataMap.get("accountId"),
				dataMap.get("transactionTypeId"), dataMap.get("amount"), dataMap.get("description")))
						.willReturn(transactionCredit);
		String json = new GsonBuilder().create().toJson(dataMap);
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "transactionTypeId");

		mvc.perform(post("/api/v1/transaction").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/v1/transaction")));
	}

	@Test
	public void testCreateTransaction_NoAmountId() throws Exception {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("currency", transactionCredit.getCurrency().getId());
		dataMap.put("accountId", transactionCredit.getAccount().getId().toString());
		dataMap.put("transactionTypeId", transactionCredit.getType().getId());
		dataMap.put("description", transactionCredit.getDescription());

		given(service.createTransaction(dataMap.get("currency"), dataMap.get("accountId"),
				dataMap.get("transactionTypeId"), dataMap.get("amount"), dataMap.get("description")))
						.willReturn(transactionCredit);
		String json = new GsonBuilder().create().toJson(dataMap);
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "amount");

		mvc.perform(post("/api/v1/transaction").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/v1/transaction")));
	}
}