package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

/**
 * Activity-Klasse der Login-Ansicht.
 */
public class User_Login extends Activity
{
	private SharedPreferences settings = null;
	private EditText usernameTextView;
	private EditText passwordTextView;
	private Button saveButton;
	private Button createButton;
	private Button activateButton;
	private Button lostPasswordButton;
	private Button changePasswordButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		usernameTextView = (EditText) findViewById(R.id.userpref_nameEdit);
		passwordTextView = (EditText) findViewById(R.id.userpref_passwordEdit);
		saveButton = (Button) findViewById(R.id.userpref_saveButton);
		activateButton = (Button) findViewById(R.id.userpref_activateButton);
		createButton = (Button) findViewById(R.id.userpref_createButton);
		lostPasswordButton = (Button) findViewById(R.id.userpref_passwordButton);
		changePasswordButton = (Button) findViewById(R.id.userpref_changePasswordButton);

		saveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Felder einlesen und trim() verwenden. Bei Passwort zusätzlich
				// md5() verwenden.
				String username = usernameTextView.getText().toString().trim();
				String password = Utilities.md5(passwordTextView.getText().toString().trim());

				// Alle Felder ausgefüllt?
				if (username.length() <= 0 && password.length() <= 0)
				{
					Utilities.toastMessage(User_Login.this, getString(R.string.utilities_allFields));
					return;
				}

				// Auf unerlaubte Zeichen prüfen.
				if (!Utilities.checkOnlyAllowedCharacter(username))
				{
					Utilities.toastMessage(User_Login.this, getString(R.string.utilities_forbiddenSignsUsername));
					return;
				}

				// Prüfen, ob Name und Passwort korrekt sind.
				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username + "&password=" + password);
				if (userList.size() == 1)
				{
					SharedPreferences.Editor editor = settings.edit();

					// Konto aktiviert?
					if (Integer.valueOf(userList.get(0).get("status")) == 0)
					{
						// In "Konto aktivieren"-Ansicht umleiten.
						Intent intent = new Intent(User_Login.this, User_Activate.class);
						startActivity(intent);
					}
					else
					{
						// Status in gemeinsamen Speicher auf 1 setzen.
						editor.putString("user_status", userList.get(0).get("status"));
					}

					// User Eigenschaften speichern.
					editor.putString("user_id", userList.get(0).get("id"));
					editor.putString("user_name", userList.get(0).get("username"));
					editor.putString("user_password", userList.get(0).get("password"));
					editor.putString("user_email", userList.get(0).get("email"));

					// WG zugewiesen?
					if (!userList.get(0).get("wgId").equals("0"))
					{
						WG wg = new WG(settings);
						ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + userList.get(0).get("wgId"));

						if (wgList.size() == 1)
						{
							// WG Eigenschaften speichern.
							editor.putString("wg_id", wgList.get(0).get("id"));
							editor.putString("wg_name", wgList.get(0).get("name"));
							editor.putString("wg_password", wgList.get(0).get("password"));
						}
					}

					editor.commit();

					Utilities.toastMessage(User_Login.this, getString(R.string.utilities_login));

					// Ins Hauptmenü wechseln.
					Intent intent = new Intent(User_Login.this, Main.class);
					startActivity(intent);
				}
				else
				{
					Utilities.toastMessage(User_Login.this, getString(R.string.utilities_usernamePasswordWrong));
				}

			}
		});

		createButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Neuen User anlegen.
				Intent intent = new Intent(User_Login.this, User_Create.class);
				startActivity(intent);
			}
		});

		activateButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Konto aktivieren.
				Intent intent = new Intent(User_Login.this, User_Activate.class);
				startActivity(intent);
			}
		});

		lostPasswordButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// "Passwort vergessen"-Dialog anzeigen.
				Dialogs.getLostPasswordDialog(User_Login.this, settings);
			}

		});
		changePasswordButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// "Passwort ändern"-Dialog anzeigen.
				Dialogs.getChangePasswordWithKeyDialog(User_Login.this, settings);
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
				Dialogs.getAboutDialog(User_Login.this, settings);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Zurück-Button blocken.
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
