package de.htwg.lpn.wgbuddy;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Preferences_User extends Activity
{
	private String wgname;
	private String password;
	private TextView connectTo;
	
	private EditText username;
	private EditText userpwd;
	private Button usersave;
	private Button newuser;
	
   public String MD5(String pw)
   {
	   byte[] defaultBytes = pw.getBytes();
	   
	   try 
	   {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
		    md.update(defaultBytes);
			byte messageDigest[] = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) 
			{
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
	   } 
	   catch (NoSuchAlgorithmException e) 
	   {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }	   
	   
	   return "";
   }
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_user);
        
        username = (EditText) findViewById(R.id.userpref_nameEdit);
        userpwd = (EditText) findViewById(R.id.userpref_passwordEdit);
        usersave = (Button) findViewById(R.id.userpref_saveButton);
        newuser = (Button) findViewById(R.id.userpref_createButton);

        
        usersave.setOnClickListener
        (
        	new OnClickListener() 
        	{		
        		@Override
        		public void onClick(View v) 
        		{
        			
        			
//        			if(Patterns.EMAIL_ADDRESS.matcher(useremail.getText().toString()).matches() && username.getText().toString().length() > 0)
//        			{
//	        			
//        				SharedPreferences settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
//	        			SharedPreferences.Editor editor = settings.edit();
//	        		    editor.putBoolean("initiated", true);
//	        		    editor.putString("wgname", wgname);
//	        		    editor.putString("password", password);
//	        		    editor.putString("username", username.getText().toString());
//	        		    editor.putString("useremail", useremail.getText().toString());
//	        		    editor.commit();
        			
        			if(username.getText().toString().length() > 0 && userpwd.getText().toString().length() > 0)
        			{
        				//TODO: COnnectionstuff
        				Store store = ((Store)getApplicationContext());
        				store.setUsername(username.getText().toString());
        				store.setUserpwd(MD5(userpwd.getText().toString()));        				
        			}
		        		    
		        	Intent intent = new Intent(Preferences_User.this,Preferences_WG.class);
	        		startActivity(intent);

        		}
        	}
        );
        
        newuser.setOnClickListener(new OnClickListener() 
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
