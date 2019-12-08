package com.worf.hadoop.config;

public class ReducerConfig extends AbstractConfig {

	private static final long serialVersionUID = 2631499480696431801L;

	private String classname;
	
	private String ref;
	
	private String input;
	
	private String output;

	private Integer order;
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ReducerConfig [classname=" + classname + ", ref=" + ref
				+ ", input=" + input + ", output=" + output + ", order="
				+ order + "]";
	}
	
}
