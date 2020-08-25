package com.lorbush.trx.service;

import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.exceptions.CustomException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Service for managing transactions
 *
 */
public interface TransactionService {
	List<Transaction> getTransactionsByAccountId(@NotNull Integer accountId) throws CustomException;

	Transaction createTransaction(@NotBlank String currencyName, @NotBlank String accountId,
			@NotBlank String transactionTypeId, @NotBlank String amount, String description) throws CustomException;

}
