/**
 * <pre>
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
package com.meidusa.amoeba.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * ������Ϊ�߳��ṩ��ѭ���Ļ������ܣ��ܹ�����һ���򵥵�ѭ���� ����ص���ÿһ��ͨ��ѭ�����¼������߳̿������׵ذ��Ŵ��������¼����С�
 */
public class LoopingThread extends Thread {

    private static Logger log      = Logger.getLogger(LoopingThread.class);

    protected boolean     _running = true;

    public LoopingThread(){
    }

    /**
     * ָ��һ�����������
     */
    public LoopingThread(String name){
        super(name);
    }

    /**
     * ����رո��̣߳�����˴����󲢷����̷߳���������Ҫ������ص����顣
     */
    public synchronized void shutdown() {
        _running = false;

        // only kick the thread if it's not requesting it's own shutdown
        if (this != Thread.currentThread()) {
            kick();
        }
    }

    /**
     * ѭ��
     */
    public void run() {
    	
    	Level level = log.getLevel();
        log.setLevel(Level.INFO);
        log.info(this.getName() + " LoopingThread willStart....");
        log.setLevel(level);
        
        try {
            willStart();

            while (isRunning()) {
                try {
                    iterate();
                } catch (Exception e) {
                    handleIterateFailure(e);
                }
            }
        } finally {
            didShutdown();
        }
    }

    /**
     * ����߳��Ƿ�������״̬������÷�������false����ʱ�߳��������iterate���ã������˳�ѭ���� ���������Ϊѭ���̵߳�һ���� {@link #run}
     */
    public synchronized boolean isRunning() {
        return _running;
    }

    /**
     * ���߳����˳���ʱ����Ҫ�����ڲ��������߽�����������
     */
    protected void kick() {
        // nothing doing by default
    }

    /**
     * �߳��ڿ�ʼִ�е�ʱ�򣬿������������������һЩ��ʼ���Ķ���
     */
    protected void willStart() {
    }

    protected void iterate() {
        throw new RuntimeException("Derived class must implement iterate().");
    }

    protected void handleIterateFailure(Exception e) {
        // log the exception

        // and shut the thread down
        log.error("error:", e);
        shutdown();
    }

    /**
     * ���shutdown�����Ժ󡣼����˳������̵߳����С�
     */
    protected void didShutdown() {
    }

}
