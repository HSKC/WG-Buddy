package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;

import de.htwg.lpn.wgbuddy.utility.JSON;

import android.content.Context;
import android.content.SharedPreferences;

public class ObjectBase
{
	protected SharedPreferences settings = null;
	protected String phpPage;
	protected String arrayName;
	protected String authCode = "authCode=42cfbce07625c322c037066183d5f5c9";

	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(settings.getString("pref_webserver", "") + phpPage + "?" + authCode, arrayName);
	}

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
		String url = settings.getString("pref_webserver", "") + phpPage + par;
		return JSON.getMapListOfJsonArray(url, arrayName);
	}

	public void insert(List<NameValuePair> nameValuePairs)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?insert&" + authCode;
		JSON.postData(url, nameValuePairs);
	}

	public void insert(List<NameValuePair> nameValuePairs, Context context)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?insert&" + authCode;
		JSON.postData(url, nameValuePairs, context);
	}

	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?update=" + String.valueOf(id) + "&" + authCode;
		JSON.postData(url, nameValuePairs);
	}

	public void delete(Integer id)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?delete=" + String.valueOf(id) + "&" + authCode;
		JSON.postData(url);
	}

	public void delete(Integer id, Context context)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?delete=" + String.valueOf(id) + "&" + authCode;
		JSON.postData(url, context);
	}
}
