package de.htwg.lpn.wgbuddy;

import de.htwg.lpn.model.Utilities;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Preferences_User extends Activity 
{
	    private SharedPreferences settings;

		@Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.preferences_user);
	        
	        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	        Utilities.checkByPass(this, settings);
	    }
}
