package edu.wayne.cs.beam.monitor;

import java.io.FileWriter;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.format.Time;


public class Event {
	    public String name;
	    public long time;
	    public Time t;
	    public String extraInfo = "";

	public void write(FileWriter writer)
    {
        try{
            writer.write("EVENT: " + time + "," + t.hour+":"+t.minute+":"+t.second+","+ name+","+extraInfo);
            writer.write("\r\n");
        }catch(Exception ex){}
    }
}