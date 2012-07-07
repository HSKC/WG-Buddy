package de.htwg.lpn.wgbuddy.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.Messenger;
import de.htwg.lpn.wgbuddy.ShoppingList;
import de.htwg.lpn.wgbuddy.TaskList;
import de.htwg.lpn.wgbuddy.WGBuddyActivity;

public class C2dmReceiver  extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		System.out.println("Received Async Message");
	    if(WGBuddyActivity.usepush)
	    {
			if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) 
			{
		        handleRegistration(context, intent);
		    } 
			else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) 
		    {
		        handleMessage(context, intent);
		    }
	    }
	}
	
	private void handleRegistration(Context context, Intent intent) 
	{
	    String registration = intent.getStringExtra("registration_id");
	    if (intent.getStringExtra("error") != null) 
	    {
	        // Registration failed, should try again later.
		    Log.d("c2dm", "registration failed");
		    String error = intent.getStringExtra("error");
		    if(error == "SERVICE_NOT_AVAILABLE")
		    {
		    	Log.d("c2dm", "SERVICE_NOT_AVAILABLE");
		    }
		    else if(error == "ACCOUNT_MISSING")
		    {
		    	Log.d("c2dm", "ACCOUNT_MISSING");
		    }
		    else if(error == "AUTHENTICATION_FAILED")
		    {
		    	Log.d("c2dm", "AUTHENTICATION_FAILED");
		    }
		    else if(error == "TOO_MANY_REGISTRATIONS")
		    {
		    	Log.d("c2dm", "TOO_MANY_REGISTRATIONS");
		    }
		    else if(error == "INVALID_SENDER")
		    {
		    	Log.d("c2dm", "INVALID_SENDER");
		    }
		    else if(error == "PHONE_REGISTRATION_ERROR")
		    {
		    	Log.d("c2dm", "PHONE_REGISTRATION_ERROR");
		    }
	    } 
	    else if (intent.getStringExtra("unregistered") != null) 
	    {
	        // unregistration done, new messages from the authorized sender will be rejected
	    	Log.d("c2dm", "unregistered");
	
	    } 
	    else if (registration != null) 
	    {
	    	Log.d("c2dm", registration);	
	    	Editor editor =context.getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0).edit();
	        editor.putString("registrationKey", registration);
	        editor.putLong("registrationKeydate", new Date().getTime());
			editor.commit();
			
			SharedPreferences settings = context.getApplicationContext().getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
			
			User user = new User(settings);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("android_key", registration));
			
			user.update(Integer.parseInt(settings.getString("user_id", "0")), nameValuePairs);
		    
	    }
	}
	
	private void handleMessage(Context context, Intent intent)
	{
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        
        System.out.println((ar.topActivity).getClassName().toString());

        System.out.println(ShoppingList.class.toString());
        System.out.println(WGBuddyActivity.class.toString());
        
        String messagetype = intent.getStringExtra("message");
        
        NotificationManager notMan = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        Notification notification = null;
        PendingIntent contentIntent = null;
        CharSequence contentTitle = null;
        CharSequence contentText = null;
        
        if(messagetype.equals("ShoppingItem"))
        {
        	notification = new Notification(android.R.drawable.stat_notify_chat,  "Einkaufsliste wurde geupdated",System.currentTimeMillis());
        	contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,new Intent(context.getApplicationContext(),ShoppingList.class),PendingIntent.FLAG_UPDATE_CURRENT);     	
            contentTitle = "WG Buddy";
            contentText = "Einkaufsliste wurde geupdated";
        }
        else if(messagetype.equals("Task"))
        {
        	notification = new Notification(android.R.drawable.stat_notify_chat,  "Aufgaben wurden geupdated",System.currentTimeMillis());
        	contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,new Intent(context.getApplicationContext(), TaskList.class),PendingIntent.FLAG_UPDATE_CURRENT);     	
            contentTitle = "WG Buddy";
            contentText = "Aufgaben wurden geupdated";
        }        
        else if(messagetype.equals("Message"))
        {
        	notification = new Notification(android.R.drawable.stat_notify_chat,  "Es sind neue Nachrichten im Chat",System.currentTimeMillis());
        	contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,new Intent(context.getApplicationContext(),Messenger.class),PendingIntent.FLAG_UPDATE_CURRENT);     	
            contentTitle = "WG Buddy";
            contentText = "Es sind neue Nachrichten im Chat";
        }

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        
        notMan.notify(0, notification);
        
        if(ar.topActivity.getClassName().toString().compareTo(ShoppingList.class.toString().split(" ")[1]) == 0)
        {
        	  Intent intent2 = new Intent(context,ShoppingList.class);
              intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent2);
        }
        else if(ar.topActivity.getClassName().toString().compareTo(TaskList.class.toString().split(" ")[1]) == 0)
        {
        	  Intent intent2 = new Intent(context, TaskList.class);
              intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent2);
        }
        else if(ar.topActivity.getClassName().toString().compareTo(Messenger.class.toString().split(" ")[1]) == 0)
        {
        	  Intent intent2 = new Intent(context,Messenger.class);
              intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent2);
        }
	}
}
