package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.Mail;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class User_Activate extends Activity
{
	private SharedPreferences settings;

	private Button sendButton;
	private Button newKeyButton;
	private EditText keyEditText;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_activate);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		sendButton = (Button) findViewById(R.id.activate_user_send);
		newKeyButton = (Button) findViewById(R.id.activate_user_newKey);
		keyEditText = (EditText) findViewById(R.id.activate_user_key);

		if (!settings.contains("user_email"))
		{
			newKeyButton.setVisibility(View.GONE);
		}

		sendButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String key = keyEditText.getText().toString().trim();

				if (key.length() <= 0)
				{
					Utilities.toastMessage(User_Activate.this, getString(R.string.utilities_fillAllFields));
					return;
				}

				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

				if (userList.size() <= 0)
				{
					Utilities.toastMessage(User_Activate.this, getString(R.string.utilities_keyWrong));
					return;
				}

				for (HashMap<String, String> userEntry : userList)
				{
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
					nameValuePairs.add(new BasicNameValuePair("status", "1"));
					user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);

					Utilities.toastMessage(User_Activate.this, getString(R.string.utilities_accountActivateSuceed));

					Editor editor = settings.edit();

					editor.putString("user_status", "1");
					editor.commit();

					Intent intent = new Intent(User_Activate.this, Main.class);
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
				Mail mail = new Mail(settings);
				mail.sendActivateKey(settings.getString("user_email", ""));

				Utilities.toastMessage(User_Activate.this, getString(R.string.utilities_activateEmailSendedMessage));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.about:
				Dialogs.getAboutDialog(User_Activate.this, settings);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(this, User_Login.class);
			startActivity(intent);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
