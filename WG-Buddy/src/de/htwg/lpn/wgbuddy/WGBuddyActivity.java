package de.htwg.lpn.wgbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WGBuddyActivity extends Activity 
{
	public static final String PREFS_NAME = "WGBuddyPreferences";
	
	private Button shoppinglist;
	private Button taskdistributor;
	private Button preferences;
	private TextView heading;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.contains("initiated"))
        {
        	//Applikation schon initialisiert. Lade Einstellungen
        	setContentView(R.layout.main);
        	
        	
        	
            shoppinglist = (Button) findViewById(R.id.main_shoppingListButton);
            taskdistributor = (Button) findViewById(R.id.main_taskDistributorButton);
            preferences = (Button) findViewById(R.id.main_preferencesButton);
            heading = (TextView) findViewById(R.id.main_headingText);
            
            heading.setText(settings.getString("username", "")+" @ "+settings.getString("wgname", ""));
            
            shoppinglist.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(WGBuddyActivity.this,ShoppingList.class);
    					startActivity(intent);
    				}
        		}
            );
            
            taskdistributor.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(WGBuddyActivity.this,TaskDistributor.class);
    					startActivity(intent);
    				}
        		}
            );
            
            preferences.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(WGBuddyActivity.this,Preferences_WG.class);
    					startActivity(intent);
    				}
        		}
            );
            
        }
        else
        {
        	//Applikation das erste mal gestartet oder nicht Konfiguriert. Preferences aufrufen
        	Intent intent = new Intent(WGBuddyActivity.this,Preferences_WG.class);
			startActivity(intent);
			return;
        }
        

    }
}