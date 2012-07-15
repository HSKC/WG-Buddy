package de.htwg.lpn.wgbuddy.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Die Klasse beinhaltet alle Methoden um mit dem Server zu kommunizieren.
 */
public class JSON
{
	/**
	 * PHP-Skript aufrufen.
	 * 
	 * @param url
	 *            URL
	 */
	public static void postData(String url)
	{
		url = url.replace(" ", "");
		HttpPost httppost = new HttpPost(url);

		AsyncPost asp = new AsyncPost();

		asp.execute(httppost);
	}

	/**
	 * PHP-Skript aufrufen.
	 * 
	 * @param url
	 *            URL
	 * @param nameValuePairs
	 *            POST-Variable
	 */
	public static void postData(String url, List<NameValuePair> nameValuePairs)
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try
		{
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpclient.execute(httppost);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * JSON-Objekt mit Hilfe von URL anfordern und in ArrayList umwanderln.
	 * 
	 * @param url
	 *            URL
	 * @param arrayname
	 *            JSON-Objekt Name
	 * @return ArrayList<HashMap<String, String>> mit allen Einträgen und
	 *         Werten.
	 */
	public static ArrayList<HashMap<String, String>> getMapListOfJsonArray(String url, String arrayname)
	{
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		JSONObject json = getJSONfromURL(url);
		if (json != null)
		{
			JSONArray jsonarray = null;

			try
			{
				jsonarray = json.getJSONArray(arrayname);
			}
			catch (JSONException e)
			{
				Log.d("JSON.getMapListOfJsonArray()", e.getMessage());
				e.printStackTrace();
			}
			if (jsonarray != null)
			{
				for (int i = 0; i < jsonarray.length(); i++)
				{

					HashMap<String, String> map = new HashMap<String, String>();

					try
					{
						JSONObject e = jsonarray.getJSONObject(i);
						Iterator<?> iter = e.keys();
						while (iter.hasNext())
						{
							String key = (String) iter.next();
							String value = e.getString(key);
							map.put(key, value);
						}
						mylist.add(map);
					}
					catch (JSONException e1)
					{
						Log.d("JSON.getMapListOfJsonArray()", e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
		}
		return mylist;
	}

	/**
	 * JSON-Object mit Hilfe der URL anfordern.
	 * 
	 * @param url
	 *            URL
	 * @return JSONObject
	 */
	public static JSONObject getJSONfromURL(String url)
	{
		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		AsyncJsonCall asc = new AsyncJsonCall();

		asc.execute(url);

		try
		{
			is = asc.get();
		}
		catch (InterruptedException e)
		{
			Log.d("log_tag", "Error get data " + e.toString() + e.getMessage());
		}
		catch (ExecutionException e)
		{
			Log.d("log_tag", "Error get data " + e.toString() + e.getMessage());
		}

		// convert response to string
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		}
		catch (Exception e)
		{
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try
		{
			jArray = new JSONObject(result);
		}
		catch (JSONException e)
		{
			Log.e("log_tag", "Error parsing data " + e.toString() + e.getMessage());
		}

		return jArray;
	}
}
