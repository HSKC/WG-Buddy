package de.htwg.lpn.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.htwg.lpn.wgbuddy.AsynJsonCall;
import de.htwg.lpn.wgbuddy.JSONStuff;

public class JSON 
{
	public List<NameValuePair> CreatePostFromClass(Object obj)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
        
		return nameValuePairs;
	}
	
	// Method from http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
	public static void postData(String url, List<NameValuePair> nameValuePairs) 
	{
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    try 
	    {       
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	    } 
	    catch (ClientProtocolException e) 
	    {
	        // TODO Auto-generated catch block
	    } 
	    catch (IOException e) 
	    {
	        // TODO Auto-generated catch block
	    }
	}
	
	public static ArrayList<HashMap<String, String>> getMapListOfJsonArray(String url, String arrayname)
	{
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		
		JSONObject json = JSONStuff.getJSONfromURL(url);
        JSONArray jsonarray = null;
        
        try 
        {
			jsonarray = json.getJSONArray(arrayname);
		} 
        catch (JSONException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i < jsonarray.length();i++)
		{						

        	HashMap<String, String> map = new HashMap<String, String>();
        	
			try 
			{
				JSONObject e = jsonarray.getJSONObject(i);
				Iterator iter = e.keys();
				while(iter.hasNext())
				{
					String key = (String)iter.next();
					String value = e.getString(key);
					map.put(key,value);
				}
	        	mylist.add(map);
			} 
			catch (JSONException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return mylist;
	}
	
	public static JSONObject getJSONfromURL(String url)
	{

		//initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		
		AsynJsonCall asc = new AsynJsonCall();
		
		asc.execute(url);
		
		try {
			is = asc.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		//try parse the string to a JSON object
		try{
	        	jArray = new JSONObject(result);
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString()+ e.getMessage());
		}

		return jArray;
	}
}
