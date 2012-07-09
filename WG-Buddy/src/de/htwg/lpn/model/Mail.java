package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.utility.JSON;
import android.content.SharedPreferences;

public class Mail extends ObjectBase
{
	public Mail(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "mail.php";
		arrayName = "Mail";
	}

	public void sendChangeKey(String email)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?lost=" + email + "&" + authCode;
		JSON.postData(url);
	}

	public void sendActivateKey(String email)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?activate=" + email + "&" + authCode;
		JSON.postData(url);
	}

	public void sendInvite(String email)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?invite=" + email + "&username=" + settings.getString("user_name", "") + "&wg=" + settings.getString("wg_name", "") + "&pw="
		+ settings.getString("wg_password", "") + "&" + authCode;
		JSON.postData(url);
	}

	public void sendTask(String id)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?task=" + id + "&" + authCode;
		JSON.postData(url);
	}
}