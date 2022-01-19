package main.java.edu.eci.arsw.blacklistvalidator;

import main.java.edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.lang.Math;
public class Threadblacklistvalidator extends Thread{
	
	private int threads;
	private HostBlackListsValidator[] searchThreads;
	private HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
	Threadblacklistvalidator(){
		
	}
	
	Threadblacklistvalidator(int threads,String ipaddress){
		this.threads = threads;
		for(int i=0;i<threads;i++)searchThreads[i]=new HostBlackListsValidator(ipaddress,i* Math.ceil((skds.getRegisteredServersCount()/threads)),max);
	}
	
	public void run(){
		
	}
	
}
