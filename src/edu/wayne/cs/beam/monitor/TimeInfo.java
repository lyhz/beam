package edu.wayne.cs.beam.monitor;

import java.io.FileWriter;

import android.text.format.Time;
import android.util.Log;

import com.android.internal.os.PkgUsageStats;

public class TimeInfo {
	
	public PkgUsageStats stats;
	public long time;
	public Time t;
	    
	    public void write(FileWriter writer)
	    {
	        try{
	        	if(stats.launchCount!=0){
	        		writer.write("App: " +time + ", "  + t.hour+":"+t.minute+":"+t.second+","+ stats.packageName +"," + stats.launchCount +"," + stats.usageTime);
	        		writer.write("\r\n");
	        	}
	        	
	        }catch(Exception ex){}
	    }
	

}
