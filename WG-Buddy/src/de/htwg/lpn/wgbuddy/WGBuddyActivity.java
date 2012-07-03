package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WGBuddyActivity extends Activity 
{
	public static final String PREFS_NAME = "WGBuddyPreferences";
	public static final String WEBSERVER = "http://wgbuddy.domoprojekt.de/";
	
	private Button shoppinglist;
	private Button taskdistributor;
	private Button preferences;
	private Button invite;
	private Button messenger;
	private Button logout;
	private TextView heading;
	
	private SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        
        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pref_webserver", "http://wgbuddy.domoprojekt.de/");
        editor.commit();
        
    	super.onCreate(savedInstanceState);
        
        // Nicht eingeloggt.
        if(!settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password"))
        {
        	//Applikation das erste mal gestartet oder nicht Konfiguriert. Preferences aufrufen
        	Intent intent = new Intent(WGBuddyActivity.this,Preferences_User.class);
			startActivity(intent);
			return;        	
        }
        
        User user = new User(settings);
        ArrayList<HashMap<String, String>> userList = user.get("?id=" + settings.getString("user_id", ""));
        
        // Keiner WG zugewiesen
        if(userList.get(0).get("wgId").equals("0"))
        {
        	Intent intent = new Intent(WGBuddyActivity.this,Preferences_WG.class);
			startActivity(intent);
			return;  
        }
        
        if(!settings.contains("wg_id") || !settings.contains("wg_name") || !settings.contains("wg_password"))
        {
        	WG wg = new WG(settings);
        	String string = userList.get(0).get("wgId");
        	ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + string);
        	
        	editor.putString("wg_id", wgList.get(0).get("id"));
        	editor.putString("wg_name", wgList.get(0).get("name"));
        	editor.putString("wg_password", wgList.get(0).get("password"));
        	editor.commit();
        }
        	
    	//Applikation schon initialisiert. Lade Einstellungen
    	setContentView(R.layout.main);
    	
        shoppinglist = (Button) findViewById(R.id.main_shoppingListButton);
        taskdistributor = (Button) findViewById(R.id.main_taskDistributorButton);
        preferences = (Button) findViewById(R.id.main_preferencesButton);
        invite = (Button) findViewById(R.id.main_addButton);
        messenger = (Button) findViewById(R.id.main_messengerButton);
        logout = (Button) findViewById(R.id.main_logoutButton);
        
        heading = (TextView) findViewById(R.id.main_headingText);
        
        heading.setText(settings.getString("user_name", "") + " @ " + settings.getString("wg_name", ""));
        
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
					Intent intent = new Intent(WGBuddyActivity.this,Preferences.class);
					startActivity(intent);
				}
    		}
        );
        
        messenger.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(WGBuddyActivity.this,Messenger.class);
					startActivity(intent);
				}
    		}
        );
        
        invite.setOnClickListener
        (
    		new OnClickListener()
    		{
				@Override
				public void onClick(View v) 
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(WGBuddyActivity.this);
					
					final EditText editText = new EditText(WGBuddyActivity.this);
					editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
					editText.setHint("E-Mail");
					
					builder.setMessage("Bitte gebe die E-Mail Adresse ein um eine Person einzuladen.");
					builder.setCancelable(true);
					builder.setView(editText);
					builder.setPositiveButton("Einladen", new DialogInterface.OnClickListener() 
					{
			           public void onClick(DialogInterface dialog, int id) 
			           {
			        	   User user = new User(settings);
			        	   user.sendInvite(editText.getText().toString());
			        	   
			           }
					});
					builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
						}
					});
					       
					AlertDialog alert = builder.create();
					alert.show();
				}
    		}
		);
        
        logout.setOnClickListener
        (
    		new OnClickListener()
    		{
				@Override
				public void onClick(View v) 
				{
					SharedPreferences.Editor editor = settings.edit();      		    
        		    editor.clear();
        		    editor.commit();
        		    
					Intent intent = new Intent(WGBuddyActivity.this,WGBuddyActivity.class);
					startActivity(intent);
				}
    		}
		);   
    }
}
