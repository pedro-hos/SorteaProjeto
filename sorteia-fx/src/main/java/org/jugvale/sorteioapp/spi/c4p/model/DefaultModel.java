package org.jugvale.sorteioapp.spi.c4p.model;

import java.io.Serializable;

/**
 * @author Pedro Hos
 */
public class DefaultModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long id = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
