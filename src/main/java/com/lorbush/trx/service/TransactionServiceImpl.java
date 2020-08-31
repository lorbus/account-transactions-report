package com.lorbush.trx.service;

import com.lorbush.trx.entities.Currency;
import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.entities.TransactionType;
import com.lorbush.trx.entities.Account;
import com.lorbush.trx.exceptions.ErrorMessages;
import com.lorbush.trx.exceptions.CustomException;
import com.lorbush.trx.repository.CurrencyRepository;
import com.lorbush.trx.repository.TransactionRepository;
import com.lorbush.trx.repository.TransactionTypeRepository;
import com.lorbush.trx.helper.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import static com.lorbush.trx.exceptions.ErrorMessages.NUMBER_FORMAT_MISMATCH;

@Validated
@PropertySource("classpath:application.properties")
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private Helper inputParametersValidator;

	@Value("${db.updated_by}")
	private String updatedBy;

	@Value("${application.transaction.type.credit}")
	private String transactionTypeCredit;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTransactionTypeCredit() {
		return transactionTypeCredit;
	}

	public void setTransactionTypeCredit(String transactionTypeCredit) {
		this.transactionTypeCredit = transactionTypeCredit;
	}

	/**
	 *
	 * @param accountId
	 * @return the list of transactions by account Id
	 * @throws CustomException when not possible to return the list of transactions
	 */
	@Transactional(rollbackFor = CustomException.class)
	@Override
	public List<Transaction> getTransactionsByAccountId(@NotNull Integer accountId) throws CustomException {
		log.debug("Call TransactionServiceImpl.getTransactionsByAccountId");
		Account account = accountService.findById(accountId);
		if (account != null) {
			return transactionRepository.findByAccount(account);
		} else {
			throw new CustomException(String.format(ErrorMessages.NO_ACCOUNT_FOUND, accountId.toString()),
					HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * Creates transaction for an account.
	 *
	 * @param currencyName
	 * @param accountId
	 * @param transactionTypeId
	 * @param amount
	 * @param description
	 * @return the created TRX
	 * @throws CustomException when not possible to create the TRX
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = CustomException.class)
	@Override
	public Transaction createTransaction(@NotBlank String currencyName, @NotBlank String accountId,
			@NotBlank String transactionTypeId, @NotBlank String amount, String description) throws CustomException {
		log.debug("Call TransactionServiceImpl.createTransaction");
		try {
			// Find currency
			Currency currency = currencyRepository.findByName(currencyName);
			String error = String.format(ErrorMessages.NO_CURRENCY_PRESENT, currencyName);
			inputParametersValidator.conditionIsTrue(currency != null, error, HttpStatus.BAD_REQUEST.value());
			// Find the TRX type
			TransactionType transactionType = transactionTypeRepository.getOne(transactionTypeId);
			// Check the existence of the account
			Account account = accountService.findById(Integer.valueOf(accountId));
			error = String.format(ErrorMessages.NO_ACCOUNT_FOUND, accountId);
			inputParametersValidator.conditionIsTrue(account != null, error, HttpStatus.BAD_REQUEST.value());
			// Check that the TRX and the account have same currency
			error = String.format(ErrorMessages.TRX_CURRENCY_DIFFERS_ACCOUNT_CURRENCY, currency.getName(),
					account.getCurrency().getName());
			inputParametersValidator.conditionIsTrue(account.getCurrency().getId().equals(currency.getId()), error,
					HttpStatus.BAD_REQUEST.value());
			// Update account and if not possible throws Exception
			account = accountService.updateAccountAmount(account, amount,
					transactionTypeId.equalsIgnoreCase(transactionTypeCredit));
			// Create & Save TRX
			Transaction transaction = new Transaction(transactionType, new BigDecimal(amount), account, currency,
					description, updatedBy);
			return transactionRepository.save(transaction);
		} catch (NumberFormatException e) {
			throw new CustomException(String.format(NUMBER_FORMAT_MISMATCH, amount), HttpStatus.BAD_REQUEST.value());
		}
	}

}