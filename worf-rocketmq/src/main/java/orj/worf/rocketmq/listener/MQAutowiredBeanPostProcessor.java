package orj.worf.rocketmq.listener;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;

import orj.worf.rocketmq.annotation.MQOrderProducer;
import orj.worf.rocketmq.annotation.MQProducer;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.order.OrderProducer;

@Configuration
public class MQAutowiredBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	protected final static Logger logger = LoggerFactory.getLogger(MQAutowiredBeanPostProcessor.class);
	
	private ConfigurableBeanFactory factory;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		autowireProducerForObject(bean);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
	
	private void autowireProducerForObject(Object obj) {
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			autowireProducerInternal(field, obj);
			autowireOrderProducerInternal(field, obj);
		}
	}

	private void autowireProducerInternal(Field field, Object obj) {
		if (!Producer.class.isAssignableFrom(field.getType())) {
			return;
		}
		MQProducer producerAnnotation = field.getAnnotation(MQProducer.class);
		if (producerAnnotation == null) {
			return;
		}
		field.setAccessible(true);
		try {
			field.set(obj, factory.getBean(factory.resolveEmbeddedValue(producerAnnotation.value())));
		} catch (Exception e) {
			logger.error("-------------->设置Producer失败, obj:{}", obj, e);
		}
	}

	private void autowireOrderProducerInternal(Field field, Object obj) {
		if (!OrderProducer.class.isAssignableFrom(field.getType())) {
			return;
		}
		MQOrderProducer producerAnnotation = field.getAnnotation(MQOrderProducer.class);
		if (producerAnnotation == null) {
			return;
		}
		field.setAccessible(true);
		try {
			field.set(obj, factory.getBean("order_" + factory.resolveEmbeddedValue(producerAnnotation.value())));
		} catch (Exception e) {
			logger.error("-------------->设置order Producer失败, obj:{}", obj, e);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = (ConfigurableBeanFactory)beanFactory;
	}
	
}
