package com.lorbush.trx.service;

import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.CustomException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Service to managing accounts
 *
 */
public interface AccountService {
	List<Account> findAll() throws CustomException;

	Account findById(@NotNull Integer id) throws CustomException;

	List<Account> findByUserId(@NotBlank String userId) throws CustomException;

	Account createAccount(@NotBlank String iban, @NotBlank String userId, @NotBlank String currencyName)
			throws CustomException;

	Account updateAccountAmount(@NotNull Account account, @NotBlank String amount, @NotNull Boolean isCredit)
			throws CustomException;
}