package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Config;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

/**
 * Activity-Klasse der Informations-Ansicht.
 */
public class Informations extends Activity
{
	private SharedPreferences settings;

	private TextView wgName;
	private TextView wgAdmin;
	private TextView wgPassword;
	private ListView wgUserList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informations);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Config.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		wgName = (TextView) findViewById(R.id.informations_wgName);
		wgAdmin = (TextView) findViewById(R.id.informations_wgAdmin);
		wgPassword = (TextView) findViewById(R.id.informations_wgPassword);
		wgUserList = (ListView) findViewById(R.id.informations_wg_list);

		// Daten in in die verschiedenen TextViews eintragen.
		User user = new User(settings);
		wgName.setText(settings.getString("wg_name", ""));

		ArrayList<HashMap<String, String>> adminList = user.get("?id=" + Utilities.getWGAdminId(settings));
		wgAdmin.setText(adminList.get(0).get("username"));

		WG wg = new WG(settings);
		ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", ""));
		wgPassword.setText(wgList.get(0).get("password"));

		// Bewohner-Liste aus der Datenbank laden.
		ArrayList<HashMap<String, String>> userList = user.get("?wgId=" + settings.getString("wg_id", ""));

		// Adapter zum Befüllen der Bewohner-Liste erzeugen.
		SimpleAdapter adapter = new SimpleAdapter(this, userList, R.layout.informations_wglist_entry, new String[] { "username", "email", "points" }, new int[] { R.id.informations_wglist_name,
		R.id.informations_wglist_email, R.id.informations_wglist_points });

		// Bewohner-Liste mit Daten füllen.
		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.informations_wglist_points)
				{
					String points = (String) data;
					points += getString(R.string.informations_points);

					TextView textView = (TextView) view;
					textView.setText(points);
					return true;
				}

				return false;
			}
		};

		adapter.setViewBinder(vb);
		wgUserList.setAdapter(adapter);
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
				Dialogs.getAboutDialog(Informations.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Informations.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
