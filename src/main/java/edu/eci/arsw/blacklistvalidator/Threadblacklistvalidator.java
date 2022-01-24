package main.java.edu.eci.arsw.blacklistvalidator;

import main.java.edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.lang.Math;
import java.util.LinkedList;
public class Threadblacklistvalidator extends Thread{
	private int checkedListsCount=0;
	private int ocurrencesCount =0;
	private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
	private String ipaddress;
	private int min;
	private int max;
	private HostBlacklistsDataSourceFacade skds;
	

	Threadblacklistvalidator(String ipaddress,int min,int max,HostBlacklistsDataSourceFacade skds){
		this.min = min;
		this.max=max;
		this.ipaddress = ipaddress;
		this.skds = skds;
	}
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
	public HostBlacklistsDataSourceFacade getSkds() {
		return skds;
	}
	public int getCheckedListsCount() {
		return checkedListsCount;
	}
	public LinkedList<Integer> getBlackListOcurrences() {
		return blackListOcurrences;
	}
	public int getOcurrencesCount() {
		return ocurrencesCount;
	}
	
	public void run(){
		for(int i=min;i<max;i++) {
			checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
		}
	}
	
	
}
