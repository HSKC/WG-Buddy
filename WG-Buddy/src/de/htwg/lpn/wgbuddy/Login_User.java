package de.htwg.lpn.wgbuddy;


import java.util.ArrayList;
import java.util.HashMap;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login_User extends Activity
{
	private SharedPreferences settings = null;
	private EditText usernameTextView;
	private EditText passwordTextView;
	private Button saveButton;
	private Button createButton;
	private Button lostPasswordButton;
	private Button changePasswordButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        usernameTextView = (EditText) findViewById(R.id.userpref_nameEdit);
        passwordTextView = (EditText) findViewById(R.id.userpref_passwordEdit);
        saveButton = (Button) findViewById(R.id.userpref_saveButton);
        createButton = (Button) findViewById(R.id.userpref_createButton);
        lostPasswordButton = (Button) findViewById(R.id.userpref_passwordButton);
        changePasswordButton = (Button) findViewById(R.id.userpref_changePasswordButton);
        
        saveButton.setOnClickListener
        (
        	new OnClickListener() 
        	{		
        		@Override
        		public void onClick(View v) 
        		{
        			String username = usernameTextView.getText().toString().trim();
    				String password = Utilities.md5(passwordTextView.getText().toString().trim());
        			
        			if(username.length() <= 0 && password.length() <= 0)
        			{
        				Utilities.message(Login_User.this, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			if(!Utilities.checkOnlyAllowedCharacter(username))
    				{
    					Utilities.message(Login_User.this, "Benutzername enthält unerlaubte Zeichen. Es sind nur folgende Zeichen erlaubt: a-z A-Z äöü ÄÖÜ ß 0-9 _", "OK"); //TODO TEXT in string.xml
    					return;
    				}
        			
    				User user = new User(settings);
    				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username + "&password=" + password);
    				if(userList.size() == 1)
    				{
	        			if(Integer.valueOf(userList.get(0).get("status")) == 0)
	        			{
//	        				getActivateAccountDialog();
//	        				return;
	        			}
    					
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
	        		    
			        	Intent intent = new Intent(Login_User.this,WGBuddyActivity.class);
		        		startActivity(intent);
    				}
    				else
    				{        					
    					Utilities.message(Login_User.this, "Der eingegebene Benutzername oder das Passwort ist falsch", "OK");	//TODO :string.xml
    				}
        			
        		}
        	}
        );
        
        createButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Login_User.this,Create_User.class);
     		    startActivity(intent);
			}
		});
        
        lostPasswordButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				getEditTextDialog();				
			}

			private void getEditTextDialog() 
			{
				Dialogs.getLostPasswordDialog(Login_User.this, settings);
			}
			
		});
        changePasswordButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Dialogs.getChangePasswordWithKeyDialog(Login_User.this, settings);				
			}
		});
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
	        	Dialogs.getAboutDialog(Login_User.this, settings);
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }	
}
