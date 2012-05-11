package com.meidusa.amoeba.mysql.context;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.meidusa.amoeba.context.RuntimeContext;
import com.meidusa.amoeba.mysql.util.CharsetMapping;
import com.meidusa.amoeba.util.InitialisationException;

public class MysqlRuntimeContext extends RuntimeContext {
	public final static String SERVER_VERSION = "5.1.45-mysql-amoeba-proxy-2.1.0-RC5";
	private static Logger logger = Logger.getLogger(MysqlRuntimeContext.class);
	private byte               serverCharsetIndex;
	private int statementCacheSize = 500;
	private long statementExpiredTime = 5;
    public void setServerCharsetIndex(byte serverCharsetIndex) {
        this.serverCharsetIndex = serverCharsetIndex;
        this.setServerCharset(CharsetMapping.INDEX_TO_CHARSET[serverCharsetIndex & 0xff]);
    }

    public byte getServerCharsetIndex() {
        if (serverCharsetIndex > 0) return serverCharsetIndex;
        return CharsetMapping.getCharsetIndex(this.getServerCharset());
    }
    
    public int getStatementCacheSize() {
		return statementCacheSize;
	}

	public void setStatementCacheSize(int statementCacheSize) {
		if(statementCacheSize <0){
			statementCacheSize = 50;
		}
		this.statementCacheSize = statementCacheSize;
		
	}

	public long getStatementExpiredTime() {
		return statementExpiredTime;
	}

	public void setStatementExpiredTime(long statementExpiredTime) {
		this.statementExpiredTime = statementExpiredTime;
	}
	
	public void init() throws InitialisationException{
		super.init();
        Level level = logger.getLevel();
        logger.setLevel(Level.INFO);
		logger.info("Amoeba for Mysql current versoin="+SERVER_VERSION);
		logger.setLevel(level);
	}
}
