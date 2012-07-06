package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Create_WG extends Activity 
{
	private SharedPreferences settings = null;
	
	private TextView nameTextView;
    private TextView passwordTextView;
    private TextView password2TextView;
    
    private Button createButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.create_wg);
    	
    	settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
    	
    	nameTextView = (TextView) findViewById(R.id.create_wg_nameEdit);
    	passwordTextView = (TextView) findViewById(R.id.create_wg_passwordEdit);
    	password2TextView = (TextView) findViewById(R.id.create_wg_password2Edit);
    	
    	createButton = (Button) findViewById(R.id.create_wg_createButton);
    	
    	createButton.setOnClickListener(new OnClickListener()
    	{
			
			@Override
			public void onClick(View v) 
			{
				String name = nameTextView.getText().toString().trim();
				String password = passwordTextView.getText().toString().trim();
				String password2 = password2TextView.getText().toString().trim();
				
				if(name.length() == 0 || password.length() == 0 || password2.length() == 0)
    			{
					Utilities.message(Create_WG.this, "Bitte alle Felder ausfüllen.", "OK"); //TODO TEXT in string.xml
					return;
    			}
				
				if(!Utilities.checkOnlyAllowedCharacter(name))
				{
					Utilities.message(Create_WG.this, "WG-Name enthält unerlaubte Zeichen. Es sind nur folgende Zeichen erlaubt: a-z A-Z 0-9", "OK"); //TODO TEXT in string.xml
					return;
				}	
				
				if(!password.equals(password2))
				{
					Utilities.message(Create_WG.this, "Passwörter stimmen nicht überein.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				
				Store store = ((Store)getApplicationContext());
				WG wg = new WG(settings);
				ArrayList<HashMap<String, String>> wgList = wg.get("?name=" + name);
				if(wgList.size() > 0)
				{
					Utilities.message(Create_WG.this, "WG-Name ist schon vergeben.", "OK"); //TODO TEXT in string.xml
					return;
				}

				
				// Insert User into DB.
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("adminId", settings.getString("user_id", "")));
				nameValuePairs.add(new BasicNameValuePair("password", password));    				
				wg.insert(nameValuePairs);
				
				wgList = wg.get("?name=" + name);
				if(wgList.size() == 1)
				{
					
					// Insert Values into SharedPreferences.				
	    			SharedPreferences.Editor editor = settings.edit();        		    
	    			editor.putString("wg_id", wgList.get(0).get("id"));
        		    editor.putString("wg_name", wgList.get(0).get("name"));
        		    editor.putString("wg_password", wgList.get(0).get("password"));
	    		    editor.commit();
	    		    
	    		    // WG-ID im User speichern.
					User user = new User(settings);
					ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
					userNameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", ""))); 				
					user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);
					
	    		    Intent intent = new Intent(Create_WG.this,WGBuddyActivity.class);
	     		    startActivity(intent);
				}
				else if(wgList.size() > 1)	// Dürfte nie passieren...
				{
					Utilities.message(Create_WG.this, "Fehler: WG-Name schon vorhanden.", "OK");
				}
				else // Dürfte nie passieren...
				{
					Utilities.message(Create_WG.this, "Fehler beim Speichern. Versuchen Sie es bitte erneut.", "OK");
				}
			}
		});
    }
}
