package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.SharedPreferences;
import de.htwg.lpn.wgbuddy.utility.Config;
import de.htwg.lpn.wgbuddy.utility.JSON;

/**
 * Stellt die Methoden f�r den GoogleService zur Verf�gung.
 */
public class GoogleService extends MethodBase
{
	/**
	 * Konstruktor setzt die ben�tigten Parameter f�r die sp�tere Kommunkikation
	 * mit dem Server.
	 * 
	 * @param settings
	 *            Der Parameter enth�lt die in der gesamten Anwendungen
	 *            geteilten Einstellung und Daten.
	 */
	public GoogleService(SharedPreferences settings)
	{
		super(settings);
		this.phpPage = "googleService.php";
		this.arrayName = "GoogleService";
	}

	/**
	 * Diese Methode sendet eine Nachricht �ber den GooleService an alle Ger�te
	 * der WG.
	 * 
	 * @param message
	 *            Nachricht, die gesendet werden soll.
	 */
	public void sendMessageToPhone(String message)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
		nameValuePairs.add(new BasicNameValuePair("msgType", "collapsed"));
		nameValuePairs.add(new BasicNameValuePair("messageText", message));

		String url = Config.WEBSERVER + phpPage + "?" + Config.AUTH_CODE;
		JSON.postData(url, nameValuePairs);
	}

	/**
	 * Diese Methode l�sst sich vom GoolgleService einen neuen AuthenticateCode
	 * geben. Der neue Code wird in der DB gespeichert.
	 */
	public void googleAuthenticate()
	{
		String url = Config.WEBSERVER + phpPage + "?Authenticate&" + Config.AUTH_CODE;
		JSON.postData(url);
	}
}