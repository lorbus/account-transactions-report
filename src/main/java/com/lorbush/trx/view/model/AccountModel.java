package com.lorbush.trx.view.model;

import com.lorbush.trx.exceptions.ErrorMessages;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AccountModel {

	@NotBlank(message = "Field iban" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field iban" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String iban;

	@NotBlank(message = "Field userId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field userId" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String userId;

	@NotBlank(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String currency;

	public AccountModel() {
	}

	public AccountModel(String iban, String userId, String currency) {
		this.iban = iban;
		this.userId = userId;
		this.currency = currency;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
}
