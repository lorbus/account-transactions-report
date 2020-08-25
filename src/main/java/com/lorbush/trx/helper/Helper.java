package com.lorbush.trx.helper;

import com.lorbush.trx.exceptions.CustomException;
import javax.validation.constraints.NotNull;

/**
 * Helper class
 * 
 * @param <K>
 * @param <V>
 *
 */
public interface Helper<K, V> {
	void conditionIsTrue(@NotNull Boolean condition, @NotNull String errorMessage, int errorCode)
			throws CustomException;

}