package com.meidusa.amoeba.route;

import com.meidusa.amoeba.util.StringUtil;

public class SqlQueryObject implements Request{
	public boolean isPrepared;
	public String sql;
	public Object[] parameters;
	public boolean isRead;
	public boolean isPrepared() {
		return isPrepared;
	}
	
	public boolean isRead() {
		return isRead;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{sql=[").append(sql).append("]").append(", parameter=");
		buffer.append(StringUtil.toString(parameters)).append("}");
		return buffer.toString();
	}
}
