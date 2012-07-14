package de.htwg.lpn.wgbuddy.utility;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Asynchron zum Server eine Nachricht schicken und Empfangen.
 */
public class AsyncJsonCall extends AsyncTask<String, Void, InputStream>
{
	@Override
	protected InputStream doInBackground(String... params)
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(params[0]);
		// httppost.addHeader("Content-Length","0");
		InputStream is = null;
		HttpResponse response;
		try
		{
			// Antwort empfangen und verarbeiten.
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}
		catch (ClientProtocolException e)
		{
			Log.d("log_tag", "Error send and receive data " + e.toString() + e.getMessage());
		}
		catch (IOException e)
		{
			Log.d("log_tag", "Error send and receive data " + e.toString() + e.getMessage());
		}

		return is;
	}

}
