package de.htwg.lpn.wgbuddy;

import de.htwg.lpn.model.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Preferences extends Activity 
{
	private Button user;
	private Button wg;
	private Button admin;
	private SharedPreferences settings;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.preferences);
	    	
	    	settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	        Utilities.checkByPass(this, settings);
	    	
	    	user = (Button) findViewById(R.id.mainpref_userButton);
	    	wg = (Button) findViewById(R.id.mainpref_wgButton);
	    	admin = (Button) findViewById(R.id.mainpref_adminButton);
	    	
	    	user.setOnClickListener(new OnClickListener() 
	    	{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(Preferences.this,Login_User.class);
					startActivity(intent);
				}
			});
	    	
	    	wg.setOnClickListener(new OnClickListener() 
	    	{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(Preferences.this,Login_WG.class);
					startActivity(intent);
				}
			});
	    	
	    	admin.setOnClickListener(new OnClickListener() 
	    	{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(Preferences.this,Preferences_Admin.class);
					startActivity(intent);
				}
			});
	    	
	    }
}
