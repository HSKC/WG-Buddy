package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import de.htwg.lpn.wgbuddy.Store;

public class ObjectBase
{
	protected Store store = null;
	protected String phpPage;
	protected String arrayName;
	
	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(store.getWebserver() + phpPage, arrayName);		
	}
	
	public ArrayList<HashMap<String, String>> get(String par)
	{
		String url = store.getWebserver() + phpPage + par;
		return JSON.getMapListOfJsonArray(url, arrayName);		
	}
	
	public void insert(List<NameValuePair> nameValuePairs)
	{
		String url = store.getWebserver() + phpPage + "?insert";
		JSON.postData(url, nameValuePairs);
	}
	
	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = store.getWebserver() + phpPage + "?update=" + String.valueOf(id);
		JSON.postData(url, nameValuePairs);
	}
	
	public void delete(Integer id)
	{
		String url = store.getWebserver() + phpPage + "?delete=" + String.valueOf(id);
		JSON.postData(url);
	}
}
