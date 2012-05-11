package com.meidusa.amoeba.util;

import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.log4j.Logger;

public class MappedByteBufferUtil {
	private static Logger logger = Logger.getLogger(MappedByteBufferUtil.class);
	/**
	 * MappedByteBufferֻ��ͨ������FileChannel��map()ȡ��, SUN�ṩ��map()ȴû���ṩunmap().
	 * ��Ϊ��File.delete()ʱ���᷵��false,�����޷�ɾ����ӳ������ļ��� ��˸÷���ֻ��һ�ֽ�� ȡ��ӳ�� �ķ�����
	 * 
	 * @param buffer
	 */
	private static Method GetCleanerMethod;
	private static Method cleanMethod;
	static {
		try {
			if(GetCleanerMethod == null){
				
				Method getCleanerMethod = Class.forName("java.nio.DirectByteBuffer").getMethod(
						"cleaner", new Class[0]);
				getCleanerMethod.setAccessible(true);
				GetCleanerMethod = getCleanerMethod;
				cleanMethod = Class.forName("sun.misc.Cleaner").getMethod("clean", new Class[0]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void unmap(final MappedByteBuffer buffer) {
		if (buffer == null) {
			return;
		}
		synchronized (buffer) {
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					try {
						//sun.misc.Cleaner
						Object cleaner = GetCleanerMethod.invoke(buffer,
								new Object[0]);
						cleanMethod.invoke(cleaner, new Object[0]);
					} catch (Exception e) {
						logger.error("unmap  MappedByteBuffer error",e);
					}
					return null;
				}
			});
		}
	}
}
