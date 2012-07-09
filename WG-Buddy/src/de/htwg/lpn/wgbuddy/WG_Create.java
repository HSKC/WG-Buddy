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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WG_Create extends Activity
{
	private SharedPreferences settings = null;

	private TextView nameTextView;
	private TextView passwordTextView;
	private TextView password2TextView;

	private Button createButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wg_create);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Nicht eingeloggt.
		if (!settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password"))
		{
			// Applikation das erste mal gestartet oder nicht konfiguriert.
			// Login aufrufen
			Intent intent = new Intent(WG_Create.this, User_Login.class);
			startActivity(intent);
			return;
		}

		// Konto freigeschalten?
		if (!settings.contains("user_status"))
		{
			Intent intent = new Intent(WG_Create.this, User_Activate.class);
			startActivity(intent);
		}

		nameTextView = (TextView) findViewById(R.id.create_wg_nameEdit);
		passwordTextView = (TextView) findViewById(R.id.create_wg_passwordEdit);
		password2TextView = (TextView) findViewById(R.id.create_wg_password2Edit);

		createButton = (Button) findViewById(R.id.create_wg_createButton);

		createButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String name = nameTextView.getText().toString().trim();
				String password = passwordTextView.getText().toString().trim();
				String password2 = password2TextView.getText().toString().trim();

				if (name.length() == 0 || password.length() == 0 || password2.length() == 0)
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_allFields));
					return;
				}

				if (!Utilities.checkOnlyAllowedCharacter(name))
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_forbiddenSignsWGname));
					return;
				}

				if (!password.equals(password2))
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_passwordDifferent));
					return;
				}

				WG wg = new WG(settings);
				ArrayList<HashMap<String, String>> wgList = wg.get("?name=" + name);
				if (wgList.size() > 0)
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_wgnameAlreadyUsed));
					return;
				}

				// Insert User into DB.
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("adminId", settings.getString("user_id", "")));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				wg.insert(nameValuePairs);

				wgList = wg.get("?name=" + name);
				if (wgList.size() == 1)
				{

					// Insert Values into SharedPreferences.
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("wg_id", wgList.get(0).get("id"));
					editor.putString("wg_name", wgList.get(0).get("name"));
					editor.putString("wg_password", wgList.get(0).get("password"));
					editor.commit();

					// WG-ID im User speichern.
					User user = new User(settings);
					ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
					userNameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
					user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);

					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_createWG));

					Intent intent = new Intent(WG_Create.this, Main.class);
					startActivity(intent);
				}
				else if (wgList.size() > 1) // Dürfte nie passieren...
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_wgnameAlreadyUsed));
				}
				else
				// Dürfte nie passieren...
				{
					Utilities.toastMessage(WG_Create.this, getString(R.string.utilities_saveError));
				}
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
				Dialogs.getAboutDialog(WG_Create.this, settings);
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
