package edu.wayne.cs.beam.display;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




import edu.wayne.cs.beam.R;
import edu.wayne.cs.beam.monitor.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AppTimeActivity extends Activity implements OnClickListener {
	
   // private int period=10000;
	private AppTimeService appTimeService = new AppTimeService();
   // private SystemEventReceiver receiver = null;
   // private Handler eventHandler = new Handler();
    private boolean state = false;
    private FileWriter writer = null;
    private String fileLocation = null; 

  /*  private Runnable eventPeriodicTask = new Runnable() {
        public void run() {
            logEvent();
            if(state)
                eventHandler.postDelayed(eventPeriodicTask, period);
           
            	
        }
    };  */ 
    
    //if the date changed, we need to modify writer location
/*    private void logEvent()
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
    */
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_time);
		 initViewListeners();
		/* try{
	        	ptopaService.setMainActivity(this);
	        }catch(Exception ex)
	        {
	        	//TODO change service states
	        }
	        */
	}

	 private void initViewListeners()
	    {
	        Button exit = (Button)findViewById(R.id.exitButton);
	        Button start = (Button)findViewById(R.id.startButton);
	        
	        exit.setOnClickListener(this);
	        start.setOnClickListener(this);
	    }
	 
	 @Override
	    protected void onDestroy() {
	/*        if(state){
	            unregisterReceiver(receiver);        
	        }
	        
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
	        
	   //     if(lastRecord != null){
	   //         lastRecord.setState(1);
	   //         rdao.update(lastRecord);
	  //      }        
	        //close database connection
	  //      SqliteHelper.getInstance().close();
	   
	   */
	        super.onDestroy();
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

/*	private void initReceiver()
    {
        if(receiver == null)
        receiver = new SystemEventReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        
        registerReceiver(receiver, filter);
    }  
    */  
	
/*	 private void initWrite()
	    {
		  if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			 try {
		 
	            File root = Environment.getExternalStorageDirectory();
	           // Log.i("AppTime: ", "start created data directory");
	            String appFilePath = root.getAbsolutePath()+"/AppTime/data";
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
	               
	  /*          }
	            else
	            {
	                Log.i("AppTime: ", "Cannot write power data.");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
		  }
	    }
	 */
	
	 private boolean startMonitor()
	    {
	      //  initWrite();
	      //  initReceiver();
	      //  if(writer == null) return false;
	      //  receiver.reset(writer);
	       // ptopaService.reset(writer, period);
	      //  eventHandler.postDelayed(eventPeriodicTask, period);
		 
		 
		    appTimeService.alreadyStartMonitor();
	        state = true;        
	        
	        
	        
	        return true;
	    }
	    
	    private void stopMonitor()
	    {
	      //  unregisterReceiver(receiver);
	      //  receiver.writeLog();
	      //  if(writer != null){
	      //      try{writer.close();}catch(Exception ex){}
	      //  }
	      //  eventHandler.removeCallbacks(eventPeriodicTask);
	        
	     /*   if(lastRecord != null)
	        {
	            lastRecord.setState(1);
	            rdao.update(lastRecord);
	        }
	        */
	       // appTimeService.stopMonitor();
	        state = false;
	    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		 switch(view.getId())
	        {
	            case R.id.exitButton:
	                finish();
	                System.exit(0);
	                break;
	            case R.id.startButton:
	                Button start = (Button)view;
	                if(this.state == false){
	               // if(appTimeService.currentState() == false)//not running
	              //  {
	                    if(startMonitor() == true)
	                        start.setText(R.string.stopButton);
	              //      else
	              //      {
	                        //TODO show alert
	              //      }
	                }
	                else
	                {
	                    stopMonitor();
	                    start.setText(R.string.startButton);
	                }
	                break;
	            default:
	                break;
	        }
	    }
		
	}

