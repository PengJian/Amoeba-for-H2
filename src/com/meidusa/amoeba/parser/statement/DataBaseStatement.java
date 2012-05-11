package com.meidusa.amoeba.parser.statement;

import com.meidusa.amoeba.parser.expression.Expression;

public class DataBaseStatement implements Statement {

	private String sql;
	private String schema;
	
	public Expression getExpression() {
		return null;
	}

	public int getParameterCount() {
		return 0;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSql() {
		return sql;
	}

	public boolean isPrepared() {
		return false;
	}

	public void setParameterCount(int count) {
		
	}

}
