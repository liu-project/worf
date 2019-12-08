package orj.worf.rocketmq.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.aliyun.openservices.ons.api.bean.ConsumerBean;

public class ConsumerFactoryBean implements FactoryBean<ConsumerBean>, InitializingBean {
	
	@Autowired
	BeanDefinitionRegistry registry;

	@Override
	public ConsumerBean getObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		return ConsumerBean.class;
	}
	
	

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(registry);
	}

}
