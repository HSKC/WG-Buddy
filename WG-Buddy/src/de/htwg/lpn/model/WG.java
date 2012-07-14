package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden für die WG-Daten zur Verfügung. Die Daten werden aus der
 * entsprechenden Datenbanktabelle bezogen.
 */
public class WG extends MethodBase
{
	/**
	 * Konstruktor setzt die benötigten Parameter für die spätere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enthält die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public WG(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "wg.php";
		this.arrayName = "WG";
	}
}