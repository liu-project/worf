package orj.worf.datasource;

import java.util.Stack;

public class DynamicDataSourceHolder {
    /**
    * 注意：数据源标识保存在线程变量中，避免多线程操作数据源时互相干扰
    */
   private static final ThreadLocal<Stack<String>> THREAD_DATA_SOURCE = new ThreadLocal<Stack<String>>();

   public static String getDataSource() {
	   if (THREAD_DATA_SOURCE.get() != null) {
		   return THREAD_DATA_SOURCE.get().peek();
	   }
       return null;
   }

   public static void setDataSource(String dataSource) {
	   if (THREAD_DATA_SOURCE.get() == null) {
		   THREAD_DATA_SOURCE.set(new Stack<String>());
	   }
	   THREAD_DATA_SOURCE.get().push(dataSource);
   }

   public static void clearDataSource() {
       THREAD_DATA_SOURCE.get().pop();
       if (THREAD_DATA_SOURCE.get().empty()) {
    	   THREAD_DATA_SOURCE.remove();
       }
   }
}
