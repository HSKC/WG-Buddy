package de.htwg.lpn.wgbuddy.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import de.htwg.lpn.model.Mail;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.R;

/**
 * Sammlung von Methoden, die verschiedene Dialoge generieren und anzeigen.
 */
public class Dialogs
{
	/**
	 * Liefert den "Passwort vergessen"-Dialog. Der Benutzer bekommt einen
	 * Änderungsschlüssel um sein Passwort zu ändern.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getLostPasswordDialog(final Context context, final SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final EditText editText = new EditText(context);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint(context.getString(R.string.utilities_email));

		builder.setMessage(context.getString(R.string.utilities_lostPasswordMessage));
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton(context.getString(R.string.utilities_keyOrder), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				String email = editText.getText().toString().trim();

				if (email.length() > 0)
				{
					if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
					{
						Utilities.toastMessage(context, context.getString(R.string.utilities_forbiddenEmail));
						getLostPasswordDialog(context, settings);
						return;
					}

					Mail mail = new Mail(settings);
					mail.sendChangeKey(email);

					getChangePasswordWithKeyDialog(context, settings);
					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordLostEmail));
				}
				else
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_allFields));
					getLostPasswordDialog(context, settings);
					return;
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den "Passwort ändern"-Dialog. Der Benutzer kann mit Hilfe eines
	 * Schlüssel das Passwort ändern.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getChangePasswordWithKeyDialog(final Context context, final SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);
		final EditText password2EditText = new EditText(context);

		keyEditText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		keyEditText.setHint(context.getString(R.string.utilities_key));

		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint(context.getString(R.string.utilities_password));

		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint(context.getString(R.string.utilities_password2));

		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);

		builder.setMessage(context.getString(R.string.utilities_keyMessage));
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton(context.getString(R.string.utilities_passwordChange), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				String key = keyEditText.getText().toString().trim();
				String password = Utilities.md5(passwordEditText.getText().toString().trim());
				String password2 = Utilities.md5(password2EditText.getText().toString().trim());

				if (key.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_fillAllFields));
					getChangePasswordWithKeyDialog(context, settings);
					return;
				}

				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

				if (userList.size() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_keyWrong));
					getChangePasswordWithKeyDialog(context, settings);
					return;
				}

				for (HashMap<String, String> userEntry : userList)
				{
					if (!password.equals(password2))
					{
						Utilities.toastMessage(context, context.getString(R.string.utilities_passwordDifferent));
						getChangePasswordWithKeyDialog(context, settings);
						return;
					}

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("password", password));
					nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
					user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);

					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordChangeSucceed));
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den "Passwort ändern"-Dialog. Der Benutzer kann mit Hilfe des
	 * alten Passwort's das Passwort ändern.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getChangePasswordDialog(final Context context, final SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);
		final EditText password2EditText = new EditText(context);

		keyEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		keyEditText.setHint(context.getString(R.string.utilities_passwordOld));

		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint(context.getString(R.string.utilities_password));

		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint(context.getString(R.string.utilities_password2));

		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);

		builder.setMessage(context.getString(R.string.utilities_passwordOldNewMessage));
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton(context.getString(R.string.utilities_passwordChange), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				String key = keyEditText.getText().toString().trim();
				String oldPassword = Utilities.md5(keyEditText.getText().toString().trim());
				String password = Utilities.md5(passwordEditText.getText().toString().trim());
				String password2 = Utilities.md5(password2EditText.getText().toString().trim());

				if (key.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_fillAllFields));
					getChangePasswordDialog(context, settings);
					return;
				}

				User user = new User(settings);
				ArrayList<HashMap<String, String>> userList = user.get("?id=" + settings.getString("user_id", "") + "&password=" + oldPassword);

				if (userList.size() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordWrong));
					getChangePasswordDialog(context, settings);
					return;
				}

				for (HashMap<String, String> userEntry : userList)
				{
					if (!password.equals(password2))
					{
						Utilities.toastMessage(context, context.getString(R.string.utilities_passwordDifferent));
						getChangePasswordDialog(context, settings);
						return;
					}

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("password", password));
					user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);

					SharedPreferences.Editor editor = settings.edit();
					editor.putString("user_password", password);
					editor.commit();

					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordChangeSucceed));
				}
			}
		});

		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den "WG-Passwort ändern"-Dialog. Der Admin kann mit Hilfe des
	 * alten Passwort's das Passwort ändern.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getChangeWGPasswordDialog(final Context context, final SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);
		final EditText password2EditText = new EditText(context);

		keyEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		keyEditText.setHint(context.getString(R.string.utilities_passwordOld));

		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint(context.getString(R.string.utilities_password));

		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint(context.getString(R.string.utilities_password2));

		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);

		builder.setMessage(context.getString(R.string.utilities_passwordWGOldNewMessage));
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton(context.getString(R.string.utilities_passwordChange), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				String oldPassword = keyEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();
				String password2 = password2EditText.getText().toString().trim();

				if (oldPassword.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_fillAllFields));
					getChangeWGPasswordDialog(context, settings);
					return;
				}

				WG wg = new WG(settings);
				ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", "") + "&password=" + oldPassword);

				if (wgList.size() <= 0)
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordWrong));
					getChangeWGPasswordDialog(context, settings);
					return;
				}

				for (HashMap<String, String> wgEntry : wgList)
				{
					if (!password.equals(password2))
					{
						Utilities.toastMessage(context, context.getString(R.string.utilities_passwordDifferent));
						getChangeWGPasswordDialog(context, settings);
						return;
					}

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("password", password));
					wg.update(Integer.valueOf(wgEntry.get("id")), nameValuePairs);

					Utilities.toastMessage(context, context.getString(R.string.utilities_passwordChangeSucceed));
				}
			}
		});

		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den "Admin ändern"-Dialog. Der Admin kann einen neuen Admin
	 * auswählen.
	 * 
	 * @param context
	 * @param settings
	 * @param leave
	 *            Falls true, WG anschließend mit Hilfe der Methode
	 *            Utilities.leaveWG() verlassen. Falls false, in die
	 *            Einstellungen-Ansicht zurückkehren.
	 */
	public static void getChangeAdminDialog(final Context context, final SharedPreferences settings, final Boolean leave)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final Spinner spinner = new Spinner(context);

		User user = new User(settings);
		ArrayList<HashMap<String, String>> userList = user.get("?wgId=" + settings.getString("wg_id", ""));

		// Kein anderer Bewohner mehr in der WG
		if (userList.size() <= 1)
		{
			if (leave)
			{
				WG wg = new WG(settings);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("adminId", "0"));
				wg.update(Integer.valueOf(settings.getString("wg_id", "")), nameValuePairs);

				Utilities.leaveWG(context, settings, user);

				return;
			}
			else
			{
				Utilities.toastMessage(context, context.getString(R.string.utilities_noOtherUser));
				return;
			}
		}

		ArrayList<String> names = new ArrayList<String>();
		for (HashMap<String, String> map : userList)
		{
			if (map.get("id").compareTo(Utilities.getWGAdminId(settings)) != 0)
			{
				names.add(map.get("username"));
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, names);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		builder.setMessage(context.getString(R.string.utilities_selectNewAdmin));
		builder.setCancelable(false);
		builder.setView(spinner);
		builder.setPositiveButton(context.getString(R.string.utilities_ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				User user = new User(settings);
				String username = (String) spinner.getSelectedItem();
				ArrayList<HashMap<String, String>> userList = user.get("?username=" + username);

				WG wg = new WG(settings);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("adminId", userList.get(0).get("id")));
				wg.update(Integer.valueOf(settings.getString("wg_id", "")), nameValuePairs);

				if (leave)
				{
					Utilities.leaveWG(context, settings, user);
				}
				else
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_changeAdminSucceed));
					((Activity) context).finish();
					((Activity) context).startActivity(((Activity) context).getIntent());
				}
			}
		});

		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den About-Dialog.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getAboutDialog(final Context context, SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.utilities_aboutText));
		builder.setCancelable(false);
		builder.setPositiveButton(context.getString(R.string.utilities_ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});

		builder.setNegativeButton(context.getString(R.string.utilities_aboutLinkText), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.utilities_aboutLink)));
				context.startActivity(i);
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Liefert den Invite-Dialog.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getInviteDialog(final Context context, final SharedPreferences settings)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final EditText editText = new EditText(context);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint(context.getString(R.string.utilities_email));

		builder.setMessage(context.getString(R.string.utilities_inviteText));
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton(context.getString(R.string.main_invite), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				String email = editText.getText().toString().trim();

				if (email.length() > 0)
				{
					if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
					{
						Utilities.toastMessage(context, context.getString(R.string.utilities_forbiddenEmail));
						getInviteDialog(context, settings);
						return;
					}

					Mail mail = new Mail(settings);
					mail.sendInvite(email);

					Utilities.toastMessage(context, context.getString(R.string.utilities_inviteSucceed));
				}
				else
				{
					Utilities.toastMessage(context, context.getString(R.string.utilities_allFields));
					getInviteDialog(context, settings);
					return;
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		builder.create().show();
	}

	/**
	 * Liefert den "WG verlassen"-Dialog.
	 * 
	 * @param context
	 * @param settings
	 */
	public static void getLeaveWGDialog(final Context context, final SharedPreferences settings)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.preferences_leaveText));
		builder.setCancelable(true);

		builder.setPositiveButton(context.getString(R.string.preferences_leaveWG), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				User user = new User(settings);

				if (Utilities.getWGAdminId(settings).compareTo(settings.getString("user_id", "")) == 0)
				{
					Dialogs.getChangeAdminDialog(context, settings, true);
				}
				else
				{
					Utilities.leaveWG(context, settings, user);
				}
			}
		});

		builder.setNegativeButton(context.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
