package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;

import android.app.PendingIntent;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.os.StrictMode;

import android.text.method.SingleLineTransformationMethod;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WGBuddyActivity extends Activity 
{
	public static final String PREFS_NAME = "WGBuddyPreferences";
	public static final String WEBSERVER = "http://wgbuddy.domoprojekt.de/";
	public static boolean usepush = true;
	
	private Button shoppinglist;
	private Button taskdistributor;
	private Button messenger;
	private Button preferences;
	private Button information;
	private Button invite;
	private Button logout;
	private TextView heading;
	
	private SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        
        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pref_webserver", "http://wgbuddy.domoprojekt.de/");
        editor.commit();
      
    	super.onCreate(savedInstanceState);
        
        // Nicht eingeloggt.
        if(!settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password"))
        {
        	//Applikation das erste mal gestartet oder nicht konfiguriert. Login aufrufen
        	Intent intent = new Intent(WGBuddyActivity.this,Login_User.class);
			startActivity(intent);
			return;        	
        }
        
        // Konto freigeschalten?
        if(!settings.contains("user_status"))
        {
        	Intent intent = new Intent(WGBuddyActivity.this, Activate_User.class);
			startActivity(intent);
        }
        	
        
        User user = new User(settings);
        ArrayList<HashMap<String, String>> userList = user.get("?id=" + settings.getString("user_id", ""));
        
        if(userList.size() == 0)
        {
        	SharedPreferences.Editor e = settings.edit();      		    
		    e.clear();
		    e.commit();
		    
		    Intent intent = new Intent(WGBuddyActivity.this,Login_User.class);
			startActivity(intent);
			return;   
        }
        
        // Keiner WG zugewiesen
        if(userList.get(0).get("wgId").equals("0"))
        {
        	Intent intent = new Intent(WGBuddyActivity.this,Login_WG.class);
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
        
    	if( ((! settings.contains("registrationKey")) && (! settings.contains("registrationKeydate"))) 
    			||
    				(settings.contains("registrationKeydate") 
    						&& 
    				(new Date(settings.getLong("registrationKeydate", 0)+86400000).getTime() < new Date().getTime())))
    	{ 
  	        //Registrieren bei Google und Registrierungsid anfordern
  			Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
  			intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
  			intent.putExtra("sender", "wgbuddy2012@googlemail.com");
  			System.out.println("sended");
  			getApplicationContext().startService(intent);
    	}
    	else
    	{	
          	System.out.println(settings.getString("registrationKey", ""));
          	
    	}
        	
    	//Applikation schon initialisiert. Lade Einstellungen
    	setContentView(R.layout.main);
    	
        shoppinglist = (Button) findViewById(R.id.main_shoppingListButton);
        taskdistributor = (Button) findViewById(R.id.main_taskDistributorButton);        
        messenger = (Button) findViewById(R.id.main_messengerButton);
        preferences = (Button) findViewById(R.id.main_preferencesButton);
        information = (Button) findViewById(R.id.main_informationButton);        
        invite = (Button) findViewById(R.id.main_addButton);
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
        
        information.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(WGBuddyActivity.this,Information.class);
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.about_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
        {        	
	        case R.id.about:
	        	Dialogs.getAboutDialog(WGBuddyActivity.this, settings);
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
}
