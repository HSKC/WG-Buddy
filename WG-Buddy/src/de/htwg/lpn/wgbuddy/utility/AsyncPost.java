package de.htwg.lpn.wgbuddy.utility;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncPost extends AsyncTask<HttpPost, Void, String> 
{
	Context context;
	ProgressDialog mydialog;
	
	public AsyncPost(ProgressDialog dialog) 
	{
		mydialog = dialog;
	}
	
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
	
	@Override
	protected void onPostExecute(String s)
	{
		System.out.println("DISMISS");
		mydialog.dismiss();
	}
}
