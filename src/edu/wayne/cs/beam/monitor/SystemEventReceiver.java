package edu.wayne.cs.beam.monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.android.internal.app.IUsageStats;
import com.android.internal.os.PkgUsageStats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;


public class SystemEventReceiver extends BroadcastReceiver {
	private FileWriter writer;
    private Vector<Event> events = new Vector<Event>();
    private ArrayList<TimeInfo> timeinfo = new ArrayList<TimeInfo>();
    private IUsageStats mUsageStatsService;
   // private PackageManager mPm;
    
    public void reset(FileWriter fw)
    {
        writer = fw;
        events.clear();
        timeinfo.clear();
    }
    
    public Event addEvent(Intent intent, String intType, long time, Time ti, String info)
    {
        Event e = new Event();
        e.name = intType;
        e.time = time;
        e.t = ti;
        e.extraInfo = info;
        events.add(e);            
        return e;
    }   
    
    public boolean addAppInfo(long time, Time ti){
    	 PkgUsageStats[] stats;
         try {
             stats = mUsageStatsService.getAllPkgUsageStats();
         } catch (RemoteException e) {
             Log.e("AppTime", "Failed initializing usage stats service");
             return false;
         }
        if (stats == null) {
            return false;
        }
        for (PkgUsageStats ps : stats) {
        TimeInfo e = new TimeInfo();
        e.stats = ps;
        e.time = time;
        e.t = ti;
        timeinfo.add(e);
        }
        return true;
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		long time = SystemClock.elapsedRealtime();
		Time ti = new Time();
		ti.setToNow();
		
		 String intType = intent.getAction();
	        Log.i("AppTime", "received event : " + intType);
	        
	        mUsageStatsService = IUsageStats.Stub.asInterface(ServiceManager.getService("usagestats"));
	        if (mUsageStatsService == null) {
	            Log.e("AppTime", "Failed to retrieve usagestats service");
	            return;
	        }
	       // mPm = context.getPackageManager();
	        
	       if(intType.equals(Intent.ACTION_SCREEN_OFF)){
	    	   addEvent(intent, intType, time, ti, "" );	
	    	   addAppInfo(time, ti);
	       }
	       else if(intType.equals(Intent.ACTION_SCREEN_ON)){  
	    	   addEvent(intent, intType, time, ti, "");	
	       }
	       
	       if(intType.equals(Intent.ACTION_BATTERY_CHANGED)) {
	           int level = intent.getIntExtra("level", -1);
	           int scale = intent.getIntExtra("scale", -1);
	           int status = intent.getIntExtra("status", -1); 
	           String info = "";
	           switch(status){
	           case BatteryManager.BATTERY_STATUS_CHARGING: 
	        	   info = info+"Charging ,"; 
	        	   break;
	           case BatteryManager.BATTERY_STATUS_DISCHARGING:
	           case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
	        	   info = info+"Discharging ,";
	        	   break;
	           }
	           info = info + (level * 100 / scale) + "%";
	           
	           addEvent(intent, intType, time, ti, info);
	         }
	    	   
		
	}
	
	 public void writeLog()
	    {
	        if(writer == null)
	            return;
	        
	        try{
	            List<Event> evs = events.subList(0, events.size());
	            Log.i("AppTime: ", "events amount : " + evs.size() );
	            for (Iterator<Event> it = evs.iterator(); it.hasNext();)
	            {
	                Event  e = it.next();
	                e.write(writer);
	            }
	            Log.i("AppTime: ", "AppInfo amount : " + timeinfo.size() );
	            for (Iterator<TimeInfo> it = timeinfo.iterator(); it.hasNext();)
	            {
	                TimeInfo  e = it.next();
	                e.write(writer);
	            }
	            writer.flush();
	            evs.clear();
	            timeinfo.clear();
	            Log.i("AppTime: ", "done write event and info");
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
	    }

}
