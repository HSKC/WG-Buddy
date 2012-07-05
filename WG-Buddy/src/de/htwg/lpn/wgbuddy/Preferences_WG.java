package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;


public class Preferences_WG extends Activity 
{
    private SharedPreferences settings;
	private ListView wgUserList;
	private Button leaveWGButton;
	private Integer userId = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_wg);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        User user = new User(settings);
        ArrayList<HashMap<String, String>> userList = user.get("?wgId=" + settings.getString("wg_id", ""));
        
        wgUserList = (ListView) findViewById(R.id.preferences_wg_list);
        leaveWGButton = (Button) findViewById(R.id.preferences_leaveButton);
        
        ArrayList<String> names = new ArrayList<String>();	        
        
        SimpleAdapter adapter = new SimpleAdapter(this, userList, R.layout.preferences_wglist_entry, new String[] { "username" }, new int[] { R.id.preferences_wglist_name});

        wgUserList.setAdapter(adapter);
        
        leaveWGButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{					
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences_WG.this);
				builder.setMessage("Sind Sie sicher, dass Sie die WG verlassen möchten? Sie werden anschließend automatisch ausgeloggt.");
				builder.setCancelable(true);
				
				builder.setPositiveButton("WG verlassen", new DialogInterface.OnClickListener() 
				{
		           public void onClick(DialogInterface dialog, int id) 
		           {			        	  			        	   
		        	  User user = new User(settings);
		        	  
		        	  if(settings.getString("wg_adminId", "").compareTo(settings.getString("user_id", "")) == 0)
	        	  	  {
		        		  changeAdminDialog();
					  }	
		        	  
		        	  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        	  nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("0", ""))); 
		        	  user.update(Integer.valueOf(settings.getString("user_id", "")), nameValuePairs);
		        	  
		        	  SharedPreferences.Editor editor = settings.edit();      		    
		        	  editor.clear();
		        	  editor.commit();        		    
		        	  Intent intent = new Intent(Preferences_WG.this, WGBuddyActivity.class);
		        	  startActivity(intent);
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
    }
	
	private void changeAdminDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Preferences_WG.this);
		
		final Spinner spinner = new Spinner(Preferences_WG.this);		
		
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				userId = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
		
		builder.setMessage("Sie müssen einen neuen Admin auswählen.");
		builder.setCancelable(false);
		builder.setView(spinner);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   User user = new User(settings);        	   
        	   
        	   spinner.getSelectedItem();
        	           	   
           }
		});
		       
		AlertDialog alert = builder.create();
		alert.show();	
	}
}
