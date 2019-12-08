package com.worf.hadoop.parser;

import java.lang.reflect.Field;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.ReflectionUtils;
import org.w3c.dom.Element;

public abstract class AbstractHadoopBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		Field[] fields = getBeanClass(element).getDeclaredFields();
		for(Field field : fields) {
			builder.addPropertyValue(field.getName(), element.getAttribute(field.getName()));
		}
	}
	
	protected void doParseGivenFileds(Element element, BeanDefinitionBuilder builder,
			String... fields) {
		for(String field : fields) {
			builder.addPropertyValue(field, element.getAttribute(field));
		}
	}
	
	protected <T> T doParseByObject(Element element, Class<? extends T> clazz) throws Exception {
		T t = clazz.newInstance();
		Field[] fields =  clazz.getDeclaredFields();
		for(Field field : fields) {
			ReflectionUtils.setField(field, t, element.getAttribute(field.getName()));
		}
		return t;
	}

}
