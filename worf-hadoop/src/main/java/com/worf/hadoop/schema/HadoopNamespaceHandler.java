package com.worf.hadoop.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.worf.hadoop.parser.ChainMapperBeanDefinitionParser;
import com.worf.hadoop.parser.ChainReducerBeanDefinitionParser;
import com.worf.hadoop.parser.CombinerBeanDefinitionParser;
import com.worf.hadoop.parser.GroupComparatorBeanDefinitionParser;
import com.worf.hadoop.parser.MapperBeanDefinitionParser;
import com.worf.hadoop.parser.MrJobBeanDefinitionParser;
import com.worf.hadoop.parser.PartitionerBeanDefinitionParser;
import com.worf.hadoop.parser.ReducerBeanDefinitionParser;

/**
 * 
 * @author lin_r
 *
 */
public class HadoopNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("mrjob", new MrJobBeanDefinitionParser());
		registerBeanDefinitionParser("chainmapper", new ChainMapperBeanDefinitionParser());
		registerBeanDefinitionParser("chainreducer", new ChainReducerBeanDefinitionParser());
		registerBeanDefinitionParser("mapper", new MapperBeanDefinitionParser());
		registerBeanDefinitionParser("reducer", new ReducerBeanDefinitionParser());
		registerBeanDefinitionParser("combiner", new CombinerBeanDefinitionParser());
		registerBeanDefinitionParser("groupingcomparator", new GroupComparatorBeanDefinitionParser());
		registerBeanDefinitionParser("partitioner", new PartitionerBeanDefinitionParser());
	}

}
