package orj.worf.rtx.util;

public class BeanFactoryAdapter {

	 private static BeanFactory beanFactory;
		
	 public static Object getBean(Class<?> aClass) {
		 return beanFactory.getBean(aClass);
	 }
	 
	 public static Object getBean(String aClass) {
		 return beanFactory.getBean(aClass);
	 }
	 
	 public static void setBeanFactory(BeanFactory beanFactory) {
		 BeanFactoryAdapter.beanFactory = beanFactory;
	 }
}
