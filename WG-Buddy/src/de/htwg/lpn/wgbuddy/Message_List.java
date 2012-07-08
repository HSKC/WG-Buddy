package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.htwg.lpn.model.Message;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
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

public class Message_List extends Activity
{
	private ListView messageList;
	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);
		Utilities.checkByPass(this, settings);

		messageList = (ListView) findViewById(R.id.messengerList);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(this, Main.class);
			startActivity(intent);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getList()
	{
		String where = "";
		String order = "orderby=createdDate";
		String directionString = "direction=ASC";

		String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "") ? where + "&" : "") + order + "&" + directionString;

		Message me = new Message(settings);
		ArrayList<HashMap<String, String>> list = me.get(parameter);

		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.message_entry, new String[] { "title", "userName", "message", "createdDate" }, new int[] { R.id.messenger_titleText,
		R.id.messenger_userText, R.id.messenger_messageText, R.id.messenger_timeText });

		final Map<String, Integer> users = new HashMap<String, Integer>();

		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.messenger_timeText)
				{
					String dateTime = Utilities.getDateTimeFormatForMessageList((String) data);
					TextView timeview = (TextView) view;
					timeview.setText(dateTime);

					return true;
				}
				if (view.getId() == R.id.messenger_userText)
				{
					// set "by" before Name
					String name = (String) data;
					TextView nameview = (TextView) view;
					nameview.setText(getResources().getString(R.string.messenger_from) + " " + name);

					// Add User to map
					if (!users.containsKey(name))
					{
						users.put(name, users.size());
					}

					// Get Color for index of User. so everyone gets a different
					// color
					Context context = getApplicationContext();
					String[] colors = context.getResources().getStringArray(R.array.messengercolors_array);
					int color = Color.parseColor(colors[users.get(name) % colors.length]);

					// Get the whole Messagebox
					View parent = (View) view.getParent();
					parent = (View) parent.getParent();
					parent = (View) parent.getParent();

					// Get the white Shape out of the XML File an add a
					// Colofilter with the Color
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
