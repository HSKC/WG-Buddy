package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden f�r die Benutzer-Daten zur Verf�gung. Die Daten werden
 * aus der entsprechenden Datenbanktabelle bezogen.
 */
public class User extends MethodBase
{
	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public User(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "user.php";
		this.arrayName = "User";
	}
}
