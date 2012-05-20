package de.htwg.lpn.wgbuddy;


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
	private EditText useremail;
	private Button usersave;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_user);
        
        connectTo = (TextView) findViewById(R.id.userpref_connectToText);
        username = (EditText) findViewById(R.id.userpref_nameEdit);
        useremail = (EditText) findViewById(R.id.userpref_emailEdit);
        usersave = (Button) findViewById(R.id.userpref_saveButton);
        
        Bundle bundle = this.getIntent().getExtras();
        wgname = bundle.getString("wg");
        password = bundle.getString("password");
        
        connectTo.setText("Connect to "+wgname);
        
        usersave.setOnClickListener
        (
        	new OnClickListener() 
        	{		
        		@Override
        		public void onClick(View v) 
        		{
        			
        			if(Patterns.EMAIL_ADDRESS.matcher(useremail.getText().toString()).matches() && username.getText().toString().length() > 0)
        			{
	        			//TODO: COnnectionstuff
	        			
	        			SharedPreferences settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	        			SharedPreferences.Editor editor = settings.edit();
	        		    editor.putBoolean("initiated", true);
	        		    editor.putString("wgname", wgname);
	        		    editor.putString("password", password);
	        		    editor.putString("username", username.getText().toString());
	        		    editor.putString("useremail", useremail.getText().toString());
	        		    editor.commit();
	        		    
	        		    Intent intent = new Intent(Preferences_User.this,WGBuddyActivity.class);
	        		    startActivity(intent);
        			}
        			else
        			{
        				//TODO: Fehlermeldung
        			}
        		}
        	}
        );
    }
}
