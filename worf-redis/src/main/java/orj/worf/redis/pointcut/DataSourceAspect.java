package orj.worf.redis.pointcut;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import orj.worf.redis.RedisDataSourceHolder;
import orj.worf.redis.annotation.RedisDS;

public class DataSourceAspect {

    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     * 
     * @param point
     * @throws Exception
     */
    public void intercept(JoinPoint point) throws Exception {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        //只解析目标类，不用追溯其父类
        resolveDataSource(target, signature.getMethod());
    }

    public void interceptAfter(JoinPoint point) throws Exception {
    	RedisDataSourceHolder.clearDataSource();
    }
    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     * 
     * @param clazz
     * @param method
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    private void resolveDataSource(Class<?> clazz, Method method) throws NoSuchMethodException, SecurityException {
        Class<?>[] types = method.getParameterTypes();
        Method m = clazz.getMethod(method.getName(), types);
        // 先用方法注解
        if (m != null && m.isAnnotationPresent(RedisDS.class)) {
        	RedisDS source = m.getAnnotation(RedisDS.class);
            RedisDataSourceHolder.setDataSource(source.value());
        } else if (clazz.isAnnotationPresent(RedisDS.class)) { // 再使用类型注解
        	RedisDS source = clazz.getAnnotation(RedisDS.class);
            RedisDataSourceHolder.setDataSource(source.value());
        }
    }

}
