package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
 * Activity-Klasse der Ansicht zum Beitreten von WG's.
 */
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

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Nicht eingeloggt.
		if (!settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password"))
		{
			// Applikation das erste mal gestartet oder nicht konfiguriert.
			// Login aufrufen
			Intent intent = new Intent(WG_Login.this, User_Login.class);
			startActivity(intent);
			return;
		}

		// Konto freigeschalten?
		if (!settings.contains("user_status"))
		{
			Intent intent = new Intent(WG_Login.this, User_Activate.class);
			startActivity(intent);
		}

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		nameTextView = (EditText) findViewById(R.id.wgpref_nameEdit);
		passwordTextView = (EditText) findViewById(R.id.wgpref_passwordEdit);
		connectButton = (Button) findViewById(R.id.wgpref_connectButton);
		createButton = (Button) findViewById(R.id.wgpref_createButton);

		connectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Felder einlesen und trim() verwenden. Bei Passwort zusätzlich
				// md5() verwenden.
				String name = nameTextView.getText().toString().trim();
				String password = passwordTextView.getText().toString().trim();

				// Alle Felder ausgefüllt?
				if (name.length() <= 0 && password.length() <= 0)
				{
					Utilities.toastMessage(WG_Login.this, getString(R.string.utilities_allFields));
					return;
				}

				// Auf unerlaubte Zeichen prüfen.
				if (!Utilities.checkOnlyAllowedCharacter(name))
				{
					Utilities.toastMessage(WG_Login.this, getString(R.string.utilities_forbiddenSignsWGname));
					return;
				}

				// Prüfen, ob WG-Name und Passwort korrekt sind.
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

					Utilities.toastMessage(WG_Login.this, getString(R.string.utilities_wgLogin));

					// Ins Hauptmenü wechseln.
					Intent intent = new Intent(WG_Login.this, Main.class);
					startActivity(intent);
				}
				else
				{
					Utilities.toastMessage(WG_Login.this, getString(R.string.utilities_wgnamePasswordWrong));
				}
			}
		});

		createButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// In die "WG erstellen"-Ansicht wechseln.
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Zurück-Button wechselt immer in die Login-Ansicht.
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(this, User_Login.class);
			startActivity(intent);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
