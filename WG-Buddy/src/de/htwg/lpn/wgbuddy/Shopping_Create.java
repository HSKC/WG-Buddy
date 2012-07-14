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
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Shopping;
import de.htwg.lpn.wgbuddy.utility.Config;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

/**
 * Activity-Klasse der Ansicht zum Erstellen von Einkaufsartikeln.
 */
public class Shopping_Create extends Activity
{
	private SharedPreferences settings = null;

	private Button addButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private RatingBar ratingBar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_create);

		// Die allgemeinen Anwendungsdaten laden.
		settings = getSharedPreferences(Config.PREFS_NAME, 0);

		// Prüfen ob der Benutzer eingeloggt ist und ggf. in die Login-Ansicht
		// umleiten.
		Utilities.checkByPass(this, settings);

		// Alle Views dieser Ansicht den entsprechenden Feldern zuweisen.
		addButton = (Button) findViewById(R.id.shoppingItemAddButton);
		nameEditText = (EditText) findViewById(R.id.ShoppingItemNameEditText);
		descriptionEditText = (EditText) findViewById(R.id.ShoppingDescriptionEditText);
		ratingBar = (RatingBar) findViewById(R.id.shoppingItemRatingBar);

		addButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Eingaben aus dem EditText-View laden und Leerzeichen am
				// Anfang und Ende abschneiden.
				final String name = nameEditText.getText().toString().trim();
				final String comment = descriptionEditText.getText().toString().trim();

				// Prüfen, ob Eingaben gemacht wurden.
				if (name.compareTo("") != 0 && comment.compareTo("") != 0)
				{
					// Warte-Dialog anzeigen.
					ProgressDialog pd = ProgressDialog.show(Shopping_Create.this, "", getString(R.string.utilities_pleaseWait));

					// Handler, welcher nach dem Bearbeiten die Hintergrund-Task
					// ausgeführt wird.
					Handler handler = new Handler()
					{
						@Override
						public void handleMessage(Message msg)
						{
							super.handleMessage(msg);

							Utilities.toastMessage(Shopping_Create.this, getString(R.string.shopping_created));

							// Zurück zur Einkaufszettel-Ansicht.
							Intent intent = new Intent(Shopping_Create.this, Shopping_List.class);
							startActivity(intent);
						}
					};

					// Code der in der Hintergrund-Task ausgeführt werden soll.
					Callable<Message> callable = new Callable<Message>()
					{
						@Override
						public Message call() throws Exception
						{
							// Werte für die Übertragung zur Datenbank
							// vorbereiten.
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
							nameValuePairs.add(new BasicNameValuePair("userId", "0"));
							nameValuePairs.add(new BasicNameValuePair("name", name));
							nameValuePairs.add(new BasicNameValuePair("comment", comment));
							nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(ratingBar.getRating())));
							nameValuePairs.add(new BasicNameValuePair("status", "0"));

							// Daten in die Datenbank eintragen.
							Shopping shopping = new Shopping(settings);
							shopping.insert(nameValuePairs);

							// Alle Geräte der WG mit Hilfe des GoogleService
							// über die Änderung informieren.
							if (Config.USE_PUSH)
							{
								GoogleService gs = new GoogleService(settings);
								gs.sendMessageToPhone("Shopping");
							}

							return Message.obtain();
						}

					};

					// Hintergrund Thread starten.
					WorkerThread workerThread = new WorkerThread(callable, pd, handler);
					workerThread.start();
				}
				else
				{
					Utilities.toastMessage(Shopping_Create.this, getString(R.string.utilities_allFields));
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
				Dialogs.getAboutDialog(Shopping_Create.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Shopping_Create.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
