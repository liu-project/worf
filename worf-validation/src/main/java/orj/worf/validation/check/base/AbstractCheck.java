package orj.worf.validation.check.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.apache.commons.lang3.StringUtils;

import orj.worf.exception.FastRuntimeException;


public abstract class AbstractCheck<T extends Annotation> implements Check {

	@Override
	@SuppressWarnings("unchecked")
	public void valid(Object obj) throws FastRuntimeException {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> annotationType = (Class<T>) pt.getActualTypeArguments()[0];
		
		Class<? extends Object> class1 = obj.getClass();
		Field[] fields = class1.getDeclaredFields();
		for (Field field : fields) {
			try {
				T annotation = field.getAnnotation(annotationType);
				String methodName = "get" + StringUtils.capitalize(field.getName());
				Method method = class1.getMethod(methodName);
				if (annotation == null) {
					annotation = (T)method.getAnnotation(annotationType);
				}
				if (annotation != null) {
					Object val = method.invoke(obj);
					checkValue(val, annotation);
				}
			} catch (Exception e) {
				if (e instanceof FastRuntimeException) {
					FastRuntimeException se = (FastRuntimeException) e;
					throw se;
				}
			}
		}
	}

	/**
	 * 校验这个值是否符合校验规则,不符合抛出异常
	 * @author LiuZhenghua
	 * 2016年11月15日 下午12:48:42
	 */
	protected abstract void checkValue(Object value, Annotation annotation) throws FastRuntimeException;

}
