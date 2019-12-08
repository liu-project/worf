package com.worf.hadoop.config;

import java.util.List;

public class ChainMapperConfig extends AbstractConfig {

	private static final long serialVersionUID = 6462418927762957464L;

	private List<MapperConfig> mapperList;

	public List<MapperConfig> getMapperList() {
		return mapperList;
	}

	public void setMapperList(List<MapperConfig> mapperList) {
		this.mapperList = mapperList;
	}
	
}
