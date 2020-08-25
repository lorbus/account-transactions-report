package com.lorbush.trx.helper;

import com.lorbush.trx.exceptions.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

/**
 * Helper class implementation.
 *
 */
@Validated
@Component
public class HelperImpl implements Helper<String, String> {

	/**
	 * Throws AccountException with errorMessage and errorCode when the condition is not true
	 * 
	 * @param condition
	 * @param errorMessage
	 * @param errorCode
	 * @throws CustomException
	 */
	@Override
	public void conditionIsTrue(@NotNull Boolean condition, @NotNull String errorMessage, int errorCode)
			throws CustomException {
		if (!condition) {
			throw new CustomException(errorMessage, errorCode);
		}
	}
}
