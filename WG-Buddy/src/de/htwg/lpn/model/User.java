package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.utility.JSON;
import android.content.SharedPreferences;

public class User extends ObjectBase
{
	public User(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "user.php";
		arrayName = "User";
	}
	
	public void getNewKey(String email)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?lost=" + email;
		JSON.postData(url);
	}
	
	public void sendTask(String id)
	{
		String url = settings.getString("pref_webserver", "") + "mail.php?task=" + id;
		JSON.postData(url);
	}
}
