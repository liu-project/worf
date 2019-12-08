package com.worf.hadoop.config;

import java.io.Serializable;

public abstract class AbstractConfig implements Serializable {

	private static final long serialVersionUID = -7953216273274126216L;
	
	protected String id;

	protected String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
