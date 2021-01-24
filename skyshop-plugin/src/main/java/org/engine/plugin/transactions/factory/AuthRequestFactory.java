package org.engine.plugin.transactions.factory;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthRequestFactory implements Serializable {

	private static final long serialVersionUID = 1744050117179344127L;
	
	private int id;

}
