package de.htwg.lpn.wgbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Preferences_WG extends Activity
{
	private EditText wgname;
	private EditText password;
	private Button connect;
	private Button create;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_wg);
        
        wgname 		= (EditText) findViewById(R.id.wgpref_nameEdit);
        password 	= (EditText) findViewById(R.id.wgpref_passwordEdit);
        connect 	= (Button) findViewById(R.id.wgpref_connectButton);
        create 		= (Button) findViewById(R.id.wgpref_createButton);
        
        connect.setOnClickListener
        (
        	new OnClickListener() 
        	{
				@Override
				public void onClick(View v) 
				{
					if(wgname.getText().toString().length() > 0 && password.getText().toString().length() > 0)
					{
						//TODO: Überprüfung ob WG in Datenbank
						
						Store store = ((Store)getApplicationContext());
						store.setWgname(wgname.getText().toString());
						store.setWgpasswd(password.getText().toString());
						store.setInitiated(true);
						
						
						Intent intent = new Intent(Preferences_WG.this,WGBuddyActivity.class);
						startActivity(intent);
					}
					else
					{
						//TODO: Fehlermeldung
					}
						
				}
        	}
        );
        
        create.setOnClickListener(new OnClickListener() 
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
