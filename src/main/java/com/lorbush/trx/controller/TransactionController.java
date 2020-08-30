package com.lorbush.trx.controller;

import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.gson.adapter.HibernateProxyTypeAdapter;
import com.lorbush.trx.gson.exclusion.ExcludeField;
import com.lorbush.trx.gson.exclusion.GsonExclusionStrategy;
import com.lorbush.trx.helper.Helper;
import com.lorbush.trx.service.TransactionService;
import com.lorbush.trx.view.model.TransactionListModel;
import com.lorbush.trx.view.model.TransactionModel;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * TRX controller to manage account transactions
 *
 */
@RestController
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String GBP = "GBP";

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private Helper inputParametersValidator;

	/**
	 *
	 * @param id
	 * @return all the list of TRX by account Id
	 * @throws CustomException when failed to obtain Transactions
	 * @throws ClassNotFoundException
	 */
	@GetMapping(value = "api/v1/accounts/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAccountTransactionsById(@PathVariable("id") Integer id)
			throws CustomException, ClassNotFoundException {
		logger.debug("Call TransactionController.getAccountTransactionsById with parameter accountId={}", id);

		List<Transaction> transactionListTemp = transactionService.getTransactionsByAccountId(id);
		BigDecimal gbpSumTrx = new BigDecimal(0);

		for(Transaction trx : transactionListTemp) {
			if(trx.getCurrency().getId().equals(GBP)) {
				gbpSumTrx = gbpSumTrx.add(trx.getAmount());
			}
		}

		TransactionListModel transactionListModel = new TransactionListModel();
		transactionListModel.setTransactions(transactionListTemp);
		transactionListModel.setPoundTotalAmount(gbpSumTrx);

		logger.info("Transactions retrieved with success");

		return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy(ExcludeField.EXCLUDE_ACCOUNT))
				.create().toJson(transactionListModel);
	}

	/**
	 * Creates account transaction
	 *
	 * @param transactionModel
	 * @return the transaction created as JSON
	 * @throws CustomException when transaction could not be created
	 * @throws ClassNotFoundException
	 */
	@PostMapping(value = "api/v1/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createAccountTransaction(@Valid @RequestBody TransactionModel transactionModel)
			throws CustomException, ClassNotFoundException {
		logger.debug("Call TransactionController.createAccountTransaction");

		Transaction transaction = transactionService.createTransaction(transactionModel.getCurrency(),
				transactionModel.getAccountId(), transactionModel.getTransactionTypeId(), transactionModel.getAmount(),
				transactionModel.getDescription());

		logger.info("TRX created with id = " + transaction.getId());

		return new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
				.setExclusionStrategies(new GsonExclusionStrategy(ExcludeField.EXCLUDE_TRANSACTIONS)).create()
				.toJson(transaction);
	}
}
