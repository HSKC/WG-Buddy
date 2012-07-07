package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.wgbuddy.utility.JSON;
import android.content.SharedPreferences;

public class GoogleService extends ObjectBase
{
	public GoogleService(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "googleService.php";
		arrayName = "GoogleService";
	}
	
	public void sendMessageToPhone(String message)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
        nameValuePairs.add(new BasicNameValuePair("msgType", "collapsed"));
        nameValuePairs.add(new BasicNameValuePair("messageText", message));
		
		String url = settings.getString("pref_webserver", "") + phpPage + "?" + authCode;
		JSON.postData(url, nameValuePairs);
	}
	
	public void googleAuthenticate()
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?Authenticate&" + authCode;
		JSON.postData(url);
	}
}