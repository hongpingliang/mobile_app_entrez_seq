package com.bim.es.base;

import com.bim.core.SpinnerOption;

public abstract class SOBase extends SpinnerOption {

	public SOBase(String id, String name) {
		super(id, name);
	}

	public String getLabel() {
		return getName();
	}
}
