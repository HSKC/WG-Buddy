package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Message;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

/**
 * Activity-Klasse der Ansicht zum Erstellen von Nachrichten.
 */
public class Message_Create extends Activity
{
	private SharedPreferences settings = null;
	private Button sendButton;
	private EditText titleEditText;
	private EditText messageEditText;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_create);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		sendButton = (Button) findViewById(R.id.messagecreate_sendButton);
		titleEditText = (EditText) findViewById(R.id.messagecreate_titleEdit);
		messageEditText = (EditText) findViewById(R.id.messagecreate_messageEdit);
		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		sendButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Eingaben aus dem EditText-View laden und Leerzeichen am
				// Anfang und Ende abschneiden.
				final String title = titleEditText.getText().toString().trim();
				final String message = messageEditText.getText().toString().trim();

				// Prüfen, ob Eingaben gemacht wurden.
				if (title.compareTo("") != 0 && message.compareTo("") != 0)
				{
					// Warte-Dialog anzeigen.
					ProgressDialog pd = ProgressDialog.show(Message_Create.this, "", getString(R.string.utilities_pleaseWait));

					// Handler, welcher nach dem Bearbeiten die Hintergrund-Task
					// ausgeführt wird.
					Handler handler = new Handler()
					{
						@Override
						public void handleMessage(android.os.Message msg)
						{
							super.handleMessage(msg);

							Utilities.toastMessage(Message_Create.this, getString(R.string.message_created));

							// Zurück zur Nachrichten-Ansicht.
							Intent intent = new Intent(Message_Create.this, Message_List.class);
							startActivity(intent);
						}
					};

					// Code der in der Hintergrund-Task ausgeführt werden soll.
					Callable<android.os.Message> callable = new Callable<android.os.Message>()
					{
						@Override
						public android.os.Message call() throws Exception
						{
							// Werte für die Übertragung zur Datenbank
							// vorbereiten.
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
							nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "0")));

							nameValuePairs.add(new BasicNameValuePair("title", title));
							nameValuePairs.add(new BasicNameValuePair("message", message));

							// Daten in die Datenbank eintragen.
							Message message = new Message(settings);
							message.insert(nameValuePairs);

							// Alle Geräte der WG mit Hilfe des GoogleService
							// über die Änderung informieren.
							if (Main.usepush)
							{
								GoogleService gs = new GoogleService(settings);
								gs.sendMessageToPhone("Message");
							}

							return android.os.Message.obtain();
						}

					};

					// Hintergrund Thread starten.
					WorkerThread workerThread = new WorkerThread(callable, pd, handler);
					workerThread.start();
				}
				else
				{
					Utilities.toastMessage(Message_Create.this, getString(R.string.utilities_allFields));
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
				Dialogs.getAboutDialog(Message_Create.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Message_Create.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
