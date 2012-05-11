package com.meidusa.amoeba.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * �������ȼ��ĳ���رչ���,��Ҫ�ڷ������رյ�ʱ����������Դ�ͷŵĲ�����������ʵ�� {@link Shutdowner} �ӿ�,
 * ������Ҫ���� {@link #addShutdowner(Shutdowner)} ע�ᵽ������ӳ�������
 * 
 * @author Struct
 *
 */
public class PriorityShutdownHook extends Thread{
	private static Logger logger = Logger.getLogger(PriorityShutdownHook.class);
	
	private static PriorityShutdownHook instance = new PriorityShutdownHook();
	
	static{
		Runtime.getRuntime().addShutdownHook(instance);
	}
	
	private List<Shutdowner> shutdowners = Collections.synchronizedList(new ArrayList<Shutdowner>());
	
	
	private PriorityShutdownHook(){}
	
	/**
	 * register a shutdowner 
	 * @param shutdowner 
	 */
	public static void addShutdowner(Shutdowner shutdowner){
		synchronized(instance){
			instance.shutdowners.add(shutdowner);
		}
	}
	
	/**
	 * remove a shutdowner
	 * @param shutdowner
	 */
	public static void removeShutdowner(Shutdowner shutdowner){
		synchronized(instance){
			instance.shutdowners.remove(shutdowner);
		}
	}
	
	public synchronized void run(){
		List<Shutdowner> shutDownTmp = new ArrayList<Shutdowner>();
		shutDownTmp.addAll(shutdowners);
		
		Collections.sort(shutDownTmp, new Comparator<Shutdowner>(){
			public int compare(final Shutdowner o1, final Shutdowner o2) {
				return  o2.getShutdownPriority() - o1.getShutdownPriority();
			}
		});
		
		for(Shutdowner shutdowner : shutDownTmp){
			try{
				shutdowner.shutdown();
				if(logger.isInfoEnabled()){
					logger.info("shutdowner :" + shutdowner +" shutdown completed!");
				}
			}catch(Exception e){
				logger.error("shutdowner invoke shutdown method error",e);
			}
		}
	}
}
