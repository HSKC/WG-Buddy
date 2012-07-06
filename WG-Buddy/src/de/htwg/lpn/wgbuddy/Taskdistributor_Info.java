package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;

import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	    @SuppressWarnings("unchecked")
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.basicrefresh_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent intent;
		switch (item.getItemId()) 
        {   
	        case R.id.refresh:
	        	finish();
	        	startActivity(getIntent());
	        	return true;
		
	        case R.id.about:
	        	Dialogs.getAboutDialog(Taskdistributor_Info.this, settings);
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(Taskdistributor_Info.this, WGBuddyActivity.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
}
