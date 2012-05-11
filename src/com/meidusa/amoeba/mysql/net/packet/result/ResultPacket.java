package com.meidusa.amoeba.mysql.net.packet.result;

import com.meidusa.amoeba.net.Connection;

/**
 * 
 * @author struct
 *
 */
public interface ResultPacket {
	
	public void setError(int errorCode,String errorMessage);
	/**
	 * ��ResultSet��Щ���ϲ��Ժ�д��Connection
	 * head--> fields --> eof -->rows --> eof
	 * @param conn
	 */
	public abstract void wirteToConnection(Connection conn);

}