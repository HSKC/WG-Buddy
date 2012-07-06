package de.htwg.lpn.model;

import android.content.SharedPreferences;
import de.htwg.lpn.wgbuddy.utility.JSON;

public class User extends ObjectBase
{
	public User(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "user.php";
		arrayName = "User";
	}
	
	public void sendChangeKey(String email)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?lost=" + email + "&" + authCode;
		JSON.postData(url);
	}
	
	public void sendActivateKey(String email)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?activate=" + email + "&" + authCode;
		JSON.postData(url);
	}
	
	public void sendInvite(String email)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?invite=" + email + "&username=" + settings.getString("user_name", "") + "&wg=" + settings.getString("wg_name", "") + "&pw=" + settings.getString("wg_password", "") + "&" + authCode;
		JSON.postData(url);
	}
	
	public void sendTask(String id)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?task=" + id + "&" + authCode;
		JSON.postData(url);
	}

}
