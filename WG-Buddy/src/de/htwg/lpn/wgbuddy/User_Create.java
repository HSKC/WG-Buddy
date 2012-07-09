package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.htwg.lpn.model.Mail;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

public class User_Create extends Activity
{
	private SharedPreferences settings = null;

	private TextView usernameTextView;
	private TextView emailTextView;
	private TextView email2TextView;
	private TextView passwordTextView;
	private TextView password2TextView;

	private Button createButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_create);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		usernameTextView = (TextView) findViewById(R.id.create_user_usernameEdit);
		emailTextView = (TextView) findViewById(R.id.create_user_emailEdit);
		email2TextView = (TextView) findViewById(R.id.create_user_email2Edit);
		passwordTextView = (TextView) findViewById(R.id.create_user_passwordEdit);
		password2TextView = (TextView) findViewById(R.id.create_user_password2Edit);

		createButton = (Button) findViewById(R.id.create_user_createButton);

		createButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String username = usernameTextView.getText().toString().trim();
				String email = emailTextView.getText().toString().toLowerCase().trim();
				String email2 = email2TextView.getText().toString().toLowerCase().trim();
				String password = Utilities.md5(passwordTextView.getText().toString().trim());
				String password2 = Utilities.md5(password2TextView.getText().toString().trim());

				if (username.length() == 0 || email.length() == 0 || email2.length() == 0 || password.length() == 0 || password2.length() == 0)
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_allFields));
					return;
				}

				if (!Utilities.checkOnlyAllowedCharacter(username))
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_forbiddenSignsUsername));
					return;
				}

				if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_forbiddenEmail));
					return;
				}

				if (!email.equals(email2))
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_emailDifferent));
					return;
				}

				if (!password.equals(password2))
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_passwordDifferent));
					return;
				}

				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username);
				if (userList.size() > 0)
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_usernameAlreadyUsed));
					return;
				}

				ArrayList<HashMap<String, String>> listEmail = user.get("?email=" + email);
				if (listEmail.size() > 0)
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_emailAlreadyUsed));
					return;
				}

				// Insert User into DB.
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				user.insert(nameValuePairs);

				userList = user.get("?email=" + email);
				if (userList.size() == 1)
				{
					Mail mail = new Mail(settings);
					mail.sendActivateKey(email);

					// Insert Values into SharedPreferences.
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("user_id", userList.get(0).get("id"));
					editor.putString("user_name", userList.get(0).get("username"));
					editor.putString("user_password", userList.get(0).get("password"));
					editor.putString("user_email", userList.get(0).get("email"));
					editor.remove("user_status");

					editor.commit();

					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_createUser));

					Intent intent = new Intent(User_Create.this, User_Activate.class);
					startActivity(intent);
				}
				else if (userList.size() > 1) // D�rfte nie passieren...
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_usernameAlreadyUsed));
				}
				else
				// D�rfte nie passieren...
				{
					Utilities.toastMessage(User_Create.this, getString(R.string.utilities_saveError));
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
				Dialogs.getAboutDialog(User_Create.this, settings);
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