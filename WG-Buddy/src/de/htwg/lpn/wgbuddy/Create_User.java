package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Create_User extends Activity 
{
	private SharedPreferences settings = null;
	
	private TextView usernameTextView;
    private TextView emailTextView;    
    private TextView email2TextView;
    private TextView passwordTextView;
    private TextView password2TextView;
    
    private Button createButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.create_user);
    	
    	settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
    	
    	usernameTextView = (TextView) findViewById(R.id.create_user_usernameEdit);
    	emailTextView = (TextView) findViewById(R.id.create_user_emailEdit);
    	email2TextView = (TextView) findViewById(R.id.create_user_email2Edit);
    	passwordTextView = (TextView) findViewById(R.id.create_user_passwordEdit);
    	password2TextView = (TextView) findViewById(R.id.create_user_password2Edit);
    	
    	createButton = (Button) findViewById(R.id.create_user_createButton);
    	
    	createButton.setOnClickListener(new OnClickListener()
    	{
			
			@Override
			public void onClick(View v) 
			{
				if(usernameTextView.getText().toString().length() == 0 
						|| emailTextView.getText().toString().length() == 0
						|| email2TextView.getText().toString().length() == 0
						|| passwordTextView.getText().toString().length() == 0
						|| password2TextView.getText().toString().length() == 0)
    			{
					Utilities.message(Create_User.this, "Bitte alle Felder ausf�llen.", "OK"); //TODO TEXT in string.xml
					return;
    			}
				
				
				String username = Utilities.getStringFormat(usernameTextView.getText().toString());
				String email = emailTextView.getText().toString().toLowerCase();
				String email2 = email2TextView.getText().toString().toLowerCase();
				String password = Utilities.md5(passwordTextView.getText().toString());
				String password2 = Utilities.md5(password2TextView.getText().toString());
				
				// TODO: Name auf Sonderzeichen pr�fen: z.B. &!?.....
				
				if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
				{
					Utilities.message(Create_User.this, "E-Mail-Adresse ist fehlerhaft.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				if(!email.equals(email2))
				{
					Utilities.message(Create_User.this, "E-Mail-Adressen stimmen nicht �berein.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				if(!password.equals(password2))
				{
					Utilities.message(Create_User.this, "Passw�rter stimmen nicht �berein.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				
				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username);
				if(userList.size() > 0)
				{
					Utilities.message(Create_User.this, "Benutzername ist schon vergeben.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				ArrayList<HashMap<String, String>> listEmail = user.get("?email=" + email);
				if(listEmail.size() > 0)
				{
					Utilities.message(Create_User.this, "E-Mail ist schon vergeben.", "OK"); //TODO TEXT in string.xml
					return;
				}
				
				// Insert User into DB.
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("password", password));    				
				user.insert(nameValuePairs);
				
				userList = user.get("?email=" + email);
				if(userList.size() == 1)
				{
					// Insert Values into SharedPreferences.				
	    			SharedPreferences.Editor editor = settings.edit();        		    
	    			editor.putString("user_id", userList.get(0).get("id"));
        		    editor.putString("user_name", userList.get(0).get("username"));
        		    editor.putString("user_password", userList.get(0).get("password"));
        		    editor.putString("user_email", userList.get(0).get("email"));
	    		    editor.commit();        		    
					
	    		    Intent intent = new Intent(Create_User.this,WGBuddyActivity.class);
	     		    startActivity(intent);
				}
				else if(userList.size() > 1)	// D�rfte nie passieren...
				{
					Utilities.message(Create_User.this, "Fehler: Benutzername schon vorhanden.", "OK");
				}
				else // D�rfte nie passieren...
				{
					Utilities.message(Create_User.this, "Fehler beim Speichern. Versuchen Sie es bitte erneut.", "OK");
				}
			}
		});
    }
}