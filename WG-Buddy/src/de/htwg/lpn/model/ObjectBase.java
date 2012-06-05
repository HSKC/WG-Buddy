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
	
	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(settings.getString("pref_webserver", "") + phpPage, arrayName);		
	}
	
	public ArrayList<HashMap<String, String>> get(String par)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + par;
		return JSON.getMapListOfJsonArray(url, arrayName);		
	}
	
	public void insert(List<NameValuePair> nameValuePairs)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?insert";
		JSON.postData(url, nameValuePairs);
	}
	
	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?update=" + String.valueOf(id);
		JSON.postData(url, nameValuePairs);
	}
	
	public void delete(Integer id)
	{
		String url = settings.getString("pref_webserver", "") + phpPage + "?delete=" + String.valueOf(id);
		JSON.postData(url);
	}
}
