package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Message;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

public class Message_Create extends Activity
{
	private SharedPreferences settings = null;
	private Button send;
	private EditText title;
	private EditText message;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messenger_create);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);
		Utilities.checkByPass(this, settings);

		send = (Button) findViewById(R.id.messagecreate_sendButton);
		title = (EditText) findViewById(R.id.messagecreate_titleEdit);
		message = (EditText) findViewById(R.id.messagecreate_messageEdit);
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("wgId", settings
						.getString("wg_id", "")));
				nameValuePairs.add(new BasicNameValuePair("userId", settings
						.getString("user_id", "0")));
				nameValuePairs.add(new BasicNameValuePair("userName", settings
						.getString("user_name", "0")));

				nameValuePairs.add(new BasicNameValuePair("title", title
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("message", message
						.getText().toString()));

				Message message = new Message(settings);
				message.insert(nameValuePairs, Message_Create.this);

				if (Main.usepush)
				{
					GoogleService gs = new GoogleService(settings);
					gs.sendMessageToPhone("Message");
				}
				Intent intent = new Intent(Message_Create.this, Message_List.class);
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
				Dialogs.getAboutDialog(Message_Create.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Message_Create.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
