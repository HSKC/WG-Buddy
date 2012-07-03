package de.htwg.lpn.wgbuddy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.Utilities;
import de.htwg.lpn.model.WG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Preferences_User extends Activity
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
        setContentView(R.layout.preferences_user);
        
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
        				Utilities.message(Preferences_User.this, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			if(!Utilities.checkOnlyAllowedCharacter(username))
    				{
    					Utilities.message(Preferences_User.this, "Benutzername enthält unerlaubte Zeichen. Es sind nur folgende Zeichen erlaubt: a-z A-Z äöü ÄÖÜ ß 0-9 _", "OK"); //TODO TEXT in string.xml
    					return;
    				}
        			
    				User user = new User(settings);
    				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username + "&password=" + password + "&auth_username=" + username + "&auth_password=" + password);
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
	        		    
			        	Intent intent = new Intent(Preferences_User.this,WGBuddyActivity.class);
		        		startActivity(intent);
    				}
    				else
    				{        					
    					Utilities.message(Preferences_User.this, "Der eingegebene Benutzername oder das Passwort ist falsch", "OK");	//TODO :string.xml
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
        
        lostPasswordButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				getEditTextDialog();				
			}

			private void getEditTextDialog() 
			{
				getLostPasswordDialog();
			}
			
		});
        changePasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				getChangePasswordDialog();				
			}
		});
    }
	
	private void getActivateAccountDialog() 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Preferences_User.this);
		
		final EditText editText = new EditText(Preferences_User.this);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint("Schlüssel");
		
		builder.setMessage("Der Account wurde noch nicht aktiviert. Bitte geben Sie den Schlüssel ein, welcher Ihnen per E-Mail zugeschickt wurde.");
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton("Account aktivieren", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   	String key = Utilities.md5(editText.getText().toString().trim());
	        	   			
   			if(key.length() <= 0)
   			{
   				Utilities.message(Preferences_User.this, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
   				return;
   			}
   			
   			User user = new User(settings);
   			ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

   			if(userList.size() <= 0)
   			{
   				Utilities.message(Preferences_User.this, "Schlüssel falsch.", "OK");	//TODO :string.xml
   				return;
   			}
   			
   			for(HashMap<String, String> userEntry : userList)
   			{
//   				Date changeTimestamp = null;
//   				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//   	            try 
//   	            {
//						changeTimestamp = sdfToDate.parse(userEntry.get("changeTimestamp"));
//					} 
//   	            catch (ParseException e) 
//   	            {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//   				
//   	            if(changeTimestamp == null)
//   	            {
//   	            	Utilities.message(Preferences_User.this, "Leider ist ein unerwarteter Fehler aufgetreten.", "OK");	//TODO :string.xml
//       				return;
//   	            }
//   	            
//       			Date now = new Date();
//       			
//       			Calendar calendar = new GregorianCalendar();
//       			calendar.setTime(changeTimestamp);
//
//       			// Add 3 days
//       			calendar.add(Calendar.DAY_OF_MONTH, 3);
//       			
//       			// Falls der Schlüssel älter als drei Tage ist.
//       			if(calendar.getTime().getTime() < now.getTime())
//       			{
//       				Utilities.message(Preferences_User.this, "Kein gültiger Schlüssel. Möglicherweise ist dieser zu alt. Sie können jedoch einen neuen anfordern.", "OK");	//TODO :string.xml
//       				return;
//       			}
   				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    			nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
    			nameValuePairs.add(new BasicNameValuePair("changeTimestamp", ""));
				user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);
   				}
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
	
	private void getLostPasswordDialog() 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Preferences_User.this);
		
		final EditText editText = new EditText(Preferences_User.this);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint("E-Mail");
		
		builder.setMessage("Bitte gebe deine E-Mail Adresse ein. Ein zufällig generierter  Schlüssel wird dann an deine E-Mail Adresse geschickt.");
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton("Schlüssel anfordern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   User user = new User(settings);
        	   user.getNewKey(editText.getText().toString());
        	   
        	   getChangePasswordDialog();
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
	
	private void getChangePasswordDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Preferences_User.this);
		
		final EditText keyEditText = new EditText(Preferences_User.this);
		final EditText passwordEditText = new EditText(Preferences_User.this);		
		final EditText password2EditText = new EditText(Preferences_User.this);
		
		keyEditText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		keyEditText.setHint("Schlüssel");
		
		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint("Passwort");
		
		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint("Passwort wiederholen");
		
		final LinearLayout layout = new LinearLayout(Preferences_User.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);
		
		builder.setMessage("Geben Sie bitte den Ihnen zugeschickten Schlüssel, so wie ein neues Passwort ein.");
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton("Passwort ändern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {		        	   
	        	String key = Utilities.md5(keyEditText.getText().toString().trim());
	        	String password = Utilities.md5(passwordEditText.getText().toString().trim());
	        	String password2 = Utilities.md5(password2EditText.getText().toString().trim());
    			
    			if(key.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
    			{
    				Utilities.message(Preferences_User.this, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			User user = new User(settings);
    			ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

    			if(userList.size() <= 0)
    			{
    				Utilities.message(Preferences_User.this, "Schlüssel falsch.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			for(HashMap<String, String> userEntry : userList)
    			{
//    				Date changeTimestamp = null;
//    				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//    	            try 
//    	            {
//						changeTimestamp = sdfToDate.parse(userEntry.get("changeTimestamp"));
//					} 
//    	            catch (ParseException e) 
//    	            {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    				
//    	            if(changeTimestamp == null)
//    	            {
//    	            	Utilities.message(Preferences_User.this, "Leider ist ein unerwarteter Fehler aufgetreten.", "OK");	//TODO :string.xml
//        				return;
//    	            }
//    	            
//        			Date now = new Date();
//        			
//        			Calendar calendar = new GregorianCalendar();
//        			calendar.setTime(changeTimestamp);
//
//        			// Add 3 days
//        			calendar.add(Calendar.DAY_OF_MONTH, 3);
//        			
//        			// Falls der Schlüssel älter als drei Tage ist.
//        			if(calendar.getTime().getTime() < now.getTime())
//        			{
//        				Utilities.message(Preferences_User.this, "Kein gültiger Schlüssel. Möglicherweise ist dieser zu alt. Sie können jedoch einen neuen anfordern.", "OK");	//TODO :string.xml
//        				return;
//        			}
        			
        			if(!password.equals(password2))
        			{
        				Utilities.message(Preferences_User.this, "Passwörter stimmen nicht überein.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        			nameValuePairs.add(new BasicNameValuePair("password", password));
        			nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
        			nameValuePairs.add(new BasicNameValuePair("changeTimestamp", ""));
    				user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);
    			}
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
