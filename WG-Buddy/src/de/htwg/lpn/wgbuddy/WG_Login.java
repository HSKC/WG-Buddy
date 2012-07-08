package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
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

public class WG_Login extends Activity
{
	private SharedPreferences settings = null;
	private EditText nameTextView;
	private EditText passwordTextView;
	private Button connectButton;
	private Button createButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wg_login);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		nameTextView = (EditText) findViewById(R.id.wgpref_nameEdit);
		passwordTextView = (EditText) findViewById(R.id.wgpref_passwordEdit);
		connectButton = (Button) findViewById(R.id.wgpref_connectButton);
		createButton = (Button) findViewById(R.id.wgpref_createButton);

		connectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String name = nameTextView.getText().toString().trim();
				String password = passwordTextView.getText().toString().trim();

				if (name.length() <= 0 && password.length() <= 0)
				{
					Utilities.message(WG_Login.this, "Bitte alle Felder ausfüllen.", "OK"); // TODO
																							// :string.xml
					return;
				}

				if (!Utilities.checkOnlyAllowedCharacter(name))
				{
					Utilities.message(WG_Login.this, "WG-Name enthält unerlaubte Zeichen. Es sind nur folgende Zeichen erlaubt: a-z A-Z äöü ÄÖÜ ß 0-9 _", "OK"); // TODO
																																									// TEXT
																																									// in
																																									// string.xml
					return;
				}

				WG wg = new WG(settings);
				ArrayList<HashMap<String, String>> wgList = wg.get("?name=" + name + "&password=" + password);
				if (wgList.size() == 1)
				{
					SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();

					// WG Eigenschaften speichern.
					editor.putString("wg_id", wgList.get(0).get("id"));
					editor.putString("wg_name", wgList.get(0).get("name"));
					editor.putString("wg_password", wgList.get(0).get("password"));

					editor.commit();

					// WG-ID im User speichern.
					User user = new User(settings);
					ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
					userNameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
					user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);

					Intent intent = new Intent(WG_Login.this, Main.class);
					startActivity(intent);
				}
				else
				{
					Utilities.message(WG_Login.this, "Der eingegebene WG-Name oder das Passwort ist falsch", "OK"); // TODO
																													// :string.xml
				}
			}
		});

		createButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(WG_Login.this, WG_Create.class);
				startActivity(intent);
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
				Dialogs.getAboutDialog(WG_Login.this, settings);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
