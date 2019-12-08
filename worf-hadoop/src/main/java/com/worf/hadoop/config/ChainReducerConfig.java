package com.worf.hadoop.config;

import java.util.List;

public class ChainReducerConfig extends AbstractConfig {

	private static final long serialVersionUID = -6178033108703179365L;

	private List<ReducerConfig> reducerList;

	public List<ReducerConfig> getReducerList() {
		return reducerList;
	}

	public void setReducerList(List<ReducerConfig> reducerList) {
		this.reducerList = reducerList;
	}
	
}
