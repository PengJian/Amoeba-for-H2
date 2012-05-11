/*
 * Copyright 2008-2108 amoeba.meidusa.com 
 * 
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU AFFERO GENERAL PUBLIC LICENSE as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU AFFERO GENERAL PUBLIC LICENSE for more details. 
 * 	You should have received a copy of the GNU AFFERO GENERAL PUBLIC LICENSE along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.meidusa.amoeba.mysql.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meidusa.amoeba.mysql.handler.session.CommandStatus;
import com.meidusa.amoeba.mysql.handler.session.ConnectionStatuts;
import com.meidusa.amoeba.mysql.net.CommandInfo;
import com.meidusa.amoeba.mysql.net.MysqlClientConnection;
import com.meidusa.amoeba.mysql.net.MysqlServerConnection;
import com.meidusa.amoeba.mysql.net.packet.EOFPacket;
import com.meidusa.amoeba.mysql.net.packet.ErrorPacket;
import com.meidusa.amoeba.mysql.net.packet.MysqlPacketBuffer;
import com.meidusa.amoeba.mysql.net.packet.OkPacket;
import com.meidusa.amoeba.mysql.net.packet.QueryCommandPacket;
import com.meidusa.amoeba.net.Connection;
import com.meidusa.amoeba.net.packet.Packet;
import com.meidusa.amoeba.parser.statement.InsertStatement;
import com.meidusa.amoeba.parser.statement.Statement;
import com.meidusa.amoeba.util.StringUtil;

class CommandQueue{
	protected List<CommandInfo> sessionInitQueryQueue; //���еĴӿͻ��˷��͹����� command ����
	protected CommandInfo currentCommand;//��ǰ��query
	protected Map<MysqlServerConnection,ConnectionStatuts> connStatusMap = new HashMap<MysqlServerConnection,ConnectionStatuts>();
	boolean mainCommandExecuted;
	private MysqlClientConnection source;
	protected Statement statment;
	public CommandQueue(MysqlClientConnection source,Statement statment){
		this.source = source;
		this.statment = statment;
	}
	public boolean isMultiple(){
		return connStatusMap.size()>1;
	}
	
	public void clearAllBuffer(){
		Collection<ConnectionStatuts> collection = connStatusMap.values();
		for(ConnectionStatuts status : collection){
			status.clearBuffer();
		}
	}
	
	/**
	 * ������һ������������false����ʾ������û�������ˡ�
	 * 
	 * @return
	 */
	boolean tryNextCommandTuple(){
		if(sessionInitQueryQueue == null){
			return false;
		}else{
			if(sessionInitQueryQueue.size()>0){
				currentCommand = sessionInitQueryQueue.get(0);
				if(CommandMessageHandler.logger.isDebugEnabled()){
					QueryCommandPacket command = new QueryCommandPacket();
					command.init(currentCommand.getBuffer(),source);
					CommandMessageHandler.logger.debug(command);
				}
				return true;
			}
			return false;
		}
	}
	
	/**
	 * �жϷ��ص������Ƿ��ǵ�ǰ����Ľ�������
	 * ��ǰȫ�����Ӷ�ȫ�������Ժ����ʾ��ǰ������ȫ������
	 * @param conn
	 * @param buffer
	 * @return
	 */
	protected  CommandStatus checkResponseCompleted(Connection conn,byte[] buffer){
		boolean isCompleted = false;
		ConnectionStatuts connStatus = connStatusMap.get(conn);
		if(connStatus == null){
			CommandMessageHandler.logger.error("connection Status not Found, byffer="+StringUtil.dumpAsHex(buffer, buffer.length));
		}
		try{
			connStatus.buffers.add(buffer);
			isCompleted = connStatus.isCompleted(buffer);
			/**
			 * ����Ƕ�����ӵģ���Ҫ�����ݻ����������ȴ�����ȫ������Ժ󣬽����ݽ�����װ��Ȼ���͵��ͻ���
			 * {@link #CommandMessageHandler.mergeMessageToClient}
			 */
			
			if(isCompleted){
				//set last insert id to client connection;
				if(conn != source){
					if(connStatus.packetIndex == 0 && MysqlPacketBuffer.isOkPacket(buffer)){
						if(statment instanceof InsertStatement && currentCommand.isMain()){
							OkPacket packet = new OkPacket();
							packet.init(buffer,conn);
							if(packet.insertId>0){
								source.setLastInsertId(packet.insertId);
							}
						}
					}
				}
				
				boolean isAllCompleted = currentCommand.getCompletedCount().incrementAndGet() == connStatusMap.size();
				if(isAllCompleted){
					connStatus.isMerged = true;
				}
				if(isAllCompleted){
					if(CommandMessageHandler.logger.isDebugEnabled()){
						Packet packet = null;
						if(MysqlPacketBuffer.isErrorPacket(buffer)){
							packet = new ErrorPacket();
						}else if(MysqlPacketBuffer.isEofPacket(buffer)){
							packet = new EOFPacket();
						}else if(MysqlPacketBuffer.isOkPacket(buffer)){
							packet = new OkPacket();
						}
						packet.init(buffer,conn);
						CommandMessageHandler.logger.debug("returned Packet:"+packet);
					}
					return CommandStatus.AllCompleted;
					
				}else{
					return CommandStatus.ConnectionCompleted;
				}
			}else{
				return CommandStatus.ConnectionNotComplete;
			}
		}finally{
			connStatus.packetIndex ++;
		}
	}
	
	/**
	 * �Ƿ�append �ɹ�������ɹ����ʾ��ǰ�����ѻ�������Ҫ��������֤���������ѭ��
	 * ���������û�жѻ�������򷵻�false.
	 * ���򷵻�true�� ���ʾ��ֱ�ӷ�������
	 * @param commandInfo
	 * @param force ǿ��append �������Ϊtrue
	 * @return
	 */
	public synchronized  boolean appendCommand(CommandInfo commandInfo,boolean force){
		if(force){
			if(sessionInitQueryQueue == null){
				sessionInitQueryQueue = Collections.synchronizedList(new ArrayList<CommandInfo>());
			}
			if(!sessionInitQueryQueue.contains(commandInfo)){
				sessionInitQueryQueue.add(commandInfo);
			}
			return true;
		}else{
			if(sessionInitQueryQueue == null){
				return false;
			}else{
				if(sessionInitQueryQueue.size() ==0){
					return false;
				}
				if(!sessionInitQueryQueue.contains(commandInfo)){
					sessionInitQueryQueue.add(commandInfo);
				}
				return true;
			}
		}
	}
}