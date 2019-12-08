package com.worf.hadoop.config;

public class GroupingComparatorConfig extends AbstractConfig {

	private static final long serialVersionUID = 3983299381159309927L;
	
	private String classname;
	
	private String ref;
	
	private String input;
	
	private String output;

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
	
}
