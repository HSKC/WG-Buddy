package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.htwg.lpn.model.MethodBase;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

/**
 * Activity-Klasse der Hauptmenü-Ansicht.
 */
public class Main extends Activity
{
	public static final String PREFS_NAME = "WGBuddyPreferences";
	public static final String WEBSERVER = MethodBase.getWebserver();
	public static boolean USEPUSH = true;

	private Button shoppinglist;
	private Button taskdistributor;
	private Button messenger;
	private Button preferences;
	private Button information;
	private Button invite;
	private Button logout;
	private TextView heading;

	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(PREFS_NAME, 0);

		// Die Webserver Url in den gemeinsamen Speicher schreiben.
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("pref_webserver", MethodBase.getWebserver());
		editor.commit();

		// Nicht eingeloggt.
		if (!settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password"))
		{
			// Applikation das erste mal gestartet oder nicht eingeloggt.
			// Login aufrufen
			Intent intent = new Intent(Main.this, User_Login.class);
			startActivity(intent);
			return;
		}

		// Konto freigeschalten?
		if (!settings.contains("user_status"))
		{
			// Konto aktivieren aufrufen.
			Intent intent = new Intent(Main.this, User_Activate.class);
			startActivity(intent);
		}

		User user = new User(settings);
		ArrayList<HashMap<String, String>> userList = user.get("?id=" + settings.getString("user_id", ""));

		// Prüfen, ob Benutzer in der Datenbank ist.
		if (userList.size() == 0)
		{
			// Alle Daten im Speicher löschen.
			SharedPreferences.Editor e = settings.edit();
			e.clear();
			e.commit();

			// Login-Ansicht aufrufen.
			Intent intent = new Intent(Main.this, User_Login.class);
			startActivity(intent);
			return;
		}

		// Keiner WG zugewiesen
		if (userList.get(0).get("wgId").equals("0"))
		{
			// Ansicht zum Beitreten einer WG aufrufen.
			Intent intent = new Intent(Main.this, WG_Login.class);
			startActivity(intent);
			return;
		}

		// Prüfen, ob WG-Daten im gemeinsamen Speicher sind.
		if (!settings.contains("wg_id") || !settings.contains("wg_name") || !settings.contains("wg_password"))
		{
			// WG-Daten aus der Datenbank laden und in den gemeinsamen Speicher
			// laden.
			WG wg = new WG(settings);
			String string = userList.get(0).get("wgId");
			ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + string);

			editor.putString("wg_id", wgList.get(0).get("id"));
			editor.putString("wg_name", wgList.get(0).get("name"));
			editor.putString("wg_password", wgList.get(0).get("password"));
			editor.commit();
		}

		// Prüfen, on die Daten für den GoogleService im Speicher sind.
		if (((!settings.contains("registrationKey")) && (!settings.contains("registrationKeydate")))
		|| (settings.contains("registrationKeydate") && (new Date(settings.getLong("registrationKeydate", 0) + 86400000).getTime() < new Date().getTime())))
		{
			// Registrieren bei Google und Registrierungsid anfordern
			Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
			intent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			intent.putExtra("sender", "wgbuddy2012@googlemail.com");
			System.out.println("sended");
			getApplicationContext().startService(intent);
		}
		else
		{
			System.out.println(settings.getString("registrationKey", ""));
		}

		// Applikation schon initialisiert.
		setContentView(R.layout.main);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		shoppinglist = (Button) findViewById(R.id.main_shoppingListButton);
		taskdistributor = (Button) findViewById(R.id.main_taskDistributorButton);
		messenger = (Button) findViewById(R.id.main_messengerButton);
		preferences = (Button) findViewById(R.id.main_preferencesButton);
		information = (Button) findViewById(R.id.main_informationButton);
		invite = (Button) findViewById(R.id.main_addButton);
		logout = (Button) findViewById(R.id.main_logoutButton);
		heading = (TextView) findViewById(R.id.main_headingText);

		// Titel setzen.
		heading.setText(settings.getString("user_name", "") + " @ " + settings.getString("wg_name", ""));

		shoppinglist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this, Shopping_List.class);
				startActivity(intent);
			}
		});

		taskdistributor.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this, Task_List.class);
				startActivity(intent);
			}
		});

		messenger.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this, Message_List.class);
				startActivity(intent);
			}
		});

		preferences.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this, Preferences.class);
				startActivity(intent);
			}
		});

		information.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this, Informations.class);
				startActivity(intent);
			}
		});

		invite.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Dialogs.getInviteDialog(Main.this, settings);
			}
		});

		logout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Alle Daten aus dem gemeinsamen Speicher löschen.
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();

				Utilities.toastMessage(Main.this, getString(R.string.utilities_logout));

				Intent intent = new Intent(Main.this, Main.class);
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
				Dialogs.getAboutDialog(Main.this, settings);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Klick Zurück-Button abfangen, sodass nicht zurück in den
		// Login-Bereich gewechselt werden kann.
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			moveTaskToBack(true); // Programm beenden.
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
