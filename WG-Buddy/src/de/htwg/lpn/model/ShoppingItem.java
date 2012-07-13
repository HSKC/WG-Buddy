package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden f�r den Einkaufzettel-Bereich zur Verf�gung. Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class ShoppingItem extends ObjectBase
{
	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public ShoppingItem(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "shopping.php";
		this.arrayName = "Item";
	}
}
