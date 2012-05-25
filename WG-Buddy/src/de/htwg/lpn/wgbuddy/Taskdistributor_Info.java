package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Taskdistributor_Info extends Activity
{
	private TextView text;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.taskdistributor_info);
	        
	        Bundle bundle = this.getIntent().getExtras();
	        ArrayList<String> users = (ArrayList<String>) bundle.get("users");
	        long[] ids = (long[]) bundle.get("checked");
	        
	        text = (TextView) findViewById(R.id.taskinfo_Userview);
	        
	        String mytext = users.toString();
	        
	        for (long l : ids) 
	        {
	        	mytext+= l;
			}
	        text.setText(mytext);
	        
	    }
}
