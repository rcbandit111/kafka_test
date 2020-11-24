package org.engine.plugin.transactions.factory;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SaleRequestFactory implements Serializable {

	private static final long serialVersionUID = 1744050117179344127L;
	
	private int id;

}
