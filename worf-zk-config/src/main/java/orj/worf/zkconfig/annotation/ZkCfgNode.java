package orj.worf.zkconfig.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明一个配置类走ZK
 * @author Liuzhenghua
 * 2017年11月22日 下午10:03:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZkCfgNode {

	/**
	 * 配置类描述，不填则默认是BeanName
	 * @author Liuzhenghua
	 * 2017年11月22日 下午10:37:07
	 */
	String value() default "defaultValue";
	
	/**
	 * 配置类所属项目名，有的
	 * @author LiuZhenghua 2017年12月4日 上午11:48:48
	 */
	String projectName() default "defaultValue";
}
