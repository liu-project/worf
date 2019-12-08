package orj.worf.rtx.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import orj.worf.rtx.annotation.TCCTransactional;
import orj.worf.rtx.model.TransactionContext;

public class RTXUtil {

	private static AtomicLong IncrId = new AtomicLong(0);
	
	private static String macAddress = "";
	
	static {
		try {
	    	byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
	    	// 下面代码是把mac地址拼装成String
	    	StringBuffer sb = new StringBuffer();
	    	for (int i = 0; i < mac.length; i++) {
		    	// mac[i] & 0xFF 是为了把byte转化为正整数
		    	String s = Integer.toHexString(mac[i] & 0xFF);
		    	sb.append(s.length() == 1 ? 0 + s : s);
	    	}
	    	macAddress = sb.toString();
		} catch (Exception e) {
			
		}
	}
	/**
	 * 获取拦截的目标方法
	 * @param pjp
	 * @return
	 */
	public static Method getAnnotationMethod(ProceedingJoinPoint pjp) {
		return ((MethodSignature)(pjp.getSignature())).getMethod();
	}
	
	/**
	 * 获取TCC 注解
	 * @param pjp
	 * @return
	 */
	public static TCCTransactional getTCCAnnotation(ProceedingJoinPoint pjp) {
		
		Method method = ((MethodSignature)(pjp.getSignature())).getMethod();
		TCCTransactional returnObj = method.getAnnotation(TCCTransactional.class);
		
		if(returnObj == null) {
            Method targetMethod = null;
            try {
                targetMethod = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());

                if (targetMethod != null) {
                	returnObj = targetMethod.getAnnotation(TCCTransactional.class);
                }
            } catch (NoSuchMethodException e) {
            	returnObj = null;
            }
		}
		
		return returnObj;
	}
	
	/**
	 * 获取事务上下文，事务上下文会作为参数传递
	 * @param arrayOb
	 * @return
	 */
	public static TransactionContext getRTXtransactionContext(Object[] arrayOb) {
		TransactionContext context = null;
		if(arrayOb != null && arrayOb.length > 0) {
			for(Object obj : arrayOb) {
				if(obj != null && obj.getClass().isAssignableFrom(TransactionContext.class)) {
					context = (TransactionContext)obj;
					break;
				}
			}
		}
		
		return context;
	}
	
	/**
	 * @Description 校验方法上的第一个参数类型是否是TransactionContext类型
	 * @param pjp
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月23日
	 */
	public static boolean hasTransactionContextParam(ProceedingJoinPoint pjp) {
		
		Method method = ((MethodSignature)(pjp.getSignature())).getMethod();
		Class[] clazzs = method.getParameterTypes();
		if(clazzs != null && clazzs.length >0) {
			if(clazzs[0].equals(TransactionContext.class)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 事务ID
	 * @return
	 */
	public static String getTransactionId(String prefix) {
		if(!StringUtils.isBlank(macAddress)) {
			prefix = macAddress;
		}
		//前缀
		StringBuilder builder = new StringBuilder(prefix);
		//进程ID
		builder.append(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
		//分隔符
		builder.append("#");
		//当前毫秒时间
		builder.append(System.currentTimeMillis());
		//自增值，每次重启后清零
		return builder.append(IncrId.addAndGet(1)).toString();
	}
	
    public static String getTargetClassName(Class aClass, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        Class findClass = aClass;
        //aClass可能是个代理类，需找到其目标类
        Class[] clazzes = findClass.getInterfaces();
        for(Class clazz : clazzes) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                method = null;
            }
            if (method != null) {
                return clazz.getCanonicalName();
            }
        }
        findClass = findClass.getSuperclass();
        return aClass.getCanonicalName();
    }
}
