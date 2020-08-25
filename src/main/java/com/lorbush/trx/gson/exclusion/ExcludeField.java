package com.lorbush.trx.gson.exclusion;

import com.lorbush.trx.entities.Transaction;
import com.lorbush.trx.entities.Account;

/**
 * Fields to be excluded from serialization when using gson serialization
 *
 */
public class ExcludeField {
	public static final String EXCLUDE_ACCOUNT = Transaction.class.getCanonicalName() + ".account";
	public static final String EXCLUDE_TRANSACTIONS = Account.class.getCanonicalName() + ".transactions";
}
