package orj.worf.validation.util;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import orj.worf.core.util.ClasspathPackageScanner;
import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.check.base.Check;

/**
 * 校验工具类
 * @author LiuZhenghua
 * 2016年5月25日 上午10:56:10
 */
public class ValidateUtil {
	
	private static List<Check> checks;

	public static void valid(Object obj) throws FastRuntimeException {
		try {
			List<Check> checks2 = getChecks();
			for (Check check : checks2) {
				check.valid(obj);
			}
		} catch (Exception e) {
			if (e instanceof FastRuntimeException) {
				throw (FastRuntimeException)e;
			}
		}
		
	}
	
	private static List<Check> getChecks() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		if (checks == null) {
			init();
		}
		return checks;
	}
	
	private synchronized static void init() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		if (checks == null) {
			checks = new ArrayList<Check>();
			Set<Class<?>> classes = ClasspathPackageScanner.scanPackage("orj.worf.validation.check", true);
			for (Class<?> clzz : classes) {
				if (Modifier.isAbstract(clzz.getModifiers()) || clzz.isInterface() || clzz.isAnnotation() || clzz.isEnum() || !Check.class.isAssignableFrom(clzz)) {
					continue;
				}
				Check check = (Check)clzz.newInstance();
				checks.add(check);
			}
		}
	}
	
}
