package com.aqua.fileParser;
/**
 * this class presents one column (in text log file) data
 * @author aqua
 *
 */
public class FPColumns {
	private String regExp = null;

	private int group = -1;

	private String ColName = null;

	public FPColumns(String name, String exp, int group) {
		this(name, exp);
		this.group = group;
	}

	public FPColumns(String name, String exp) {
		super();
		ColName = name;
		regExp = exp;
	}

	public String getColName() {
		return ColName;
	}

	public void setColName(String colName) {
		ColName = colName;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

} // of class FPCOlumns
