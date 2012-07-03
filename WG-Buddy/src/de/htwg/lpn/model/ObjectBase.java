package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;

import de.htwg.lpn.wgbuddy.utility.JSON;

import android.content.SharedPreferences;

public class ObjectBase
{
	protected SharedPreferences settings = null;
	protected String phpPage;
	protected String arrayName;
	
	public String getAuthString()
	{
		String username = settings.getString("user_name", "");
		String password = settings.getString("user_password", "");
		if(username == "" || password == "")
		{
			return "";
		}
		else
		{
			return "auth_username=" + settings.getString("user_name", "") + "&auth_password=" + settings.getString("user_password", "");
		}
	}
	
	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(settings.getString("pref_webserver", "") + phpPage + "?" + getAuthString() , arrayName);		
	}
	
	public ArrayList<HashMap<String, String>> get(String par)
	{
		String auth_string = getAuthString();
		if(auth_string != "")
		{
			if(par == "")
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
		String url = settings.getString("pref_webserver", "") + phpPage + "?insert&" + getAuthString();
		JSON.postData(url, nameValuePairs);
	}
	
	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?update=" + String.valueOf(id) + "&" + getAuthString();
		JSON.postData(url, nameValuePairs);
	}
	
	public void delete(Integer id)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?delete=" + String.valueOf(id) + "&" + getAuthString();
		JSON.postData(url);
	}
}
