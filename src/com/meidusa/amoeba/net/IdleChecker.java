package com.meidusa.amoeba.net;

public interface IdleChecker {
	
	/**
	 * ����Ƿ��Լ�����idle���������true������Ҫ�ر�.
	 * @param now
	 * @return
	 */
	public boolean checkIdle(long now);
}
