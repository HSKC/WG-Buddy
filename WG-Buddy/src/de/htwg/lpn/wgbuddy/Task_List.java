package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

/**
 * Activity-Klasse der Aufgaben-Ansicht.
 */
public class Task_List extends Activity
{
	private SharedPreferences settings;

	private ListView taskList;
	private RadioGroup filterRadioGroup;

	// Variablen werden von den Spinnern im Filter-Dialog, sowie der RadioGroup
	// verändert.
	private Integer status = 0; // Status 0 = alle, Status 1 = offen, Status 2 =
								// erledigt
	private Integer sort = 1; // Sortieren nach: 0 = Aufgabe, 1 = Datum, 2 =
								// Punkte, 3 = Benutzer
	private Integer direction = 1; // Richtung: 0 = aufsteigend, 1 = absteigend
	private Integer filter = 0; // Filter: 0 = Alle anzeigen, 1 = Meine anzeigen

	// Adapter für den Filter-Dialog
	ArrayAdapter<CharSequence> statusAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;
	ArrayAdapter<CharSequence> directionAdapter = null;

	private Task task;
	private HashMap<Integer, HashMap<String, String>> taskMap = new HashMap<Integer, HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		task = new Task(settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		taskList = (ListView) findViewById(R.id.userTaskList);
		filterRadioGroup = (RadioGroup) findViewById(R.id.taskList_radio_filter);

		// Die drei ArrayAdapter für die drei Spinner im Filter-Dialog aus den
		// Resourcen erstellen.
		statusAdapter = ArrayAdapter.createFromResource(this, R.array.taskListStatus_array, android.R.layout.simple_spinner_item);
		sortAdapter = ArrayAdapter.createFromResource(this, R.array.taskListSort_array, android.R.layout.simple_spinner_item);
		directionAdapter = ArrayAdapter.createFromResource(this, R.array.listDirection_array, android.R.layout.simple_spinner_item);

		// Änderung der "Alle anzeigen / Meine anzeigen" - RadioGroup behandeln.
		filterRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.taskList_radio_filterAll)
				{
					filter = 0; // Alle Einträge anzeigen.
				}
				else
				{
					filter = 1; // Nur meine Einträge anzeigen.
				}
				getList();
			}
		});

		// Daten in die Liste laden.
		getList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tasklist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.add:
				intent = new Intent(this, Task_Create.class);
				startActivity(intent);
				return true;

			case R.id.refresh:
				getList();
				return true;

			case R.id.settings:
				getTaskListOptionsDialog();
				return true;

			case R.id.about:
				Dialogs.getAboutDialog(this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(this, Main.class);
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

	/**
	 * Daten aus der Datenbank laden und die Liste befüllen.
	 */
	private void getList()
	{
		// Werte die im Filter-Dialog ausgewählt sind für den Datenbankzugriff
		// verwenden.
		String where = "";
		String order = "";
		String directionString = "";

		// Nach Status filtern
		if (status == 1)
		{
			where = "status=0"; // Offene Aufgaben
		}
		else if (status == 2)
		{
			where = "status=1"; // Erledigte Aufgaben
		}
		else
		{
			where = ""; // Alle Aufgaben
		}

		// Alle anzeigen / Meine anzeigen hinzufügen
		if (filter == 1)
		{
			where += "&userId=" + settings.getString("user_id", "");
		}

		// Sortieren
		if (sort == 0)
		{
			order = "orderby=name";
		}
		else if (sort == 1)
		{
			order = "orderby=createdDate";
		}
		else if (sort == 2)
		{
			order = "orderby=points";
		}
		else if (sort == 3)
		{
			order = "orderby=userId";
		}

		// Sortier-Richtung
		if (direction == 0)
		{
			directionString = "direction=ASC";
		}
		else
		{
			directionString = "direction=DESC";
		}

		// Aus den einzelnen Parametern die URL zusammenbauen.
		String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "") ? where + "&" : "") + order + "&" + directionString;

		// Daten aus der Datenbank in die Liste laden.
		ArrayList<HashMap<String, String>> list = task.get(parameter);

		// Zu jedem Eintrag aus der UserId, den Usernamen bestimmen.
		Utilities.addUsernameToList(list, taskMap, settings);

		// Adapter zum Befüllen der Einkaufsliste erzeugen.
		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.list_entry, new String[] { "id", "id", "name", "comment", "points", "createdDate", "username", "status" }, new int[] {
		R.id.list_completedButton, R.id.list_deleteButton, R.id.list_name, R.id.list_comment, R.id.list_rating, R.id.list_createdDate, R.id.list_username, R.id.list_entry });

		// ViewBinder für Daten, die nicht direkt aus der Datenbank übernommen
		// werden können.
		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.list_rating)
				{
					// Punkte in 0-5 Sterne
					RatingBar rb = (RatingBar) view;
					rb.setRating(Float.valueOf(data.toString()));
					return true;
				}
				else if (view.getId() == R.id.list_createdDate)
				{
					// Datum und Zeit formatieren und darstellen.
					TextView timeview = (TextView) view;
					timeview.setText(Utilities.getDateTimeFormat((String) data));

					return true;
				}
				else if (view.getId() == R.id.list_entry)
				{
					LinearLayout ll = (LinearLayout) view;

					// Alle Farben aus den Resourcen auslesen.
					String[] colors = getResources().getStringArray(R.array.messengercolors_array);
					int color = 0;

					if (textRepresentation.compareTo("0") == 0)
					{
						color = Color.parseColor(colors[0]); // gelber
																// Hintergrund
					}
					else
					{
						color = Color.parseColor(colors[2]); // grüner
																// Hintergrund
					}

					// Get the white Shape out of the XML File an add a
					// Colofilter with the Color
					Drawable d = getResources().getDrawable(R.drawable.message_border);
					d.setColorFilter(color, Mode.MULTIPLY);

					// set the Shape as Background
					ll.setBackgroundDrawable(d);

					return true;
				}
				else if (view.getId() == R.id.list_completedButton)
				{
					// Button um die Aufgabe als erledigt zu markieren.
					ImageButton button = (ImageButton) view;
					final Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					button.setVisibility(View.VISIBLE);

					// Button ausblenden, falls die ID nicht in der Map ist
					if (!taskMap.containsKey(id))
					{
						button.setVisibility(View.INVISIBLE);
						return true;
					}

					Integer userId = Integer.valueOf(taskMap.get(id).get("userId"));
					Integer status = Integer.valueOf(taskMap.get(id).get("status"));

					// Button ausblenden, falls die Aufgabe schon erledigt ist.
					if (userId != Integer.valueOf(settings.getString("user_id", "")) || status == 1)
					{
						button.setVisibility(View.INVISIBLE);
					}

					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// Warte-Dialog anzeigen.
							ProgressDialog pd = ProgressDialog.show(Task_List.this, "", getString(R.string.utilities_pleaseWait));

							// Handler, welcher nach dem Bearbeiten die
							// Hintergrund-Task ausgeführt wird.
							Handler handler = new Handler()
							{
								@Override
								public void handleMessage(Message msg)
								{
									super.handleMessage(msg);

									// Je nach Message eine Meldung ausgeben.
									// Und ggf. die Liste neu laden.
									switch (msg.arg1)
									{
										case 0:
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_alreadyDeleted));
											break;
										case 1:
											Utilities.toastMessage(Task_List.this, getString(R.string.task_alreadyDone));
											break;
										case 2:
											Utilities.toastMessage(Task_List.this, getString(R.string.task_otherUser));
											break;
										case 3:
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_completedItem));
											break;
									}
								}
							};

							// Code der in der Hintergrund-Task ausgeführt
							// werden soll.
							Callable<Message> callable = new Callable<Message>()
							{
								@Override
								public Message call() throws Exception
								{
									// Message für den Handler erzeugen.
									Message message = Message.obtain();

									// Eintrag nochmal aus der Datenbank laden.
									ArrayList<HashMap<String, String>> selectedTask = task.get("?id=" + id.toString());

									// Ist Eintrag noch vorhanden?
									if (selectedTask.size() == 0)
									{
										// Nachricht: Nicht mehr vorhanden.
										message.arg1 = 0;
										return message;
									}

									// Wurde die Aufgabe schon als gekauft
									// markiert?
									if (selectedTask.get(0).get("status").compareTo("1") == 0)
									{
										// Nachricht: Schon erledigt.
										message.arg1 = 1;
										return message;
									}

									// Wurde die Aufgabe diesem User zugewiesen?
									if (selectedTask.get(0).get("userId").compareTo(settings.getString("user_id", "")) != 0)
									{
										// Nachricht: Die Aufgabe ist einem
										// anderen User zugewiesen.
										message.arg1 = 2;
										return message;
									}

									// UserId und Status des Eintrags ändern.
									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "")));
									nameValuePairs.add(new BasicNameValuePair("status", "1"));

									// Daten in der Datenbank aktualisieren.
									task.update(id, nameValuePairs);

									// Daten des Users aus der Datenbank lesen.
									User user = new User(settings);
									ArrayList<HashMap<String, String>> selectedUser = user.get("?id=" + settings.getString("user_id", ""));

									// Punkte der Aufgabe mit den vorhanden
									// Punkten des Users addieren.
									Float points = Float.valueOf(selectedUser.get(0).get("points")) + Float.valueOf(selectedTask.get(0).get("points"));

									// Neue Punktzahl in die Datenbank
									// eintragen.
									ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
									userNameValuePairs.add(new BasicNameValuePair("points", points.toString()));
									user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);

									// Alle Geräte der WG mit Hilfe des
									// GoogleService über die Änderung
									// informieren.
									if (Main.USEPUSH)
									{
										GoogleService gs = new GoogleService(settings);
										gs.sendMessageToPhone("Task");
									}

									message.arg1 = 3;
									return message;
								}
							};

							// Nachricht: Erfolgreich geändert.
							WorkerThread workerThread = new WorkerThread(callable, pd, handler);
							workerThread.start();
						}
					});
					return true;
				}
				else if (view.getId() == R.id.list_deleteButton)
				{
					// Button um Eintrag zu löschen.
					ImageButton button = (ImageButton) view;
					final Integer id = Integer.valueOf(data.toString());
					button.setTag(id);

					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// Dialog erstellen, bei dem man das Löschen des
							// Eintrags bestätigen muss.
							AlertDialog.Builder builder = new AlertDialog.Builder(Task_List.this);
							builder.setMessage(Task_List.this.getString(R.string.utilities_deleteQuestion));

							// Beim Klick auf "OK" wird der Eintrag gelöscht.
							builder.setPositiveButton(Task_List.this.getString(R.string.utilities_delete), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int index)
								{
									// Warte-Dialog anzeigen.
									ProgressDialog pd = ProgressDialog.show(Task_List.this, "", getString(R.string.utilities_pleaseWait));

									// Handler, welcher nach dem Bearbeiten die
									// Hintergrund-Task ausgeführt wird.
									Handler handler = new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											super.handleMessage(msg);

											// Liste neu laden.
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_deletedItem));
										}
									};

									// Code der in der Hintergrund-Task
									// ausgeführt werden soll.
									Callable<Message> callable = new Callable<Message>()
									{
										@Override
										public Message call()
										{
											// Eintrag aus Datenbank löschen.
											task.delete(id);

											// Alle Geräte der WG mit Hilfe des
											// GoogleService über die Änderung
											// informieren.
											if (Main.USEPUSH)
											{
												GoogleService gs = new GoogleService(settings);
												gs.sendMessageToPhone("Task");
											}

											return Message.obtain();
										}
									};

									// Hintergrund Thread starten.
									WorkerThread workerThread = new WorkerThread(callable, pd, handler);
									workerThread.start();
								}
							});

							// Beim Klick auf "Abbrechen" wird nichts
							// unternommen.
							builder.setNegativeButton(Task_List.this.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{

								}
							});

							// Dialog anzeigen
							AlertDialog alert = builder.create();
							alert.show();
						}
					});
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
		taskList.setAdapter(sa);
	}

	/**
	 * Filter-Dialog für die Aufgaben-Liste generieren.
	 */
	public void getTaskListOptionsDialog()
	{
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(Task_List.this);

		// Layout aus "list_optionsdialog.xml" einlesen.
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.list_optionsdialog, (ViewGroup) findViewById(R.id.shoppingList_optionsDialogLayout));
		builder.setView(layout);

		// Dialog erstellen und anzeigen.
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		// Größe des Dialog-Fensters festlegen.
		alertDialog.getWindow().setLayout(300, 600);

		// Daten aus Resource "strings.xml" laden.
		statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Spinner, welcher angibt nach welchem Status gefiltert werden soll.
		Spinner statusSpinner = (Spinner) layout.findViewById(R.id.listStatusSpinner);
		statusSpinner.setAdapter(statusAdapter);
		statusSpinner.setSelection(status);
		statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				// Feldvariable setzen damit aus der Methode getList() auf den
				// Wert zugegriffen werden kann.
				status = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		// Daten aus Resource "strings.xml" laden.
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Spinner, welcher angibt nach welcher Spalte sortiert werden soll.
		Spinner sortSpinner = (Spinner) layout.findViewById(R.id.shoppingListSortSpinner);
		sortSpinner.setAdapter(sortAdapter);
		sortSpinner.setSelection(sort);
		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				// Feldvariable setzen damit aus der Methode getList() auf den
				// Wert zugegriffen werden kann.
				sort = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		// Daten aus Resource "strings.xml" laden.
		directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Spinner, welcher angibt nach welcher Richtung sortiert werden soll.
		Spinner directionSpinner = (Spinner) layout.findViewById(R.id.listDirectionSpinner);
		directionSpinner.setAdapter(directionAdapter);
		directionSpinner.setSelection(direction);
		directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				// Feldvariable setzen damit aus der Methode getList() auf den
				// Wert zugegriffen werden kann.
				direction = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		// Button zum bestätigen.
		Button okButton = (Button) layout.findViewById(R.id.shoppingListOptionsDialogButton);
		okButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// Dialog schließen und die Liste neu laden.
				alertDialog.dismiss();
				getList();
			}
		});
	}
}
