package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activate_User  extends Activity 
{
	private SharedPreferences settings;
	
	private Button sendButton;
	private Button newKeyButton;
	private EditText keyEditText;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_user);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        sendButton = (Button) findViewById(R.id.activate_user_send);
        newKeyButton = (Button) findViewById(R.id.activate_user_newKey);
        keyEditText = (EditText) findViewById(R.id.activate_user_key);
        
        if(!settings.contains("user_email"))
        {
        	newKeyButton.setVisibility(View.GONE);
        }
        
        sendButton.setOnClickListener(new OnClickListener() 
        {
			@Override			
			public void onClick(View v)
			{
				String key = keyEditText.getText().toString().trim();
				
				if(key.length() <= 0)
	   			{
	   				Utilities.message(Activate_User.this, getString(R.string.utilities_fillAllFields), getString(R.string.utilities_ok));	
	   				return;
	   			}
	   			
	   			User user = new User(settings);
	   			ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);
	
	   			if(userList.size() <= 0)
	   			{
	   				Utilities.message(Activate_User.this, getString(R.string.utilities_keyWrong), getString(R.string.utilities_ok));	
	   				return;
	   			}
	   			
	   			for(HashMap<String, String> userEntry : userList)
	   			{
	   				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    			nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
	    			nameValuePairs.add(new BasicNameValuePair("status", "1"));
					user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);					
					
					Utilities.toastMessage(Activate_User.this, getString(R.string.utilities_accountActivateSuceed));
					
					Editor editor = settings.edit();
					
					editor.putString("user_status", "1");
		        	editor.commit();
					
					Intent intent = new Intent(Activate_User.this, WGBuddyActivity.class);
					startActivity(intent);
					return; 
	   			}
			}
		});
        
        newKeyButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				User user = new User(settings);
				user.sendActivateKey(settings.getString("user_email", ""));
				
				Utilities.toastMessage(Activate_User.this, getString(R.string.utilities_activateEmailSendedMessage));
			}
		});
    }
}
