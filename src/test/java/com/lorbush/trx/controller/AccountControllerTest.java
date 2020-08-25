package com.lorbush.trx.controller;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.service.AccountService;
import com.lorbush.trx.helper.HelperImpl;
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
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AccountController tests
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@TestConfiguration
	static class AccountControllerTestContextConfiguration {
		@Bean
		public Helper validator() {
			return new HelperImpl();
		}
	}

	public static final String ACCOUNT_IBAN_1 = "CH93-0000-0000-0000-0000-1";
	public static final String UPDATED_BY = "user1";
	public static final String USER = "user1";
	public static final String CURRENCY_ID = "EUR";
	public static final String EUR_CURRENCY = "EUR";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AccountService service;

	private Currency currency;
	private Account account;

	@Before
	public void before() {
		currency = new Currency(CURRENCY_ID, EUR_CURRENCY, UPDATED_BY);
		account = new Account(ACCOUNT_IBAN_1, USER, new Currency(CURRENCY_ID, EUR_CURRENCY, UPDATED_BY),
				new BigDecimal(0), UPDATED_BY);
		account.setId(1);
	}

	@Test
	public void testGetAll_whenGetAccount_thenReturnJsonArray() throws Exception {
		List<Account> allAccounts = Arrays.asList(account);

		given(service.findAll()).willReturn(allAccounts);

		mvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(account.getId())));
	}

	@Test
	public void testGetAccountById_thenReturnJson() throws Exception {

		given(service.findById(account.getId())).willReturn(account);

		mvc.perform(get("/api/account/" + account.getId().toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(account.getId())))
				.andExpect(jsonPath("$.userId", is(account.getUserId())))
				.andExpect(jsonPath("$.currency.id", is(account.getCurrency().getId())))
				.andExpect(jsonPath("$.balance", is(account.getBalance().intValue())))
				.andExpect(jsonPath("$.updatedBy", is(account.getUpdatedBy())));
	}

	@Test
	public void testGetAccountByUserId_thenReturnJson() throws Exception {

		given(service.findByUserId(account.getUserId())).willReturn(Arrays.asList(account));

		mvc.perform(get("/api/accounts/user").param("userId", account.getUserId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].userId", is(account.getUserId())));
	}

	@Test
	public void testCreateAccount_thenReturnJson() throws Exception {

		given(service.createAccount(ACCOUNT_IBAN_1, USER, EUR_CURRENCY)).willReturn(account);
		String validCurrencyJson = "{ \"iban\":\"" + ACCOUNT_IBAN_1 + "\", \"userId\":\"" + USER + "\",\"currency\":\""
				+ EUR_CURRENCY + "\"}";

		mvc.perform(post("/api/account").content(validCurrencyJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(account.getId())))
				.andExpect(jsonPath("$.iban", is(account.getIBan())))
				.andExpect(jsonPath("$.userId", is(account.getUserId())))
				.andExpect(jsonPath("$.currency.name", is(EUR_CURRENCY)))
				.andExpect(jsonPath("$.balance", is(account.getBalance().intValue())))
				.andExpect(jsonPath("$.updatedBy", is(account.getUpdatedBy())));
	}

	@Test
	public void testCreateAccount_NoCurrency() throws Exception {
		given(service.createAccount(ACCOUNT_IBAN_1, USER, currency.getName())).willReturn(account);
		String notValidCurrencyJson = "{ \"iban\":\"" + ACCOUNT_IBAN_1 + "\", \"userId\":\"" + USER + "\" }";
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "currency");

		mvc.perform(post("/api/account").content(notValidCurrencyJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/account")));
	}

	@Test
	public void testCreateAccount_NoUserId() throws Exception {

		given(service.createAccount(ACCOUNT_IBAN_1, USER, currency.getName())).willReturn(account);
		String notValidUserIdJson = "{ \"iban\":\"" + ACCOUNT_IBAN_1 + "\", \"currency\":\"" + EUR_CURRENCY + "\"}";
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "userId");

		mvc.perform(post("/api/account").content(notValidUserIdJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is(errorMessage)))
				.andExpect(jsonPath("$.details", is("uri=/api/account")));
	}

	@Test
	public void testCreateAccount_MalformedJson() throws Exception {

		given(service.createAccount(ACCOUNT_IBAN_1, USER, currency.getName())).willReturn(account);
		String malformedJson = "{ not correct Json ";
		String errorMessage = String.format(ErrorMessages.NO_MANDATORY_FIELD_2, "currency");

		mvc.perform(post("/api/account").content(malformedJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", containsString("JSON parse error: Unexpected character")))
				.andExpect(jsonPath("$.details", is("uri=/api/account")));
	}

}
