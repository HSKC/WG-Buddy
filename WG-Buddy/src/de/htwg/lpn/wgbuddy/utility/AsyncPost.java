package de.htwg.lpn.wgbuddy.utility;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncPost extends AsyncTask<HttpPost, Void, String> 
{
	Context context;
	
//	@Override
//	protected void onPreExecute() 
//	{
//		System.out.println("SHOW2");
//		mydialog.setTitle("Please wait");
//		mydialog.show();          
//	}
	

	@Override
	protected String doInBackground(HttpPost... params) 
	{
		HttpClient httpclient = new DefaultHttpClient();

		try 
		{
			 httpclient.execute(params[0]);

		} 
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
