package com.worf.hadoop.parser;

import org.w3c.dom.Element;

import com.worf.hadoop.config.GroupingComparatorConfig;

public class GroupComparatorBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return GroupingComparatorConfig.class;
	}

}
