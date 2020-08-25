package com.lorbush.trx.exceptions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom error messages.
 *
 */
public class ErrorMessages {

	public static final String NO_CURRENCY_PRESENT = "The currency %s doesn't exist.";
	public static final String MALFORMED_CURRENCY = "The currency is invalid.";
	public static final String NO_ACCOUNT_FOUND = "No account with id %s exists.";
	public static final String NO_TRANSACTION_TYPE_PRESENT = "Undefined transactionType %s.";
	public static final String NO_MANDATORY_FIELD_1 = " is mandatory. Should be provided not empty.";
	public static final String NO_MANDATORY_FIELD_2 = "Field %s" + NO_MANDATORY_FIELD_1;
	public static final String NUMBER_FORMAT_MISMATCH = "'%s' should be a number";
	public static final String TYPE_MISMATCH = "%s should be of type %s";
	public static final String NO_ENOUGH_FUNDS = "Account %d has not enough credits to perform debit transaction with the amount %s";
	public static final String TRX_CURRENCY_DIFFERS_ACCOUNT_CURRENCY = "Transaction can't be created. Transaction currency %s differs from account currency %s.";
	public static final String CURRENCY_TOO_LONG = "value too long";
	public static final String CURRENCY_FK_VIOLATES_TRANSACTION = "foreign key constraint \"transaction_currency_id_fkey\"";
	public static final String CURRENCY_FK_VIOLATES_ACCOUNT = "foreign key constraint violation \"account_currency_id_fkey\"";
	public static final String TYPE_FK_VIOLATES_TRANSACTION = "foreign key constraint violation \"transaction_type_id_fkey\"";
	public static final String ACCOUNT_FK_VIOLATES_TRANSACTION = "foreign key constraint violation \"transaction_account_id_fkey\"";

	protected static final String[][] ERRORS = { { CURRENCY_FK_VIOLATES_TRANSACTION, NO_CURRENCY_PRESENT },
			{ CURRENCY_FK_VIOLATES_ACCOUNT, NO_CURRENCY_PRESENT }, { CURRENCY_TOO_LONG, MALFORMED_CURRENCY },
			{ TYPE_FK_VIOLATES_TRANSACTION, NO_TRANSACTION_TYPE_PRESENT },
			{ ACCOUNT_FK_VIOLATES_TRANSACTION, NO_ACCOUNT_FOUND } };

	/**
	 * Generates error messages
	 * 
	 * @param errorMessage
	 * @return Generated message for user
	 */
	public static String generateErrorMessageForDataIntegrityViolationException(String errorMessage) {
		String bodyOfResponse = errorMessage;
		Map<String, String> errors = Arrays.stream(ERRORS).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
		for (Iterator<String> iterator = errors.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if (errorMessage.contains(key)) {
				if (!key.equals(CURRENCY_TOO_LONG)) {
					String id = getId(errorMessage);
					bodyOfResponse = String.format(errors.get(key), id);
				} else {
					bodyOfResponse = errors.get(key);
				}
			}
		}
		return bodyOfResponse;
	}

	private static String getId(String error) {
		return error.substring(error.lastIndexOf("(") + 1, error.lastIndexOf(")"));
	}

}
