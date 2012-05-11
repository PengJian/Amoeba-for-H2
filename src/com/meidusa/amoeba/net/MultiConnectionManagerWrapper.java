package com.meidusa.amoeba.net;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.meidusa.amoeba.util.InitialisationException;

public class MultiConnectionManagerWrapper extends ConnectionManager{
	private static Logger log      = Logger.getLogger(MultiConnectionManagerWrapper.class);
	private AtomicLong counter = new AtomicLong();
	public ConnectionManager[] connMgrs;
	private String subManagerClassName;
	private int processors = Runtime.getRuntime().availableProcessors();
	{
		int managers = Integer.valueOf(System.getProperty("managers","0"));
		processors = processors+managers;
	}
	
	public void setSubManagerClassName(String className) {
		this.subManagerClassName = className;
	}

	public void setProcessors(int processors) {
		this.processors = processors;
	}

	
	
	public MultiConnectionManagerWrapper(ConnectionManager... connMgrs) throws IOException {
		super();
		this.connMgrs = connMgrs;
	}
	
	public MultiConnectionManagerWrapper()throws IOException{
		
	}
	
    public void postRegisterNetEventHandler(NetEventHandler handler, int key) {
    	if(connMgrs != null){
    		long count = counter.incrementAndGet();
    		connMgrs[(int)count% connMgrs.length].postRegisterNetEventHandler(handler, key);
    	}else{
    		super.postRegisterNetEventHandler(handler, key);
    	}
    }
    
    public void run(){
        if(connMgrs == null){
        	super.run();
        }
    }
    
    public void init() throws InitialisationException {
    	if(connMgrs == null || connMgrs.length<1 || connMgrs[0] == null){
    		if(subManagerClassName == null){
    			subManagerClassName = ConnectionManager.class.getName();
    		}
    		connMgrs = new ConnectionManager[processors];
    		for(int i=0;i<processors;i++){
    		try {
    			connMgrs[i] = (ConnectionManager)Class.forName(subManagerClassName).newInstance();
    			connMgrs[i].setName(this.getName()+"-"+i);
    			connMgrs[i].setIdleCheckTime(this.getIdleCheckTime());
    			connMgrs[i]._observers.addAll(this._observers);
				} catch (Exception e) {
					log.error("create sub manager error",e);
					e.printStackTrace();
					System.exit(-1);
				}
    		}
    	}
    	
    	Level level = log.getLevel();
        log.setLevel(Level.INFO);
        log.info(this.getName() + " LoopingThread willStart....");
        log.setLevel(level);
        if(connMgrs != null){
	        for(int i=0;i<connMgrs.length;i++){
	        	if(connMgrs[i] instanceof AuthingableConnectionManager){
	        		AuthingableConnectionManager aconnMgr = (AuthingableConnectionManager)connMgrs[i];
	        		//aconnMgr.setAuthenticator(ProxyRuntimeContext.getInstance().get)
	        	}
	        	if(!connMgrs[i].isAlive()){
	        		connMgrs[i].start();
	        		log.info(connMgrs[i].getName() + " connectionManager willStart....");
	        	}
	        	
	        }
        }
    }
    
    public synchronized void shutdown() {
    	if(connMgrs != null){
    		for(int i=0;i<connMgrs.length;i++){
    			connMgrs[i].shutdown();
    		}
    	}
    	super.shutdown();
    }
    
    public int getSize(){
    	int size = super.getSize();
    	if(connMgrs != null){
	    	for(ConnectionManager manager : connMgrs){
	    		size += manager.getSize();
	    	}
    	}
    	return size;
    }
}

