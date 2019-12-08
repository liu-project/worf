package orj.worf.zkconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import orj.worf.zkconfig.CfgNode;

/**
 * 走ZK配置类中的字段，主要是为了给字段添加描述 {@link CfgNode#getName()}
 * @author Liuzhenghua
 * 2017年11月22日 下午10:06:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZkCfgNodeField {
	
	String value() default "defaultValue";
}
