package com.meidusa.amoeba.monitor.io;

import com.meidusa.amoeba.monitor.MonitorConstant;
import com.meidusa.amoeba.net.io.PacketInputStream;

public class MonitorPacketInputStream extends PacketInputStream implements MonitorConstant{

	protected int decodeLength() {
		/**
		 * �ж�һ�����ǵ�ǰ�Ѿ���ȡ�����ݰ��������Ƿ�Ȱ�ͷ��,�����:����Լ����������ĳ���,���򷵻�-1
		 */
		if (_have < getHeaderSize()) {
			return -1;
		}

		//_buffer.rewind();
		
		/**
		 * mysql ���ݲ��֣���ͷ=�������ݰ�����
		 */
		int length = (_buffer.get(0) & 0xff)
					| ((_buffer.get(1) & 0xff) << 8)
					| ((_buffer.get(2) & 0xff) << 16)
					| ((_buffer.get(3) & 0xff) << 24);
		
		return length;
	}

	public int getHeaderSize() {
		return HEADER_SIZE;
	}
	
	protected byte[] readPacket(){
        byte[] msg = new byte[_length];
        int position = _buffer.position();
    	_buffer.position(0);
        _buffer.get(msg, 0, _length);
    	try{
    		_buffer.limit(_have);
    		
    		_buffer.compact();
    		_buffer.position(position - _length);
            _have -= _length;
            _length = this.decodeLength();
    	}catch(IllegalArgumentException e){
    		throw new IllegalArgumentException("old position="+_buffer.position()+", new position="+_length+",old limit="+_buffer.limit() +", have(new limit)="+_have,e);
    	}
        return msg;
    }

}
