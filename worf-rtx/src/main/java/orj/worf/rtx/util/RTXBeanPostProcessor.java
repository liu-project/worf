package orj.worf.rtx.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class RTXBeanPostProcessor implements ApplicationContextAware {
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		BeanFactoryAdapter.setBeanFactory(applicationContext.getBean(BeanFactory.class));
	}
}
