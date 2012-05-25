package de.htwg.lpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.http.NameValuePair;

public class ShoppingItem
{
	private WGBuddy context = null;
	private String phpPage;
	private String arrayName;
	
	public ShoppingItem(WGBuddy context)
	{
		this.context = context;
		phpPage = "shopping.php";
		arrayName = "Item";
	}
	
	
	public ArrayList<HashMap<String, String>> get()
	{
		return JSON.getMapListOfJsonArray(context.getWebserver() + phpPage, arrayName);		
	}
	
	public ArrayList<HashMap<String, String>> get(String par)
	{
		String url = context.getWebserver() + phpPage + par;
		return JSON.getMapListOfJsonArray(url, arrayName);		
	}
	
	public void insert(List<NameValuePair> nameValuePairs)
	{
		String url = context.getWebserver() + phpPage + "?insert";
		JSON.postData(url, nameValuePairs);
	}
	
	public void update(Integer id, List<NameValuePair> nameValuePairs)
	{
		String url = context.getWebserver() + phpPage + "?update=" + String.valueOf(id);
		JSON.postData(url, nameValuePairs);
	}
	
	public void delete(Integer id)
	{
		String url = context.getWebserver() + phpPage + "?delete=" + String.valueOf(id);
		JSON.postData(url);
	}
}
