package orj.worf.rtx.model;

import java.io.Serializable;

public class InvokeMethod implements Serializable{

	private static final long serialVersionUID = -7462272110619559923L;

	private String className;  //所属类全路径名
	
	private String methodName;  // 方法名
	
	private Class[] parameterTypes; //方法参数所属类型

    private Object[] args;  //方法参数

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
    
}
