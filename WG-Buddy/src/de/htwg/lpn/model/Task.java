package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden f�r den Aufgaben-Bereich zur Verf�gung.  Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class Task extends ObjectBase
{
	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public Task(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "task.php";
		this.arrayName = "Task";
	}
}
