package com.lorbush.trx.view.model;

import com.lorbush.trx.entities.User;
import com.lorbush.trx.exceptions.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {

	@NotBlank(message = "Field iban" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field iban" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String iban;

	@NotBlank(message = "Field user" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field user" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private User user;

	@NotBlank(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	@NotNull(message = "Field currency" + ErrorMessages.NO_MANDATORY_FIELD_1)
	private String currency;

	public AccountModel() {
	}

	public AccountModel(String iban, User user, String currency) {
		this.iban = iban;
		this.user = user;
		this.currency = currency;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
