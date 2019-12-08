package com.worf.hadoop.parser;

import org.w3c.dom.Element;

import com.worf.hadoop.config.CombinerConfig;

public class CombinerBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return CombinerConfig.class;
	}

}
