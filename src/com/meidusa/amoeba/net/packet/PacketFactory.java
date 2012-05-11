package com.meidusa.amoeba.net.packet;

import com.meidusa.amoeba.net.Connection;

/**
 * ��Packet �������𴴽� Packet
 * @author struct
 *
 */
public interface PacketFactory<T extends Packet> {
	
	/**
	 * 
	 * @param conn ���ݰ�����ԴConnection
	 * @param buffer ���ݰ��ֽ�
	 * @return Packet Ŀ�����ݰ�
	 */
	T createPacket(Connection conn,byte[] buffer);

}
