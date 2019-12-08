package com.worf.hadoop.parser;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.worf.hadoop.config.PartitionerConfig;

public class PartitionerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return PartitionerConfig.class;
	}

}
