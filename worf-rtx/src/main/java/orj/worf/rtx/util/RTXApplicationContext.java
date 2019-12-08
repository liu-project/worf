package orj.worf.rtx.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("rTXApplicationContext")
public class RTXApplicationContext implements BeanFactory, ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
    public Object getBean(Class<?> aClass) {
        return this.applicationContext.getBean(aClass);
    }
    
    public Object getBean(String aclass) {
    	return this.applicationContext.getBean(aclass);
    }
}
