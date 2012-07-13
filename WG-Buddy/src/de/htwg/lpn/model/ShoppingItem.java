package de.htwg.lpn.model;

import android.content.SharedPreferences;

/**
 * Stellt die Methoden für den Einkaufzettel-Bereich zur Verfügung. Die Daten
 * werden aus der entsprechenden Datenbanktabelle bezogen.
 */
public class ShoppingItem extends ObjectBase
{
	/**
	 * Konstruktor setzt die benötigten Parameter für die spätere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enthält die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public ShoppingItem(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "shopping.php";
		this.arrayName = "Item";
	}
}
