package com.lorbush.trx.view.model;

import com.lorbush.trx.exceptions.ErrorMessages;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
