package edu.wayne.cs.beam.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver  extends BroadcastReceiver {
	
	 static final String ACTION = "android.intent.action.BOOT_COMPLETED";  

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if (intent.getAction().equals(ACTION)||intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {  
			// Log.i("AppTime: ","receive boot completed info");
	            Intent serviceIntent = new Intent(context, AppTimeService.class);  
	            // start Service  
	            context.startService(serviceIntent);  
	        }  
		
	}

}
