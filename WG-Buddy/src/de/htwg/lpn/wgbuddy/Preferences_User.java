package de.htwg.lpn.wgbuddy;


import java.util.ArrayList;
import java.util.HashMap;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.Utilities;
import de.htwg.lpn.model.WG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Preferences_User extends Activity
{
	private SharedPreferences settings = null;
	private EditText usernameTextView;
	private EditText passwordTextView;
	private Button saveButton;
	private Button createButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_user);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        usernameTextView = (EditText) findViewById(R.id.userpref_nameEdit);
        passwordTextView = (EditText) findViewById(R.id.userpref_passwordEdit);
        saveButton = (Button) findViewById(R.id.userpref_saveButton);
        createButton = (Button) findViewById(R.id.userpref_createButton);

        
        saveButton.setOnClickListener
        (
        	new OnClickListener() 
        	{		
        		@Override
        		public void onClick(View v) 
        		{
        			if(usernameTextView.getText().toString().length() > 0 && passwordTextView.getText().toString().length() > 0)
        			{
        				String username = Utilities.getStringFormat(usernameTextView.getText().toString());
        				String password = Utilities.md5(passwordTextView.getText().toString());
        				
        				User user = new User(settings);
        				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username + "&password=" + password);
        				if(userList.size() == 1)
        				{
		        			SharedPreferences.Editor editor = settings.edit();
		        			
		        			// User Eigenschaften speichern.
		        			editor.putString("user_id", userList.get(0).get("id"));
		        		    editor.putString("user_name", userList.get(0).get("username"));
		        		    editor.putString("user_password", userList.get(0).get("password"));
		        		    editor.putString("user_email", userList.get(0).get("email"));
		        		    
		        		    // WG zugewiesen?
		        		    if(!userList.get(0).get("wgId").equals("0"))
		        		    {
		        		    	WG wg = new WG(settings);
		        		    	ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + userList.get(0).get("wgId"));
		        		    	
		        		    	if(wgList.size() == 1)
		        		    	{		        		    	
			        		    	// WG Eigenschaften speichern.
		        		    		editor.putString("wg_id", wgList.get(0).get("id"));
				        		    editor.putString("wg_name", wgList.get(0).get("name"));
				        		    editor.putString("wg_password", wgList.get(0).get("password"));
		        		    	}
		        		    }		        		    
		        		    
		        		    editor.commit();
		        		    
				        	Intent intent = new Intent(Preferences_User.this,WGBuddyActivity.class);
			        		startActivity(intent);
        				}
        				else
        				{        					
        					Utilities.message(Preferences_User.this, "Der eingegebene Benutzername oder das Passwort ist falsch", "OK");	//TODO :string.xml
        				}
        			}
        		}
        	}
        );
        
        createButton.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Preferences_User.this,Create_User.class);
     		    startActivity(intent);
			}
		});
    }
}
