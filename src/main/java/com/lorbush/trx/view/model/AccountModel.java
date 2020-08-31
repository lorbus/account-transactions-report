package com.lorbush.trx.view.model;

import com.lorbush.trx.entities.User;
import com.lorbush.trx.exceptions.ErrorMessages;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
