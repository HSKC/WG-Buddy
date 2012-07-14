package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.Message;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

/**
 * Activity-Klasse der Nachrichten-Ansicht.
 */
public class Message_List extends Activity
{
	private ListView messageList;
	private SharedPreferences settings;

	private HashMap<Integer, HashMap<String, String>> messageMap = new HashMap<Integer, HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		messageList = (ListView) findViewById(R.id.messengerList);

		// Daten in die Liste laden.
		getList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.messenger_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.add:
				intent = new Intent(Message_List.this, Message_Create.class);
				startActivity(intent);
				return true;

			case R.id.refresh:
				getList();
				return true;

			case R.id.about:
				Dialogs.getAboutDialog(Message_List.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Message_List.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if (keyCode == KeyEvent.KEYCODE_BACK)
//		{
//			Intent intent = new Intent(this, Main.class);
//			startActivity(intent);
//
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	/**
	 * Daten aus der Datenbank laden und die Liste befüllen.
	 */
	private void getList()
	{
		// Filter erstellen.
		String where = "";
		String order = "orderby=createdDate";
		String directionString = "direction=ASC";
		String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "") ? where + "&" : "") + order + "&" + directionString;

		// Nachrichten aus der Datenbank in die Liste laden.
		Message me = new Message(settings);
		ArrayList<HashMap<String, String>> list = me.get(parameter);

		// Zu jedem Eintrag aus der UserId, den Usernamen bestimmen.
		Utilities.addUsernameToList(list, messageMap, settings);

		// Adapter zum Befüllen der Bewohner-Liste erzeugen.
		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.message_entry, new String[] { "title", "username", "message", "createdDate" }, new int[] { R.id.messenger_titleText,
		R.id.messenger_userText, R.id.messenger_messageText, R.id.messenger_timeText });

		// Map für die Verwaltung der Hintergrundfarben der Nachrichten.
		final Map<String, Integer> userColorMap = new HashMap<String, Integer>();

		// ViewBinder für Daten, die nicht direkt aus der Datenbank übernommen
		// werden können.
		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.messenger_timeText)
				{
					// Datum formatieren und in den TextView eintragen.
					TextView timeview = (TextView) view;
					timeview.setText(Utilities.getDateTimeFormatForMessageList((String) data));

					return true;
				}
				if (view.getId() == R.id.messenger_userText)
				{
					// set "by" before Name
					String name = (String) data;
					TextView nameview = (TextView) view;
					nameview.setText(getResources().getString(R.string.messenger_from) + " " + name);

					// Add User to map
					if (!userColorMap.containsKey(name))
					{
						userColorMap.put(name, userColorMap.size());
					}

					// Get Color for index of User. so everyone gets a different
					// color
					Context context = getApplicationContext();
					String[] colors = context.getResources().getStringArray(R.array.messengercolors_array);
					int color = Color.parseColor(colors[userColorMap.get(name) % colors.length]);

					// Get the whole Messagebox
					View parent = (View) view.getParent();
					parent = (View) parent.getParent();
					parent = (View) parent.getParent();

					// Get the white Shape out of the XML File an add a
					// Colorfilter with the Color
					Drawable d = context.getResources().getDrawable(R.drawable.message_border);
					d.setColorFilter(color, Mode.MULTIPLY);

					// set the Shape as Background
					parent.setBackgroundDrawable(d);
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		sa.notifyDataSetChanged();
		messageList.setAdapter(sa);

		messageList.setSelection(messageList.getCount() - 1);
	}
}
