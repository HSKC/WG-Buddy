package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.Message;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.JSON;
import de.htwg.lpn.wgbuddy.utility.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Create_Message extends Activity
{
	private SharedPreferences settings = null;
	private Button send;
	private EditText title;
	private EditText message;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_message);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        send = (Button) findViewById(R.id.messagecreate_sendButton);
        title = (EditText) findViewById(R.id.messagecreate_titleEdit);
        message = (EditText) findViewById(R.id.messagecreate_messageEdit);
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        send.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
		        nameValuePairs.add(new BasicNameValuePair("userId",settings.getString("user_id", "0")));
		        nameValuePairs.add(new BasicNameValuePair("userName",settings.getString("user_name", "0")));
		        
		        nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("message", message.getText().toString()));
		        
		        Message message = new Message(settings);
		        message.insert(nameValuePairs,Create_Message.this);
		        
		        if(WGBuddyActivity.usepush)
		        {
					List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
			        nameValuePairs2.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
			        nameValuePairs2.add(new BasicNameValuePair("msgType", "collapsed"));
			        nameValuePairs2.add(new BasicNameValuePair("messageText", "Chat"));
					
					String message2 = "http://wgbuddy.domoprojekt.de/googleService.php"; 
					
					JSON.postData(message2,nameValuePairs2,Create_Message.this);
		        }
		        Intent intent = new Intent(Create_Message.this,Messenger.class);
		        startActivity(intent);
			}
		});
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.basic_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent intent;
		switch (item.getItemId()) 
        {        	
	        case R.id.about:
	        	Dialogs.getAboutDialog(Create_Message.this, settings);
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(Create_Message.this, WGBuddyActivity.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
}
