package edu.wayne.cs.beam.monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;


import edu.wayne.cs.beam.display.*;


import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AppTimeService extends Service{
	 private boolean state = false;
	 
	 private int period=10000;
		//private AppTimeService appTimeService = new PtopaService();
	    private SystemEventReceiver receiver;
	    private Handler eventHandler = new Handler();
	    private static AppTimeActivity mainActivity;
	    
	    private FileWriter writer = null;
	    private String fileLocation = null; 

	    private Runnable eventPeriodicTask = new Runnable() {
	        public void run() {
	            if(receiver!=null)
	            	logEvent();
	            if(state)
	                eventHandler.postDelayed(eventPeriodicTask, period);
	           
	            	
	        }
	    };   
	    
	    //if the date changed, we need to modify writer location
	    private void logEvent()
	    {
	    	Calendar c = Calendar.getInstance();
	        String date = Integer.toString(c.get(Calendar.DAY_OF_MONTH))+ Integer.toString(c.get(Calendar.MONTH)+1)+Integer.toString(c.get(Calendar.YEAR));
	        if(!(fileLocation.equalsIgnoreCase(date)))
	        	{
	        		initWrite();
	        		receiver.reset(writer);
	        	}
	        receiver.writeLog();
	    }
	    
	    public boolean currentState()
	    {
	        return state;
	    }
	    
	    public void stopMonitor()
	    {        
	        state = false;
	    }
	    
	    @Override
		public void onCreate() {
	    	
			super.onCreate(); 
			//initReceiver();   
			   receiver = new SystemEventReceiver();
			   IntentFilter filter = new IntentFilter();
		        filter.addAction(Intent.ACTION_SCREEN_OFF);
		        filter.addAction(Intent.ACTION_SCREEN_ON);
		        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		        Log.i("AppTime:" , "before receiver");
		        if(receiver == null||filter == null)
		        {
		        	Log.i("AppTime:" , "can not creat receiver");
		        }
		        
		    
		        
		        	else this.registerReceiver(receiver, filter);
		        
		        startMonitor();
		}

		@Override
		public void onDestroy() {  
			 if(receiver != null)
		        {
		            receiver.writeLog();    
		        }
		        if(writer != null)
		        {
		            try{
		                writer.flush();
		                writer.close();
		            }catch(Exception ex){}
		        }
		        
		        if(!state){
		            unregisterReceiver(receiver);        
		        }
		        
	        eventHandler.removeCallbacks(eventPeriodicTask);
		    super.onDestroy();
		}

		@Override
		public void onLowMemory() {
			super.onLowMemory();
		}

		@Override
		  public int onStartCommand(Intent intent, int flags, int startId) {
		    //TODO do something useful
		    return Service.START_STICKY;
		  }
		
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
		
	
		 

		private void initReceiver()
	    {
	        if(receiver == null)
	        receiver = new SystemEventReceiver();
	/*        IntentFilter filter = new IntentFilter();
	        filter.addAction(Intent.ACTION_SCREEN_OFF);
	        filter.addAction(Intent.ACTION_SCREEN_ON);
	        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
	        Log.i("AppTime:" , "before receiver");
	        if(receiver == null||filter == null)
	        {
	        	Log.i("AppTime:" , "can not creat receiver");
	        }
	        
	    
	        
	        	else this.registerReceiver(receiver, filter);
	        	*/
	        	
	     
	    }    
		
		 private void initWrite()
		    {
			  if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				 try {
			 
		            File root = Environment.getExternalStorageDirectory();
		           // Log.i("AppTime: ", "start created data directory");
		            String appFilePath = root.getAbsolutePath()+"/BEAM/data";
		            File appFile = new File(appFilePath);
		           // if(appFile )
		            if(appFile.exists() == false) { 
		            	boolean a = appFile.mkdirs();
		            	if(!a)
		    	           Log.i("AppTime: ", "Not apptime data failed.");
		            }
		            //calendar month need add 1, it starts from 0
		            Calendar c = Calendar.getInstance();
		            String date = Integer.toString(c.get(Calendar.DAY_OF_MONTH))+ Integer.toString(c.get(Calendar.MONTH)+1)+Integer.toString(c.get(Calendar.YEAR));
		            fileLocation = date;
		            String fileName = date+".txt";
		            File file = new File(appFile, fileName);
		            if(!file.exists())
		        //    {
		        //        file.delete();
		        //    }            
		            file.createNewFile();
		         
		            if (root.canWrite()){
		                writer = new FileWriter(file,true);
		              /*  lastRecord = new Record();
		                lastRecord.setName(fileName);
		                lastRecord.setAddtime(new Timestamp(System.currentTimeMillis()));
		                lastRecord.setState(0);
		                rdao.insert(lastRecord);
		                */
		               
		            }
		            else
		            {
		                Log.i("AppTime: ", "Cannot write power data.");
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        } 
			  }
		    }
		 
		 public boolean startMonitor()
		    {
		        initWrite();
		       // initReceiver();
		        if(writer == null) return false;
		        if(receiver == null)
		        	Log.i("AppTime: ", "receiver is null");
		        else
		        receiver.reset(writer);
		       // ptopaService.reset(writer, period);
		        eventHandler.postDelayed(eventPeriodicTask, period);
		        state = true;   
		           
		        
		        return true;
		    }
		    
		 public boolean alreadyStartMonitor()
		    {
		       if(state == true)   
		           return true;
		       else
		    	   return startMonitor();
		    }
	/*	    private void stopMonitor()
		    {
		        unregisterReceiver(receiver);
		        receiver.writeLog();
		        if(writer != null){
		            try{writer.close();}catch(Exception ex){}
		        }
		        eventHandler.removeCallbacks(eventPeriodicTask);
		        
		     /*   if(lastRecord != null)
		        {
		            lastRecord.setState(1);
		            rdao.update(lastRecord);
		        }
		        */
		       // ptopaService.stopMonitor();
		 //       state = false;
		 //   }*/

}
