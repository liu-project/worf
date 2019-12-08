package com.worf.hadoop.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.worf.hadoop.config.ChainMapperConfig;
import com.worf.hadoop.config.ChainReducerConfig;
import com.worf.hadoop.config.CombinerConfig;
import com.worf.hadoop.config.GroupingComparatorConfig;
import com.worf.hadoop.config.MapperConfig;
import com.worf.hadoop.config.MrJobConfig;
import com.worf.hadoop.config.PartitionerConfig;
import com.worf.hadoop.config.ReducerConfig;

public class MrJobBeanDefinitionParser extends AbstractHadoopBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return MrJobConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		try {
			doParseGivenFileds(element, builder, "id","name","classname");
			NodeList nodeList = element.getChildNodes();
			if(nodeList != null && nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
	                if (node instanceof Element) {
	                	if("chainmapper".equals(node.getNodeName())) {
	                		Element childElement = (Element)node;
	                		ChainMapperConfig chainConfig = ChainMapperConfig.class.newInstance();
	                		List<MapperConfig> mapperList = new ArrayList<MapperConfig>();
	                		NodeList childNodeList = childElement.getChildNodes();
	                		if(childNodeList != null && childNodeList.getLength() >0) {
	                			for(int j=0; i< childNodeList.getLength(); j++) {
	                				Node childNode = childNodeList.item(j);
	                				if(childNode instanceof Element) {
	                					if(childNode.getNodeName().equals("mapperclass")) {
	                						try {
	                							mapperList.add(doParseByObject((Element)childNode, MapperConfig.class));
	                						} catch (Exception e) {
	                							throw new IllegalStateException("解析chainmapper下mapperclass标签异常");
	                						}
	                					} else {
	                						throw new IllegalStateException("chainmapper标签只能有mapperclass子标签");
	                					}
	                				}
	                			}
	                		}
	                		chainConfig.setMapperList(mapperList);
	                		builder.addPropertyValue("chainMapper", doParseByObject((Element)node, MapperConfig.class));
	                	} else if("mapper".equals(node.getNodeName())) {
	                		builder.addPropertyValue("mapper", doParseByObject((Element)node, MapperConfig.class));
	                	} else if("chainreducer".equals(node.getNodeName())) {
	                		Element childElement = (Element)node;
	                		ChainReducerConfig chainReducerConfig = ChainReducerConfig.class.newInstance();
	                		List<ReducerConfig> reducerList = new ArrayList<ReducerConfig>();
	                		NodeList childNodeList = childElement.getChildNodes();
	                		if(childNodeList != null && childNodeList.getLength() >0) {
	                			for(int j=0; i< childNodeList.getLength(); j++) {
	                				Node childNode = childNodeList.item(j);
	                				if(childNode instanceof Element) {
	                					if(childNode.getNodeName().equals("reducerclass")) {
	                						try {
	                							reducerList.add(doParseByObject((Element)childNode, ReducerConfig.class));
	                						} catch (Exception e) {
	                							throw new IllegalStateException("解析chainreducer下reducerclass标签异常");
	                						}
	                					} else {
	                						throw new IllegalStateException("chainreducer标签只能有reducerclass子标签");
	                					}
	                				}
	                			}
	                		}
	                		chainReducerConfig.setReducerList(reducerList);
	                		builder.addPropertyValue("chainMapper", doParseByObject((Element)node, MapperConfig.class));
	                	} else if("reducer".equals(node.getNodeName())) {
	                		builder.addPropertyValue("reducer", doParseByObject((Element)node, ReducerConfig.class));
	                	} else if("combiner".equals(node.getNodeName())) {
	                		builder.addPropertyValue("combiner", doParseByObject((Element)node, CombinerConfig.class));
	                	} else if("groupingcomparator".equals(node.getNodeName())) {
	                		builder.addPropertyValue("partitioner", doParseByObject((Element)node, PartitionerConfig.class));
	                	} else if("partitioner".equals(node.getNodeName())) {
	                		builder.addPropertyValue("comparator", doParseByObject((Element)node, GroupingComparatorConfig.class));
	                	} 
	                }
				}
			} else {
				throw new IllegalStateException("mrjob标签必须有子标签");
			}	
		} catch (Exception e) {
			throw new IllegalStateException("解析mrjob标签异常");
		}
	}

}
