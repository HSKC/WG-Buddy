package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

public class Preferences_WG extends Activity
{
	private SharedPreferences settings = null;
	private EditText nameTextView;
	private EditText passwordTextView;
	private Button connectButton;
	private Button createButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_wg);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        nameTextView 		= (EditText) findViewById(R.id.wgpref_nameEdit);
        passwordTextView 	= (EditText) findViewById(R.id.wgpref_passwordEdit);
        connectButton 		= (Button) findViewById(R.id.wgpref_connectButton);
        createButton 		= (Button) findViewById(R.id.wgpref_createButton);
        
        connectButton.setOnClickListener
        (
        	new OnClickListener() 
        	{
				@Override
				public void onClick(View v) 
				{
					if(nameTextView.getText().toString().length() > 0 && passwordTextView.getText().toString().length() > 0)
        			{
        				String name = Utilities.getStringFormat(nameTextView.getText().toString());
        				String password = Utilities.md5(passwordTextView.getText().toString());
        				
        				WG wg = new WG(settings);
        				ArrayList<HashMap<String, String>> wgList = wg.get("?name=" + name + "&password=" + password);
        				if(wgList.size() == 1)
        				{
	        				SharedPreferences settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
		        			SharedPreferences.Editor editor = settings.edit();		        			
		        			
		        			// WG Eigenschaften speichern.
		        			editor.putString("wg_id", wgList.get(0).get("id"));
		        		    editor.putString("wg_name", wgList.get(0).get("name"));
		        		    editor.putString("wg_password", wgList.get(0).get("password"));
		        		    
		        		    editor.commit();
		        		    
		        		    // WG-ID im User speichern.
							User user = new User(settings);
							ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
							userNameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", ""))); 				
							user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);
		        		    
				        	Intent intent = new Intent(Preferences_WG.this, WGBuddyActivity.class);
			        		startActivity(intent);
        				}
        				else
        				{        					
        					Utilities.message(Preferences_WG.this, "Der eingegebene WG-Name oder das Passwort ist falsch", "OK");	//TODO :string.xml
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
				Intent intent = new Intent(Preferences_WG.this,Create_WG.class);
				startActivity(intent);
			}
		});
        
    }
}
