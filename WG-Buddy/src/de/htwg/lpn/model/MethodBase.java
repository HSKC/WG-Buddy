package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.SharedPreferences;
import de.htwg.lpn.wgbuddy.utility.JSON;

/**
 * Implementiert die Methoden zur Kommunikation mit dem Server und der Datenbank
 * allgemein.
 * 
 */
public class MethodBase
{
	protected SharedPreferences settings = null;
	protected String phpPage;
	protected String arrayName;

	protected final static String webserver = "http://wgbuddy.domoprojekt.de/";
	protected final static String authCode = "authCode=42cfbce07625c322c037066183d5f5c9";

	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public MethodBase(SharedPreferences settings)
	{
		this.settings = settings;
	}

	/**
	 * @return the webserver
	 */
	public static String getWebserver()
	{
		return webserver;
	}

	/**
	 * @return the authcode
	 */
	public static String getAuthcode()
	{
		return authCode;
	}

	/**
	 * Diese Methode liefert alle Eintr�ge der entsprechenden Datenbanktabelle.
	 * 
	 * @return Eine Liste aller Tabelleeintr�ge.
	 */
	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(webserver + phpPage + "?" + authCode, arrayName);
	}

	/**
	 * Diese Methode liefert alle Eintr�ge der entsprechenden Datenbanktabelle,
	 * die mit den angebenen Parametern �bereinstimmen.
	 * 
	 * @param par
	 *            Mit diesem String k�nnen die Eintr�ge gefiltert und sortiert
	 *            werden.
	 * @return Eine Liste aller Tabelleeintr�ge.
	 */
	public ArrayList<HashMap<String, String>> get(String par)
	{
		String auth_string = authCode;
		if (auth_string != "")
		{
			if (par == "")
			{
				par = "?" + auth_string;
			}
			else
			{
				par += "&" + auth_string;
			}
		}
		String url = webserver + phpPage + par;
		return JSON.getMapListOfJsonArray(url, arrayName);
	}

	/**
	 * Speichert die in dem Parameter �bergebenen Werte in einem neuen Eintrag
	 * in der Datenbank.
	 * 
	 * @param nameValuePairs
	 *            Liste aller Werte die der Eintrag enthalten soll.
	 */
	public void insert(List<NameValuePair> nameValuePairs)
	{
		String url = webserver + phpPage + "?insert&" + authCode;
		JSON.postData(url, nameValuePairs);
	}

	/**
	 * �ndert die angebenen Werte des Eintrags mit der angegbenen ID.
	 * 
	 * @param id
	 *            ID des zu �ndernden Eintrags.
	 * @param nameValuePairs
	 *            Werte die ge�ndert werden sollen.
	 */
	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = webserver + phpPage + "?update=" + String.valueOf(id) + "&" + authCode;
		JSON.postData(url, nameValuePairs);
	}

	/**
	 * L�scht den Eintrag mit der angegbenen ID aus der Datenbank.
	 * 
	 * @param id
	 *            Id des zu l�schenden Eintrags.
	 */
	public void delete(Integer id)
	{
		String url = webserver + phpPage + "?delete=" + String.valueOf(id) + "&" + authCode;
		JSON.postData(url);
	}
}
