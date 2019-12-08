package orj.worf.rtx.util;

public interface BeanFactory {
	
	 Object getBean(Class<?> aClass);
	 
	 Object getBean(String aClass);
}
