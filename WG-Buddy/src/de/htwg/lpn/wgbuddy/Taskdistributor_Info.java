package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;

import de.htwg.lpn.model.Utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Taskdistributor_Info extends Activity
{
	private TextView text;
	private SharedPreferences settings;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.taskdistributor_info);
	        
	        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	        Utilities.checkByPass(this, settings);
	        
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
