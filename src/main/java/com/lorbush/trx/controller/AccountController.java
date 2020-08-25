package com.lorbush.trx.controller;

import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.gson.adapter.HibernateProxyTypeAdapter;
import com.lorbush.trx.gson.exclusion.ExcludeField;
import com.lorbush.trx.gson.exclusion.GsonExclusionStrategy;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.view.model.AccountModel;
import com.google.gson.GsonBuilder;
import com.lorbush.trx.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller to manage accounts
 * 
 */
@RestController
class AccountController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountService accountService;

	@Autowired
	private Helper inputParametersValidator;

	@GetMapping(value = "api/test", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String test() throws CustomException, ClassNotFoundException {
		return "Hello from account transactions report!";
	}

	/**
	 *
	 * @return the list of all accounts
	 * @throws CustomException when failed to get the accounts
	 * @throws ClassNotFoundException
	 */
	@GetMapping(value = "api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAll() throws CustomException, ClassNotFoundException {
		logger.debug("Call AccountController.getAll");
		return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy(ExcludeField.EXCLUDE_TRANSACTIONS))
				.create().toJson(accountService.findAll());
	}

	/**
	 *
	 * @param id
	 * @return the account founded by id in JSON format
	 * @throws CustomException when failed to get the account by id
	 * @throws ClassNotFoundException
	 */
	@GetMapping(value = "api/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAccountById(@PathVariable("id") Integer id) throws CustomException, ClassNotFoundException {
		logger.debug("Called AccountController.getAccountById with id={}", id);
		Account account = accountService.findById(id);
		return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy(ExcludeField.EXCLUDE_ACCOUNT))
				.create().toJson(account);
	}

	/**
	 *
	 * @param userId
	 * @return the list of Accounts of a User
	 * @throws CustomException when failed to get the accounts by user id
	 * @throws ClassNotFoundException
	 */
	@GetMapping(value = "api/accounts/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAccountsByUserId(@RequestParam("userId") String userId)
			throws CustomException, ClassNotFoundException {
		logger.debug("Call AccountController.getAccountsByUserId with userId={}", userId);

		List<Account> accounts = accountService.findByUserId(userId);
		return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy(ExcludeField.EXCLUDE_TRANSACTIONS))
				.create().toJson(accounts);
	}

	/**
	 * Creates a new account
	 * 
	 * @param accountModel
	 * @return new account created in JSON format
	 * @throws CustomException when failed to create account
	 */
	@PostMapping(value = "api/account", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createAccount(@Valid @RequestBody AccountModel accountModel) throws CustomException {
		logger.debug("Call AccountController.createAccount");
		Account account = accountService.createAccount(accountModel.getIban(), accountModel.getUserId(),
				accountModel.getCurrency());
		return new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create().toJson(account);
	}

}
