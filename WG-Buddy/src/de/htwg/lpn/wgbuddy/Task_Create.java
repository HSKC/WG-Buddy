package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Mail;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.RandomUser;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

/**
 * Activity-Klasse der Ansicht zum Erstellen von Aufgaben.
 */
public class Task_Create extends Activity
{
	private SharedPreferences settings;

	private TextView name_TextView;
	private TextView comment_TextView;
	private RatingBar points_RatingBar;
	private Button start_Button;

	private Button userList_Button;
	private ListView user_ListView;
	private AlertDialog alertDialog;

	private Task task;
	private User user;

	private ArrayList<HashMap<String, String>> users;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_create);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		name_TextView = (TextView) findViewById(R.id.taskNameText);
		comment_TextView = (TextView) findViewById(R.id.userTaskComment);
		points_RatingBar = (RatingBar) findViewById(R.id.taskRatingBar);
		start_Button = (Button) findViewById(R.id.usertaskgoButton);

		userList_Button = (Button) findViewById(R.id.task_userListButton);
		user_ListView = new ListView(this);

		task = new Task(settings);
		user = new User(settings);

		users = user.get("?wgId=" + settings.getString("wg_id", ""));

		// Adapter für die Bewohner-Liste.
		SimpleAdapter sa = new SimpleAdapter(this, users, R.layout.task_user_entry, new String[] { "username" }, new int[] { R.id.task_userlistcheckbox });

		// ViewBinder für Daten, die nicht direkt aus der Datenbank übernommen
		// werden können.
		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.task_userlistcheckbox)
				{
					// Checkbox-Text setzen.
					CheckBox cb = (CheckBox) view;
					cb.setText((CharSequence) data);
					cb.setChecked(true);
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		user_ListView.setAdapter(sa);

		// Teilnehmer-Dialog öffnen.
		userList_Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				alertDialog.show();
			}
		});

		// Teilnehmer-Dialog erstellen.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(user_ListView);
		builder.setPositiveButton(getString(R.string.utilities_ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});

		alertDialog = builder.create();

		clickOnStartButton();
	}

	private void clickOnStartButton()
	{
		start_Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Eingaben aus dem EditText-View laden und Leerzeichen am
				// Anfang und Ende abschneiden.
				final String name = name_TextView.getText().toString().trim();
				final String comment = comment_TextView.getText().toString().trim();
				final String points = new Double(points_RatingBar.getRating()).toString();

				// Prüfen, ob Eingaben gemacht wurden.
				if (name.compareTo("") != 0 && comment.compareTo("") != 0)
				{
					// Warte-Dialog anzeigen.
					ProgressDialog pd = ProgressDialog.show(Task_Create.this, "", getString(R.string.utilities_pleaseWait));

					// Handler, welcher nach dem Bearbeiten die Hintergrund-Task
					// ausgeführt wird.
					Handler handler = new Handler()
					{
						@Override
						public void handleMessage(Message msg)
						{
							super.handleMessage(msg);

							Utilities.toastMessage(Task_Create.this, getString(R.string.task_created) + (String) msg.obj);

							// Zurück zur Aufgaben-Ansicht.
							Intent intent = new Intent(Task_Create.this, Task_List.class);
							startActivity(intent);
						}
					};

					// Code der in der Hintergrund-Task ausgeführt werden soll.
					Callable<Message> callable = new Callable<Message>()
					{
						@Override
						public Message call() throws Exception
						{
							Message message = Message.obtain();

							ListAdapter adapter = user_ListView.getAdapter();
							Integer count = user_ListView.getChildCount();
							TreeMap<String, Double> checkedUser = new TreeMap<String, Double>();

							// Wenn count == 0, dann wurde der Teilnehmer Dialog
							// nicht aufgerufen.
							if (count == 0)
							{
								// Alle Bewohner als Kandidaten eintragen.
								for (HashMap<String, String> map : users)
								{
									checkedUser.put(map.get("username"), Double.valueOf(map.get("points")));
								}
							}
							else
							{
								// Die angeklickten Bewohner als Kandidaten in
								// die Liste eintragen.
								for (int i = 0; i < count; i++)
								{
									View entry = user_ListView.getChildAt(i);
									if (entry != null)
									{
										CheckBox checkbox = (CheckBox) entry.findViewById(R.id.task_userlistcheckbox);
										if (checkbox.isChecked())
										{
											@SuppressWarnings("unchecked")
											HashMap<String, String> userData = (HashMap<String, String>) adapter.getItem(i);
											checkedUser.put(userData.get("username"), Double.valueOf(userData.get("points")));
										}
									}
								}
							}

							// Aus den Kandidaten einen Bewohner auswählen.
							String chosenUserName = RandomUser.getRandomUser(checkedUser);

							// Benutzername für den Handler speichern.
							message.obj = chosenUserName;

							HashMap<String, String> userObject = Utilities.getUserWithName(settings, chosenUserName);

							// Werte für die Übertragung zur Datenbank
							// vorbereiten.
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
							nameValuePairs.add(new BasicNameValuePair("userId", userObject.get("id")));
							nameValuePairs.add(new BasicNameValuePair("name", name));
							nameValuePairs.add(new BasicNameValuePair("comment", comment));
							nameValuePairs.add(new BasicNameValuePair("points", points));
							nameValuePairs.add(new BasicNameValuePair("status", "0"));

							// Daten in die Datenbank eintragen.
							task.insert(nameValuePairs);

							// Alle Geräte der WG mit Hilfe des GoogleService
							// über die Änderung informieren.
							if (Main.usepush)
							{
								GoogleService gs = new GoogleService(settings);
								gs.sendMessageToPhone("Task");
							}

							// Werte für die E-Mail vorbereiten.
							List<NameValuePair> mailNameValuePairs = new ArrayList<NameValuePair>();
							mailNameValuePairs.add(new BasicNameValuePair("username", userObject.get("username")));
							mailNameValuePairs.add(new BasicNameValuePair("email", userObject.get("email")));
							mailNameValuePairs.add(new BasicNameValuePair("taskname", name));
							mailNameValuePairs.add(new BasicNameValuePair("tasktext", comment));

							// E-Mail an ausgewählten Bewohner senden.
							Mail mail = new Mail(settings);
							mail.sendTask(mailNameValuePairs);

							return message;
						}
					};

					// Hintergrund Thread starten.
					WorkerThread workerThread = new WorkerThread(callable, pd, handler);
					workerThread.start();
				}
				else
				{
					Utilities.toastMessage(Task_Create.this, getString(R.string.utilities_allFields));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.basic_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.about:
				Dialogs.getAboutDialog(Task_Create.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Task_Create.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
