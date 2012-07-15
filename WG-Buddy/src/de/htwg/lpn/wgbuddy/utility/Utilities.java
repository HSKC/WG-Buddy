package de.htwg.lpn.wgbuddy.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.Main;
import de.htwg.lpn.wgbuddy.R;
import de.htwg.lpn.wgbuddy.User_Login;

public class Utilities
{
	/**
	 * Erster Buchstabe Groß-, sonst Kleinbuchstaben.
	 * 
	 * @param string
	 * @return
	 */
	public static String getHighLowCharacter(String string)
	{
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}

	/**
	 * Prüft, ob String nicht erlaubte Zeichen enthält. Erlaubte Zeichen: a-z
	 * A-Z 0-9
	 * 
	 * @param string
	 * @return true, falls nur erlaubte Zeichen und false, falls Sonderzeichen
	 *         vorhanden.
	 */
	public static Boolean checkOnlyAllowedCharacter(String string)
	{
		if (Pattern.matches("[a-zA-Z0-9]+", string))
		{
			return true;
		}

		return false;
	}

	/**
	 * MD5-Hashfunktion
	 * 
	 * @param pw
	 *            Passwort, auf das die Hashfunktion ausgeführt werden soll.
	 * @return Ergebnis der Hashfunktion
	 */
	public static String md5(String pw)
	{
		byte[] defaultBytes = pw.getBytes();

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(defaultBytes);
			byte messageDigest[] = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
			{
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.d("log_tag", "Error md5 " + e.toString() + e.getMessage());
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Toast-Message anzeigen.
	 * 
	 * @param context
	 *            Aktueller Kontext.
	 * @param text
	 *            Text, welcher angezeigt werden soll.
	 */
	public static void toastMessage(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast-Message anzeigen.
	 * 
	 * @param context
	 *            Aktueller Kontext.
	 * @param text
	 *            Text, welcher angezeigt werden soll.
	 * @param duration
	 *            Dauer der Anzeige.
	 */
	public static void toastMessage(Context context, String text, int duration)
	{
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * Prüfen, ob User eingeloggt ist und ggf. umgeleitet werden muss.
	 * 
	 * @param context
	 *            Aktueller Kontext
	 * @param settings
	 *            SharedPreferences
	 */
	public static void checkByPass(Context context, SharedPreferences settings)
	{
		// Nicht eingeloggt.
		if (!settings.contains("user_status") || !settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password")
		|| !settings.contains("wg_id") || !settings.contains("wg_name") || !settings.contains("wg_password"))
		{
			// Umleiten
			Intent intent = new Intent(context, User_Login.class);
			context.startActivity(intent);
			return;
		}
	}

	/**
	 * User verlässt, die WG. Punkte werden auf 0 gesetzt.
	 * 
	 * @param context
	 *            Aktueller Kontext
	 * @param settings
	 *            SharedPreferences
	 * @param user
	 *            User-Instanz
	 */
	public static void leaveWG(Context context, SharedPreferences settings, User user)
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("0", "")));
		nameValuePairs.add(new BasicNameValuePair("points", "0"));
		user.update(Integer.valueOf(settings.getString("user_id", "")), nameValuePairs);

		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		Utilities.toastMessage(context, context.getString(R.string.utilities_leaveWGSucceed));

		Intent intent = new Intent(context, Main.class);
		context.startActivity(intent);
	}

	/**
	 * Admin-Name anhand der WG-ID ermitteln
	 * 
	 * @param settings
	 *            SharedPreferences
	 * @return Admin-Name als String
	 */
	public static String getWGAdminId(SharedPreferences settings)
	{
		WG wg = new WG(settings);
		ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", ""));

		return wgList.get(0).get("adminId");
	}

	/**
	 * Alle Daten des Users anhand seines Namens bestimmen. Username ist
	 * eindeutig.
	 * 
	 * @param settings
	 *            SharedPreferences
	 * @param username
	 *            Username
	 * @return HashMap mit allen Daten des Users.
	 */
	public static HashMap<String, String> getUserWithName(SharedPreferences settings, String username)
	{
		User user = new User(settings);
		return user.get("?username=" + username).get(0);
	}

	/**
	 * Datum und Zeit in ein bestimmtes Format umwandeln.
	 * 
	 * @param data
	 *            Standardformat.
	 * @return Neues Format.
	 */
	public static String getDateTimeFormatForMessageList(String data)
	{
		// Change Dateformat
		String datum = data;
		String time = datum.split(" ")[1];
		datum = datum.split(" ")[0];
		return time + "\n" + datum.split("-")[2] + "." + datum.split("-")[1] + "." + datum.split("-")[0];
	}

	/**
	 * Datum und Zeit in ein bestimmtes Format umwandeln.
	 * 
	 * @param data
	 *            Standardformat.
	 * @return Neues Format.
	 */
	public static String getDateTimeFormat(String data)
	{
		// Change Dateformat
		String datum = data;
		String time = datum.split(" ")[1];
		datum = datum.split(" ")[0];
		return datum.split("-")[2] + "." + datum.split("-")[1] + "." + datum.split("-")[0] + " " + time;
	}

	/**
	 * Den Benutzernamen aus der User-Id bestimmen und anschließend der
	 * entsprechenden Map in der Liste hinzufügen.
	 * 
	 * @param list
	 *            Liste an Daten. Z.B. Nachrichten oder Aufgaben
	 * @param mapList
	 *            Diese MapList muss zusätzlich zu der Liste von der Methode
	 *            verwaltet werden.
	 * @param settings
	 *            SharedPreferences
	 */
	public static void addUsernameToList(ArrayList<HashMap<String, String>> list, HashMap<Integer, HashMap<String, String>> mapList, SharedPreferences settings)
	{
		User user = new User(settings);
		ArrayList<HashMap<String, String>> userListData = user.get("?wgId=" + settings.getString("wg_id", ""));
		HashMap<Integer, HashMap<String, String>> userListWithIndex = new HashMap<Integer, HashMap<String, String>>();
		for (HashMap<String, String> map : userListData)
		{
			userListWithIndex.put(Integer.valueOf(map.get("id")), map);
		}

		mapList.clear();

		for (HashMap<String, String> map : list)
		{
			mapList.put(Integer.valueOf(map.get("id")), map);
			if (map.get("userId").equals("0"))
			{
				map.put("username", "Keiner");
			}
			else
			{
				HashMap<String, String> userMap = userListWithIndex.get(Integer.valueOf(map.get("userId")));
				map.put("username", userMap.get("username"));
			}
		}
	}
}
