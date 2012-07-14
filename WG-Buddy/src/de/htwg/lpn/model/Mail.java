package de.htwg.lpn.model;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.SharedPreferences;
import de.htwg.lpn.wgbuddy.utility.Config;
import de.htwg.lpn.wgbuddy.utility.JSON;

/**
 * Stellt die Methoden f�r die E-Mail Versendung zur Verf�gung.
 */
public class Mail extends MethodBase
{
	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public Mail(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "mail.php";
		this.arrayName = "Mail";
	}

	/**
	 * Sendet an die angegebene E-Mail Adresse einen �nderungsschl�ssel.
	 * 
	 * @param email
	 *            E-Mail Adresse an die die E-Mail gesendet werden soll.
	 */
	public void sendChangeKey(String email)
	{
		String url = Config.WEBSERVER + phpPage + "?lost=" + email + "&" + Config.AUTH_CODE;
		JSON.postData(url);
	}

	/**
	 * Sendet an die angegebene E-Mail Adresse einen Aktivierungsschl�ssel.
	 * 
	 * @param email
	 *            E-Mail Adresse an die die E-Mail gesendet werden soll.
	 */
	public void sendActivateKey(String email)
	{
		String url = Config.WEBSERVER + phpPage + "?activate=" + email + "&" + Config.AUTH_CODE;
		JSON.postData(url);
	}

	/**
	 * Sendet and die angegebene E-Mail Adresse eine Einladung.
	 * 
	 * @param email
	 *            E-Mail Adresse an die die E-Mail gesendet werden soll.
	 */
	public void sendInvite(String email)
	{
		String url = Config.WEBSERVER + phpPage + "?invite=" + email + "&username=" + settings.getString("user_name", "") + "&wg=" + settings.getString("wg_name", "") + "&pw="
		+ settings.getString("wg_password", "") + "&" + Config.AUTH_CODE;
		JSON.postData(url);
	}

	/**
	 * Sendet dem angegebenen Benutzer eine E-Mail mit Informationen �ber die
	 * ihm zugeteilte Aufgabe.
	 * 
	 * @param nameValuePairs
	 *            Liste mit den Eintr�gen Benutzername, E-Mail Adresse,
	 *            Aufgabenname und Aufgabenbeschreibung.
	 */
	public void sendTask(List<NameValuePair> nameValuePairs)
	{
		String url = Config.WEBSERVER + phpPage + "?task&" + Config.AUTH_CODE;
		JSON.postData(url, nameValuePairs);
	}
}