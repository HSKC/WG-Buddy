package de.htwg.lpn.wgbuddy;

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
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

/**
 * Activity-Klasse der Einstellungen-Ansicht.
 */
public class Preferences extends Activity
{
	private SharedPreferences settings;
	private Button changePasswordButton;
	private Button leaveWGButton;
	private Button changeAdminButton;
	private Button changeWGPasswordButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		leaveWGButton = (Button) findViewById(R.id.preferences_leaveButton);
		changePasswordButton = (Button) findViewById(R.id.preferences_changePasswordButton);
		changeWGPasswordButton = (Button) findViewById(R.id.preferences_changeWGPasswordButton);
		changeAdminButton = (Button) findViewById(R.id.preferences_changeAdminButton);

		// AdminId und UserId aus dem allgemeinen Speicher auslesen.
		String wgAdminId = Utilities.getWGAdminId(settings);
		String userId = settings.getString("user_id", "");

		// Prüfen, ob dieser User der Admin ist.
		if (wgAdminId.compareTo(userId) != 0)
		{
			// Views, die nur dem Admin angeboten werden, werden ausgeblendet.
			changeWGPasswordButton.setVisibility(View.GONE);
			changeAdminButton.setVisibility(View.GONE);
		}

		changePasswordButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Dialog zum "Passwort ändern" aufrufen.
				Dialogs.getChangePasswordDialog(Preferences.this, settings);
			}
		});

		leaveWGButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// // Dialog zum "WG verlassen" aufrufen.
				Dialogs.getLeaveWGDialog(Preferences.this, settings);
			}
		});

		changeWGPasswordButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Admin-only: Dialog zum "WG-Passwort ändern" aufrufen.
				Dialogs.getChangeWGPasswordDialog(Preferences.this, settings);
			}
		});

		changeAdminButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Admin-only: Dialog zum "Admin ändern" aufrufen.
				Dialogs.getChangeAdminDialog(Preferences.this, settings, false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.basicrefresh_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.refresh:
				finish();
				startActivity(getIntent());
				return true;

			case R.id.about:
				Dialogs.getAboutDialog(Preferences.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Preferences.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
