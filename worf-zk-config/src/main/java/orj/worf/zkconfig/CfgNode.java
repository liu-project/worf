package orj.worf.zkconfig;

import java.io.Serializable;

public class CfgNode implements Serializable {

	private static final long serialVersionUID = -7258449458328097147L;
	
	/**字段描述 */
	private String name;
	/**字段值 */
	private Object value;
	/**属性所在的bean的类型*/
	private String beanClassName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public CfgNode() {
	}
	
	public CfgNode(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public CfgNode(String name, Object value, String beanClass) {
		super();
		this.name = name;
		this.value = value;
		this.beanClassName = beanClass;
	}
	
	public String getBeanClassName() {
		return beanClassName;
	}
	
	public void setBeanClassName(String beanClass) {
		this.beanClassName = beanClass;
	}
	
	@Override
	public String toString() {
		return "CfgNode [name=" + name + ", value=" + value + ", beanClassName=" + beanClassName + "]";
	}
	
}
