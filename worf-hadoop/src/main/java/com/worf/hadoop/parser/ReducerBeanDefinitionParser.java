package com.worf.hadoop.parser;

import org.w3c.dom.Element;

import com.worf.hadoop.config.ReducerConfig;

public class ReducerBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ReducerConfig.class;
	}

}
