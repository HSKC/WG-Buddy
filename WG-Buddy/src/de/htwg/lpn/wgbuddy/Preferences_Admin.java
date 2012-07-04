package de.htwg.lpn.wgbuddy;

import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Preferences_Admin extends Activity 
{
	    private SharedPreferences settings;

		@Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.preferences_admin);
	        
	        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	        Utilities.checkByPass(this, settings);
	    }
}
