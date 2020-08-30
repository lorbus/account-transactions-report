package com.lorbush.trx.exceptions.exceptionHandler;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = -261503645686776342L;

	public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
