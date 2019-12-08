package orj.worf.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

public class ClasspathPackageScanner {
	
	private static ClassLoader classLoader;
	
	public static synchronized ClassLoader getClassLoader() {
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		return classLoader;
	}
	
	/**
	 * 扫描包下面的类
	 * @author LiuZhenghua
	 * 2016年11月16日 下午1:37:34
	 */
	public static Set<Class<?>> scanPackage(String basePackage, boolean recursive) throws IOException, ClassNotFoundException {
		return scanPackage(basePackage, recursive, null);
	}
	
	/**
	 * 扫描包下面的类
	 * @author LiuZhenghua
	 * 2016年11月16日 下午1:37:34
	 */
	public static Set<Class<?>> scanPackage(String basePackage, boolean recursive, ClassNameFilter classNameFilter) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageDirName = StringUtils.replace(basePackage, ".", "/");
		Enumeration<URL> urls = getClassLoader().getResources(packageDirName);
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				scanClassesByFile(basePackage, url, recursive, classes, classNameFilter);
			} else if ("jar".equals(protocol)) {
				scanClassesByJar(packageDirName, url, recursive, classes, classNameFilter);
			}
		}
		return classes;
	}

	/**
	 * 扫描包下的类（该包存在于jar文件中）
	 * @author LiuZhenghua
	 * 2016年11月16日 上午11:25:37
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private static void scanClassesByJar(String packageDirName, URL url, boolean recursive, Set<Class<?>> classes, ClassNameFilter classNameFilter) throws IOException, ClassNotFoundException {
		JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String name = jarEntry.getName();
			if (name.charAt(0) == '/') {
				name = name.substring(0);
			}
			//com/fasterxml/类似这种最后一/结尾的是包名
			if (name.length() == 0 || name.charAt(name.length() - 1) == '/' || !name.endsWith(".class")) {
				continue;
			}
			if (packageDirName.equals("/")) {
				packageDirName = "";
			}
			if (StringUtils.isBlank(packageDirName) || name.startsWith(packageDirName)) {
				//去掉.class
				name = name.substring(0, name.length() - 6);
				String className = name;
				if (packageDirName != null && packageDirName.length() > 0) {
					className = name.substring(packageDirName.length() + 1);	
				}
				//内部类的路径为：com/fasterxml/classmate/AnnotationOverrides$StdImpl.class带有$符号
				if ((!recursive && className.indexOf("/") != -1) || className.lastIndexOf("$") != -1) {
					continue;
				}
				name = StringUtils.replace(name, "/", ".");
				try {
					Class<?> loadClass = getClassLoader().loadClass(name);
					if (classNameFilter != null && !classNameFilter.accept(loadClass)) {
						continue;
					}
					classes.add(loadClass);
				} catch(NoClassDefFoundError e) {
				}
			}
		}
	}

	/**
	 * 扫描包下的类（该包以文件的形式存储在）
	 * @author LiuZhenghua
	 * 2016年11月16日 上午9:53:23
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private static void scanClassesByFile(String basePackage, URL url, final boolean recursive, Set<Class<?>> classes, ClassNameFilter classNameFilter) throws IOException, ClassNotFoundException {
		File dir = new File(url.getFile());
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles(new FileFilter() {
			//循环加载子类的话就加载目录，否则加载class
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".class") || (recursive && file.isDirectory());
			}
		});
		for (File file : files) {
			String packageName = basePackage;
			if (StringUtils.isNotBlank(packageName)) {
				packageName = packageName + ".";
			}
			if (file.isDirectory()) {
				scanClassesByFile(packageName + file.getName(), file.toURI().toURL(), recursive, classes, classNameFilter);
				continue;
			}
			//排除内部类（内部类名字有$符号）
			if (file.getName().endsWith(".class") && file.getName().indexOf("$") == -1) {
				Class<?> loadClass = getClassLoader().loadClass(packageName + file.getName().substring(0, file.getName().length() - 6));
				if (classNameFilter != null && !classNameFilter.accept(loadClass)) {
					continue;
				}
				classes.add(loadClass);
			}
		}
	}
	
}
