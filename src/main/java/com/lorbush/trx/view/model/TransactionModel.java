package com.lorbush.trx.view.model;

import com.lorbush.trx.exceptions.ErrorMessages;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransactionModel {

	@NotBlank(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String currency;

	@NotBlank(message = "Field accountId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field accountId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String accountId;

	@NotBlank(message = "Field transactionTypeId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field transactionTypeId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String transactionTypeId;

	@NotBlank(message = "Field amount" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field amount" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String amount;

	@NotBlank(message = "Field description" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field description" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String description;

	public TransactionModel() {
	}

	public TransactionModel(String currency, String accountId, String transactionTypeId, String amount,
			String description) {
		this.currency = currency;
		this.accountId = accountId;
		this.transactionTypeId = transactionTypeId;
		this.amount = amount;
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(String transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
