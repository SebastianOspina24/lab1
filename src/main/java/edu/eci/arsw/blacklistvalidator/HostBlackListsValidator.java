/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.edu.eci.arsw.blacklistvalidator;

import main.java.edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {


    private static final int BLACK_LIST_ALARM_COUNT=5;
     private int ocurrencesCount=0;
     private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
     private int checkedListsCount=0;
	
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    public List<Integer> checkHost(String ipaddress,int cores){
    	long start = System.currentTimeMillis();
    	HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
    	int serverAmount = skds.getRegisteredServersCount();
    	int groups = (int)Math.ceil((serverAmount/ cores));
    	Threadblacklistvalidator[] threads = new Threadblacklistvalidator[cores];
        for(int i =0;i<cores;i++) {
        	threads[i] = new Threadblacklistvalidator(ipaddress,i*groups,((i+1)*groups)>serverAmount?serverAmount:(i+1)*groups,skds);
        }
        for(int i =0;i<cores;i++) {
        	threads[i].start();
        }
        while(checkedListsCount<serverAmount  && ocurrencesCount<BLACK_LIST_ALARM_COUNT) {
        	ocurrencesCount=0;
        	checkedListsCount=0;
        	for(int i =0;i<cores;i++) {
        		ocurrencesCount += threads[i].getOcurrencesCount();
        		checkedListsCount += threads[i].getCheckedListsCount();
            }
        }
        for(int i =0;i<cores;i++) {
        	blackListOcurrences.addAll(threads[i].getBlackListOcurrences());
        }
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        long fin = System.currentTimeMillis();
        System.out.println("Hilos: "+cores+"   Tiempo duracion: "+(-start+fin));
        return blackListOcurrences;
    }
    
    public int getocurrencesCount() {
    	return ocurrencesCount;
    }

	public int getcheckedlistCount() {
		return checkedListsCount;
	}
	
	public List<Integer> getblackListOcurrences(){
		 return blackListOcurrences;
	 }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}


