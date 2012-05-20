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
        
        wgname 		= (EditText) findViewById(R.id.wgnameedit);
        password 	= (EditText) findViewById(R.id.wgpasswordedit);
        connect 	= (Button) findViewById(R.id.wgconnectbutton);
        create 		= (Button) findViewById(R.id.wgcreatebutton);
        
        connect.setOnClickListener
        (
        	new OnClickListener() 
        	{
				@Override
				public void onClick(View v) 
				{
					if(wgname.getText().toString().length() > 0 && password.getText().toString().length() > 0)
					{
						//TODO: �berpr�fung ob WG in Datenbank
						

						Intent intent = new Intent(Preferences_WG.this,Preferences_User.class);
						intent.putExtra("wg",wgname.getText().toString());
						intent.putExtra("password",password.getText().toString());
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