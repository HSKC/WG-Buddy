package de.htwg.lpn.wgbuddy;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Preferences_User extends Activity
{
	private String wgname;
	private String password;
	private TextView connectTo;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_user);
        
        connectTo = (TextView) findViewById(R.id.userConncetToText);
        
        Bundle bundle = this.getIntent().getExtras();
        wgname = bundle.getString("wg");
        password = bundle.getString("password");
        
        connectTo.setText("Connect to "+wgname);
    }
}
