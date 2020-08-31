package com.lorbush.trx.view.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = -6986741235915540855L;

	private String username;
	private String password;

}
