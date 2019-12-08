package com.worf.hadoop.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.worf.hadoop.config.ChainReducerConfig;
import com.worf.hadoop.config.MapperConfig;
import com.worf.hadoop.config.ReducerConfig;

public class ChainReducerBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ChainReducerConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		doParseGivenFileds(element, builder, "id","name");
		List<ReducerConfig> reducerList = new ArrayList<ReducerConfig>();
		NodeList nodeList = element.getChildNodes();
		if(nodeList != null && nodeList.getLength() >0) {
			for(int i=0; i< nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if(node instanceof Element) {
					if(node.getNodeName().equals("reducerclass")) {
						try {
							reducerList.add(doParseByObject((Element)node, ReducerConfig.class));
						} catch (Exception e) {
							throw new IllegalStateException("解析chainmapper下reducerclass标签异常");
						}
					} else {
						throw new IllegalStateException("chainmapper标签只能有reducerclass子标签");
					}
				}
			}
		}
	}
}
