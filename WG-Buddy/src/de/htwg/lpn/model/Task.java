package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden für den Aufgaben-Bereich zur Verfügung.  Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class Task extends ObjectBase
{
	/**
	 * Konstruktor setzt die benötigten Parameter für die spätere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enthält die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public Task(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "task.php";
		this.arrayName = "Task";
	}
}
