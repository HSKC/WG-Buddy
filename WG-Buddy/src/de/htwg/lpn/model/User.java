package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden für die Benutzer-Daten zur Verfügung. Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class User extends ObjectBase
{
	/**
	 * Konstruktor setzt die benötigten Parameter für die spätere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enthält die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public User(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "user.php";
		this.arrayName = "User";
	}
}
