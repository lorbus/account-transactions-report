package com.lorbush.trx.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

	private String secretKey = "P-0123456789";
	private long validityInMs = 3600000; // milliseconds = 1h

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public long getValidityInMs() {
		return validityInMs;
	}

	public void setValidityInMs(long validityInMs) {
		this.validityInMs = validityInMs;
	}

}
