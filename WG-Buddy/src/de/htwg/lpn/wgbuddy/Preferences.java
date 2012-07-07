package de.htwg.lpn.wgbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;


public class Preferences extends Activity 
{
    private SharedPreferences settings;
	private Button changePasswordButton;
	private Button leaveWGButton;	
	private Button changeAdminButton;
	private Button changeWGPasswordButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);  
        
        leaveWGButton = (Button) findViewById(R.id.preferences_leaveButton);
        changePasswordButton = (Button) findViewById(R.id.preferences_changePasswordButton);
        changeWGPasswordButton = (Button) findViewById(R.id.preferences_changeWGPasswordButton);
        changeAdminButton = (Button) findViewById(R.id.preferences_changeAdminButton);        
        
        settings = getSharedPreferences(Main.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        String wgAdminId = Utilities.getWGAdminId(settings);
        String userId = settings.getString("user_id", "");
        
        if(wgAdminId.compareTo(userId) != 0)
        {
        	changeWGPasswordButton.setVisibility(View.GONE);
        	changeAdminButton.setVisibility(View.GONE);
        }
        
        changePasswordButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Dialogs.getChangePasswordDialog(Preferences.this, settings);
			}
		});
        
        leaveWGButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{					
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setMessage("Sind Sie sicher, dass Sie die WG verlassen möchten? Sie werden anschließend automatisch ausgeloggt.");
				builder.setCancelable(true);
				
				builder.setPositiveButton("WG verlassen", new DialogInterface.OnClickListener() 
				{
		           public void onClick(DialogInterface dialog, int id) 
		           {			        	  			        	   
		        	  User user = new User(settings);
		        	  
		        	  if(Utilities.getWGAdminId(settings).compareTo(settings.getString("user_id", "")) == 0)
	        	  	  {
		        		  Dialogs.getChangeAdminDialog(Preferences.this, settings, true);
					  }
		        	  else
		        	  {		        	  
		        		  Utilities.leaveWG(Preferences.this, settings, user);
		        	  }
		           }				
				});
				
				builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
				{
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	   dialog.cancel();
		           }
				});
				       
				AlertDialog alert = builder.create();
				alert.show();									
			}
		});
        
        changeWGPasswordButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Dialogs.getChangeWGPasswordDialog(Preferences.this, settings);
			}
		});
        
        changeAdminButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Dialogs.getChangeAdminDialog(Preferences.this, settings, false);			
			}
		});
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
	        	Dialogs.getAboutDialog(Preferences.this, settings);
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(Preferences.this, Main.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
}
