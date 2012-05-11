/**
 * <pre>
 * copy right meidusa.com
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
 * </pre>
 */
package com.meidusa.amoeba.net.packet;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.meidusa.amoeba.net.Connection;

/**
 * @author struct
 */
public abstract class AbstractPacket<T extends AbstractPacketBuffer> implements Packet {
    public void init(byte[] buffer, Connection conn) {
        T packetBuffer = constractorBuffer(buffer);
        packetBuffer.init(conn);
        init(packetBuffer);
        afterRead(packetBuffer);
    }

    /**
	 * �������ݰ�(������ͷ+��������,�������ͷ�Ժ�Ӧ�ý�Buffer��postion���õ�������)
	 */
	protected void init(T buffer){
		readHead(buffer);
		readBody(buffer);
	}
	
	protected void readHead(T buffer){
		
	}

	protected void readBody(T buffer){
		
	}
	
    /**
     * �����ʼ���Ժ�
     */
    protected void afterRead(T buffer) {
    }

    public ByteBuffer toByteBuffer(Connection conn) {
        try {
            int bufferSize = calculatePacketSize();
            T packetBuffer = constractorBuffer(bufferSize);
            packetBuffer.init(conn);
            return toBuffer(packetBuffer).toByteBuffer();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private T constractorBuffer(int bufferSize) {
        T buffer = null;
        try {
            Constructor<T> constractor = getPacketBufferClass().getConstructor(int.class);
            buffer = constractor.newInstance(bufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * <pre>
     *  �÷���������{@link #write2Buffer(PacketBuffer)} д�뵽ָ����buffer�� 
     *  ���ҵ�����{@link #afterPacketWritten(PacketBuffer)}
     * </pre>
     */
    private T toBuffer(T buffer) throws UnsupportedEncodingException {
        write2Buffer(buffer);
        afterPacketWritten(buffer);
        return buffer;
    }

    /**
     * ����ͷ����Ϣ��װ
     */
    protected void write2Buffer(T buffer) throws UnsupportedEncodingException{
    	writeHead(buffer);
    	writeBody(buffer);
	}

    protected void writeBody(T buffer) throws UnsupportedEncodingException {
    	
    }
    protected void writeHead(T buffer){
    	
    }

    /**
     * <pre>
     * д��֮��һ����Ҫ�������������buffer��ָ��λ��ָ��ĩβ����һ��λ�ã����ܳ���λ�ã���
     * ���һ���Ǽ������ݰ��ܳ���,����������Ҫ���ݰ�д�������ɵ�����
     * </pre>
     */
    protected abstract void afterPacketWritten(T buffer);

    /**
     * ����packet�Ĵ�С�������̫���˷��ڴ棬�����̫С��Ӱ������
     */
    protected abstract int calculatePacketSize();

    private T constractorBuffer(byte[] buffer) {
        T packetbuffer = null;
        try {
            Constructor<T> constractor = getPacketBufferClass().getConstructor(byte[].class);
            packetbuffer = constractor.newInstance(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packetbuffer;
    }

    protected abstract Class<T> getPacketBufferClass();

    public String toString() {
		return ToStringBuilder.reflectionToString(this);
    }

}
