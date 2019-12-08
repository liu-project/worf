package orj.worf.rocketmq.listener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.annotation.Configuration;

import orj.worf.core.util.BeanRegisterUtils;
import orj.worf.core.util.ClassNameFilter;
import orj.worf.core.util.ClasspathPackageScanner;
import orj.worf.rocketmq.annotation.MQConsumer;
import orj.worf.rocketmq.annotation.MQOrderConsumer;
import orj.worf.rocketmq.annotation.MQOrderProducer;
import orj.worf.rocketmq.annotation.MQProducer;
import orj.worf.rocketmq.bean.MQScannerConfigure;
import orj.worf.rocketmq.bean.MessageListenerWrapper;
import orj.worf.rocketmq.bean.MessageOrderListenerWrapper;
import orj.worf.rocketmq.bean.MultiConsumerBean;
import orj.worf.rocketmq.bean.MultiOrderConsumerBean;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderProducer;

@Configuration
public class MQBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory)beanFactory;
		try {
			//创建消费者
			Set<Class<?>> clzzs = getClzzForMQConsumer(factory);
			registerConsumerBean(factory, clzzs);
			registerOrderConsumerBean(factory, clzzs);
			//创建生产者, 必须显创建消费者，否则消费者中注入生产者无法被识别（那时还没将消费者加入spring bean容器）
			String[] beanDefinitionNames = factory.getBeanDefinitionNames();
			registerProducer(factory, beanDefinitionNames);
			registerOrderProducer(factory, beanDefinitionNames);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	private void registerProducer(DefaultListableBeanFactory factory,
			String[] beanDefinitionNames) {
		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanDefinitionName);
			try {
				if (passCheckAutowireProducer(beanDefinition.getBeanClassName())) {
					Class<?> clzz = ClasspathPackageScanner.getClassLoader().loadClass(beanDefinition.getBeanClassName());
					Field[] fields = clzz.getDeclaredFields();
					for (Field field : fields) {
						if (!Producer.class.isAssignableFrom(field.getType())) {
							continue;
						}
						MQProducer producerAnnotation = field.getAnnotation(MQProducer.class);
						if (producerAnnotation == null) {
							continue;
						}
						String producerId = factory.resolveEmbeddedValue(producerAnnotation.value());
						try {
							if (!factory.containsBeanDefinition(producerId)) {
								Properties props = new Properties();
								props.put("ProducerId", producerId);
								props.put("AccessKey", factory.resolveEmbeddedValue("${mq.defaultAccessKey}"));
								props.put("SecretKey", factory.resolveEmbeddedValue("${mq.defaultSecretKey}"));
//								props.put("ONSAddr", "${mq.ONSAddr}");
								props.put("NAMESRV_ADDR", factory.resolveEmbeddedValue("${mq.NAMESRV_ADDR}"));
								MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
								mutablePropertyValues.add("properties", props);
								BeanRegisterUtils.registerBean(ProducerBean.class, factory, mutablePropertyValues, producerId, "start", "shutdown", false);
							}
							//此种方式设置属性值必须有setter方法，不方便，所以放到bean创建完成之后去注入
							//beanDefinition.getPropertyValues().add(field.getName(), new RuntimeBeanReference(producerAnnotation.value()));
						} catch (Exception e) {
							logger.error("-------------->生成Producer失败, {}#{}", beanDefinition.getBeanClassName(), producerId, e);
						}
					}
				}
			} catch (Throwable e) {
				logger.error("------------->scan package to add bean definition for MQ producer error, class:{}",beanDefinition.getBeanClassName(), e);
			}
		}
		
	}

	private void registerOrderProducer(DefaultListableBeanFactory factory, String[] beanDefinitionNames) {
		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanDefinitionName);
			try {
				if (passCheckAutowireProducer(beanDefinition.getBeanClassName())) {
					Class<?> clzz = ClasspathPackageScanner.getClassLoader().loadClass(beanDefinition.getBeanClassName());
					Field[] fields = clzz.getDeclaredFields();
					for (Field field : fields) {
						if (!OrderProducer.class.isAssignableFrom(field.getType())) {
							continue;
						}
						MQOrderProducer producerAnnotation = field.getAnnotation(MQOrderProducer.class);
						if (producerAnnotation == null) {
							continue;
						}
						String producerId = factory.resolveEmbeddedValue(producerAnnotation.value());
						try {
							String beanName = "order_" + producerId;
							if (!factory.containsBeanDefinition(beanName)) {
								Properties props = new Properties();
								props.put("ProducerId", producerId);
								props.put("AccessKey", factory.resolveEmbeddedValue("${mq.defaultAccessKey}"));
								props.put("SecretKey", factory.resolveEmbeddedValue("${mq.defaultSecretKey}"));
//								props.put("ONSAddr", "${mq.ONSAddr}");
								props.put("NAMESRV_ADDR", factory.resolveEmbeddedValue("${mq.NAMESRV_ADDR}"));
								MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
								mutablePropertyValues.add("properties", props);
								BeanRegisterUtils.registerBean(OrderProducerBean.class, factory, mutablePropertyValues, beanName, "start", "shutdown", false);
							}
							//此种方式设置属性值必须有setter方法，不方便，所以放到bean创建完成之后去注入
							//beanDefinition.getPropertyValues().add(field.getName(), new RuntimeBeanReference(producerAnnotation.value()));
						} catch (Exception e) {
							logger.error("-------------->生成Order Producer失败, {}#{}", beanDefinition.getBeanClassName(), producerAnnotation.value(), e);
						}
					}
				}
			} catch (Throwable e) {
				logger.error("------------->scan package to add bean definition for Order MQ producer error, class:{}",beanDefinition.getBeanClassName(), e);
			}
		}
	}

	/**
	 * 对要注入Producer的bean进行一些过滤
	 * @author LiuZhenghua
	 * 2016年11月29日 上午9:29:15
	 */
	private static boolean passCheckAutowireProducer(String beanClassName) {
		//lazy init的bean没有将class name加载
		if (beanClassName == null || beanClassName.indexOf("spring") != -1) {
			return false;
		}
		return true;
	}
	
	private void registerConsumerBean(DefaultListableBeanFactory factory, Set<Class<?>> clzzs) throws ClassNotFoundException, IOException {
		// 将定义了MQConsumer注解的listener，按照consumerId -> topic -> listener分好类到consumerMap
		Map<String, Map<String, Map<String, RuntimeBeanReference>>> consumerMap = new HashMap<String, Map<String, Map<String, RuntimeBeanReference>>>();
		for (Class<?> clzz : clzzs) {
			MQConsumer mqCosumer = clzz.getAnnotation(MQConsumer.class);
			if (mqCosumer == null) {
				continue;
			}
			logger.info("------------------------->装载message listener:{}", clzz.getCanonicalName());
			String topic = factory.resolveEmbeddedValue(mqCosumer.topic());
			String tags = factory.resolveEmbeddedValue(mqCosumer.tags());
			String consumerId = factory.resolveEmbeddedValue(mqCosumer.consumerId());
			if (consumerMap.get(consumerId) == null) {
				consumerMap.put(consumerId, new HashMap<String, Map<String, RuntimeBeanReference>>());
			}
			if (consumerMap.get(consumerId).get(topic) == null) {
				consumerMap.get(consumerId).put(topic, new HashMap<String, RuntimeBeanReference>());
			}
			String listenerBeanName = BeanRegisterUtils.registerBean(clzz, factory, false);
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			mutablePropertyValues.addPropertyValue("tags", tags);
			mutablePropertyValues.addPropertyValue("messageListener", new RuntimeBeanReference(listenerBeanName));
			String messageListenerWrapperName = BeanRegisterUtils.registerBean(MessageListenerWrapper.class, factory, mutablePropertyValues, true);
			consumerMap.get(consumerId).get(topic).put(tags, new RuntimeBeanReference(messageListenerWrapperName));
		}
		
		//注册ConsumerBean, 1个consumer一个ConsumerBean, 一个ConsuemrBean包含多个topic，一个topic包含多个listener
		for (String consumerId : consumerMap.keySet()) {
			Map<String, Map<String, RuntimeBeanReference>> topicMap = consumerMap.get(consumerId);
			ManagedMap<RuntimeBeanReference, RuntimeBeanReference> subscriptionMap = new ManagedMap<RuntimeBeanReference, RuntimeBeanReference>();
			for (String topic : topicMap.keySet()) {
				String tags = mergeListenerTags(topicMap.get(topic));
				MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
				mutablePropertyValues.addPropertyValue("topic", topic);
				mutablePropertyValues.addPropertyValue("expression", tags);
				String subsrciptionBeanName = BeanRegisterUtils.registerBean(Subscription.class, factory, mutablePropertyValues, true);
				
				mutablePropertyValues = new MutablePropertyValues();
				mutablePropertyValues.addPropertyValue("listeners", getManagedListFromMap(topicMap.get(topic)));
				String multiListenerBean = BeanRegisterUtils.registerBean(MultiConsumerBean.class, factory, mutablePropertyValues, true);
				subscriptionMap.put(new RuntimeBeanReference(subsrciptionBeanName), new RuntimeBeanReference(multiListenerBean));
			}
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			Properties props = new Properties();
			props.put("ConsumerId", consumerId);
			props.put("AccessKey", factory.resolveEmbeddedValue("${mq.defaultAccessKey}"));
			props.put("SecretKey", factory.resolveEmbeddedValue("${mq.defaultSecretKey}"));
//			props.put("ONSAddr", "${mq.ONSAddr}");
			props.put("NAMESRV_ADDR", factory.resolveEmbeddedValue("${mq.NAMESRV_ADDR}"));
			props.put("ConsumeThreadNums", factory.resolveEmbeddedValue("${" + consumerId + ".thread:20}"));
			mutablePropertyValues.add("properties", props);
			mutablePropertyValues.add("subscriptionTable", subscriptionMap);
			BeanRegisterUtils.registerBean(ConsumerBean.class, factory, mutablePropertyValues, null, "start", "shutdown", true);
		}
	}
	
	private void registerOrderConsumerBean(DefaultListableBeanFactory factory, Set<Class<?>> clzzs) {
		// 将定义了MQConsumer注解的listener，按照consumerId -> topic -> listener分好类到consumerMap
		Map<String, Map<String, Map<String, RuntimeBeanReference>>> consumerMap = new HashMap<String, Map<String, Map<String, RuntimeBeanReference>>>();
		for (Class<?> clzz : clzzs) {
			MQOrderConsumer mqCosumer = clzz.getAnnotation(MQOrderConsumer.class);
			if (mqCosumer == null) {
				continue;
			}
			logger.info("------------------------->装载message listener:{}", clzz.getCanonicalName());
			String topic = factory.resolveEmbeddedValue(mqCosumer.topic());
			String tags = factory.resolveEmbeddedValue(mqCosumer.tags());
			String consumerId = factory.resolveEmbeddedValue(mqCosumer.consumerId());
			if (consumerMap.get(consumerId) == null) {
				consumerMap.put(consumerId, new HashMap<String, Map<String, RuntimeBeanReference>>());
			}
			if (consumerMap.get(consumerId).get(topic) == null) {
				consumerMap.get(consumerId).put(topic, new HashMap<String, RuntimeBeanReference>());
			}
			String listenerBeanName = BeanRegisterUtils.registerBean(clzz, factory, false);
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			mutablePropertyValues.addPropertyValue("tags", tags);
			mutablePropertyValues.addPropertyValue("messageListener", new RuntimeBeanReference(listenerBeanName));
			String messageListenerWrapperName = BeanRegisterUtils.registerBean(MessageOrderListenerWrapper.class, factory, mutablePropertyValues, true);
			consumerMap.get(consumerId).get(topic).put(tags, new RuntimeBeanReference(messageListenerWrapperName));
		}
		
		//注册ConsumerBean, 1个consumer一个ConsumerBean, 一个ConsuemrBean包含多个topic，一个topic包含多个listener
		for (String consumerId : consumerMap.keySet()) {
			Map<String, Map<String, RuntimeBeanReference>> topicMap = consumerMap.get(consumerId);
			ManagedMap<RuntimeBeanReference, RuntimeBeanReference> subscriptionMap = new ManagedMap<RuntimeBeanReference, RuntimeBeanReference>();
			for (String topic : topicMap.keySet()) {
				String tags = mergeListenerTags(topicMap.get(topic));
				MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
				mutablePropertyValues.addPropertyValue("topic", topic);
				mutablePropertyValues.addPropertyValue("expression", tags);
				String subsrciptionBeanName = BeanRegisterUtils.registerBean(Subscription.class, factory, mutablePropertyValues, true);
				
				mutablePropertyValues = new MutablePropertyValues();
				mutablePropertyValues.addPropertyValue("listeners", getManagedListFromMap(topicMap.get(topic)));
				String multiListenerBean = BeanRegisterUtils.registerBean(MultiOrderConsumerBean.class, factory, mutablePropertyValues, true);
				subscriptionMap.put(new RuntimeBeanReference(subsrciptionBeanName), new RuntimeBeanReference(multiListenerBean));
			}
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			Properties props = new Properties();
			props.put("ConsumerId", consumerId);
			props.put("AccessKey", factory.resolveEmbeddedValue("${mq.defaultAccessKey}"));
			props.put("SecretKey", factory.resolveEmbeddedValue("${mq.defaultSecretKey}"));
//					props.put("ONSAddr", "${mq.ONSAddr}");
			props.put("NAMESRV_ADDR", factory.resolveEmbeddedValue("${mq.NAMESRV_ADDR}"));
			props.put("ConsumeThreadNums", factory.resolveEmbeddedValue("${" + consumerId + ".thread:20}"));
			mutablePropertyValues.add("properties", props);
			mutablePropertyValues.add("subscriptionTable", subscriptionMap);
			BeanRegisterUtils.registerBean(OrderConsumerBean.class, factory, mutablePropertyValues, null, "start", "shutdown", true);
		}
	}
	
	private Set<Class<?>> getClzzForMQConsumer(DefaultListableBeanFactory factory) throws IOException, ClassNotFoundException {
		ClassNameFilter mqConsumerFilter = new ClassNameFilter() {
			@Override
			public boolean accept(Class<?> clzz) {
				if ((clzz.isAnnotationPresent(MQConsumer.class) && MessageListener.class.isAssignableFrom(clzz))
						|| (clzz.isAnnotationPresent(MQOrderConsumer.class) && MessageOrderListener.class.isAssignableFrom(clzz))) {
					return true;
				}
				return false;
			}
		};
		Set<Class<?>> clzzes = new LinkedHashSet<Class<?>>();
		boolean needDefaultBasePackage = true;
		try {
			MQScannerConfigure scannerConfigure = factory.getBean(MQScannerConfigure.class);
			needDefaultBasePackage = false;
			clzzes.addAll(ClasspathPackageScanner.scanPackage(scannerConfigure.getBasePackage(), true, mqConsumerFilter));
		} catch (BeansException e) {
			
		}
		if (needDefaultBasePackage) {
			clzzes.addAll(ClasspathPackageScanner.scanPackage("com.rjs", true, mqConsumerFilter));
		}
		return clzzes;
	}

	/**
	 * 将tag list合为一个tag
	 * egg: {tag_a, tag_b} => tag_a || tag_b、{tag_a, *} => *
	 * @author LiuZhenghua
	 * 2016年12月2日 下午3:06:27
	 */
	private String mergeListenerTags(Map<String, RuntimeBeanReference> map) {
		String tags[] = new String[map.size()];
		int i = 0;
		for (String key : map.keySet()) {
			if (key.equals("*")) {
				return "*";
			}
			tags[i++] = key;
		}
		return StringUtils.join(tags, "||");
	}
	
	private ManagedList<RuntimeBeanReference> getManagedListFromMap(Map<String, RuntimeBeanReference> map) {
		ManagedList<RuntimeBeanReference> managedList = new ManagedList<RuntimeBeanReference>();
		managedList.addAll(map.values());
		return managedList;
	}
	
}
