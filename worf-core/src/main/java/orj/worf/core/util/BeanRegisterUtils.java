package orj.worf.core.util;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;

/**
 * 此工具类用于实现了 {@link BeanDefinitionRegistry}接口的类，可以通过代码的方式往spring容器中添加bean定义
 * 注册bean定义的工具类
 * @author LiuZhenghua
 * 2016年11月30日 下午5:52:43
 */
public class BeanRegisterUtils {
	
	private static DefaultBeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	/**
	 * 注册bean definition
	 * @param clzz 要注册的类
	 * @param properties bean内的属性值
	 * @param beanName 放到spring容器中的name
	 * @author LiuZhenghua
	 * 2016年11月30日 下午3:45:53
	 */
	public static String registerBean(Class<?> clzz, BeanDefinitionRegistry registry, MutablePropertyValues properties, String beanName, String initMethod, String destoryMethod, boolean forceRegister) {
		//如果没有传bean name， 或者强制注册bean， 这个时候bean定义已经存在的话，则直接返回
		if (beanName != null && !forceRegister && registry.containsBeanDefinition(beanName)) {
			return beanName;
		}
		AnnotatedGenericBeanDefinition annotatedGenericBeanDefinition = new AnnotatedGenericBeanDefinition(clzz);
		annotatedGenericBeanDefinition.setInitMethodName(initMethod);
		annotatedGenericBeanDefinition.setDestroyMethodName(destoryMethod);
		annotatedGenericBeanDefinition.setScope("singleton");
		annotatedGenericBeanDefinition.setPropertyValues(properties);
		annotatedGenericBeanDefinition.setLazyInit(false);
		
		AnnotationConfigUtils.processCommonDefinitionAnnotations(annotatedGenericBeanDefinition);
		if (beanName == null) {
			beanName = beanNameGenerator.generateBeanName(annotatedGenericBeanDefinition, registry);
		}
		BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(annotatedGenericBeanDefinition, beanName);
		BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
		return beanName;
	}

	public static String registerBean(Class<?> clzz, BeanDefinitionRegistry registry, MutablePropertyValues properties, boolean forceRegister) {
		return registerBean(clzz, registry, properties, null, null, null, forceRegister);
	}
	
	public static String registerBean(Class<?> clzz, BeanDefinitionRegistry registry, boolean forceRegister) {
		return registerBean(clzz, registry, null, forceRegister);
	}
}
