package com.worf.hadoop.parser;

import org.w3c.dom.Element;

import com.worf.hadoop.config.MapperConfig;

public class MapperBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return MapperConfig.class;
	}

}
