package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden für den Nachrichten-Bereich zur Verfügung. Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class Message extends MethodBase
{
	/**
	 * Konstruktor setzt die benötigten Parameter für die spätere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enthält die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public Message(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "message.php";
		this.arrayName = "Message";
	}
}
