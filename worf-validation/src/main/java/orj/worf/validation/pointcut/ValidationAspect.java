package orj.worf.validation.pointcut;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.Check;
import orj.worf.validation.annotation.NotCheck;
import orj.worf.validation.util.ValidateUtil;

/**
 * 因抛出异常必须在源方法中声明，所以
 * @author LiuZhenghua
 * 2016年11月14日 上午9:39:43
 */
@Aspect
@Order(189)
public class ValidationAspect {

	@Before("@within(orj.worf.validation.annotation.Check) || @annotation(orj.worf.validation.annotation.Check)")
	public void beforeValidation(JoinPoint jp) throws FastRuntimeException {
		Object[] args = jp.getArgs();
		if (args.length == 0) {
			return;
		}
		Class<?> declaringType = jp.getSignature().getDeclaringType();
		NotCheck notCheck = (NotCheck)declaringType.getAnnotation(NotCheck.class);
		if (notCheck != null) {
			//如果类上面用了NotCheck，方法上面没用Check，则不校验。
			Method method = getMethodByJoinPoint(jp);
			if (method == null) {
				return;
			}
			Check check = (Check)method.getAnnotation(Check.class);
			if (check == null) {
				return;
			}
		} else {
			//如果类上面没有用NotCheck，方法上面用了NotCheck，则不校验。
			Method method = getMethodByJoinPoint(jp);
			if (method != null) {
				notCheck = (NotCheck)method.getAnnotation(NotCheck.class);
				if (notCheck != null) {
					return;
				}
			}
		}
		
		for (Object obj : args) {
			if (!isPrimitive(obj)) {
				ValidateUtil.valid(obj);
			}
		}
	}
	
	public static boolean isPrimitive(Object obj) {
		return obj == null 	//null参数不参与校验
				|| obj.getClass().isPrimitive()
				|| obj.getClass().equals(String.class)
				|| obj.getClass().equals(Integer.class)
				|| obj.getClass().equals(Byte.class)
				|| obj.getClass().equals(Long.class)
				|| obj.getClass().equals(Double.class)
				|| obj.getClass().equals(Float.class)
				|| obj.getClass().equals(Character.class)
				|| obj.getClass().equals(Short.class)
				|| obj.getClass().equals(BigDecimal.class)
				|| obj.getClass().equals(BigInteger.class)
				|| obj.getClass().equals(Boolean.class)
				|| obj.getClass().equals(Date.class)
				;
	}

	private static Method getMethodByJoinPoint(JoinPoint jp) {
		Signature signature = jp.getSignature();
		if (signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature)signature;
			return methodSignature.getMethod();
		} else {
			Method method = null;
			Object[] args = jp.getArgs();
			Class<?> declaringType = jp.getSignature().getDeclaringType();
			String methodName = jp.getSignature().getName();
			List<Class<?>> paramTypes = new ArrayList<Class<?>>();
			for (Object obj : args) {
				//参数值为null的时候，这里取不到类型,直接不校验
				if (obj == null) {
					return null;
				}
				paramTypes.add(obj.getClass());
			}
			try {
				method = declaringType.getMethod(methodName, (Class<?>[])paramTypes.toArray(new Class<?>[paramTypes.size()]));
			} catch (Exception e) {
				
			}
			return method;
		}
		
	}
	
}
