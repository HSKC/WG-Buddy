package de.htwg.lpn.wgbuddy.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;
import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.User_Login;
import de.htwg.lpn.wgbuddy.Main;

public class Utilities 
{
	/** 
	 * Erster Buchstabe Groß-, sonst Kleinbuchstaben.
	 * @param string
	 * @return
	 */
	public static String getHighLowCharacter(String string)
	{
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
	
	/**
	 * Prüft, ob String nicht erlaubte Zeichen enthält.
	 * Erlaubte Zeichen:
	 * a-z A-Z 0-9
	 * @param string
	 * @return true, falls nur erlaubte Zeichen und false, falls Sonderzeichen vorhanden.
	 */
	public static Boolean checkOnlyAllowedCharacter(String string)
	{
		if(Pattern.matches("[a-zA-Z0-9]+", string))
		{
			return true;
		}
		
		return false;
	}
	
	public static String md5(String pw)
	{
	   byte[] defaultBytes = pw.getBytes();
	   
	   try 
	   {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
		    md.update(defaultBytes);
			byte messageDigest[] = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) 
			{
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
	   } 
	   catch (NoSuchAlgorithmException e) 
	   {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }	   
	   
	   return "";
	}
	
	public static void message(Context context, String text, String buttonText)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(text);
		builder.setCancelable(false);
		builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   dialog.cancel();
           }
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void toastMessage(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static void toastMessage(Context context, String text, int duration)
	{
		Toast.makeText(context, text, duration).show();
	}
	
	public static void message(final Context context, String text, String buttonText, final String link)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(text);
		builder.setCancelable(false);
		builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   dialog.cancel();
           }
		});
		
		builder.setNegativeButton("Handbuch", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
               context.startActivity(i);
           }
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	public static void checkByPass(Context context, SharedPreferences settings)
	{
		// Nicht eingeloggt.
        if(!settings.contains("user_status") || !settings.contains("user_id") || !settings.contains("user_name") || !settings.contains("user_email") || !settings.contains("user_password") || !settings.contains("wg_id") || !settings.contains("wg_name") || !settings.contains("wg_password"))
        {
        	// Umleiten
        	Intent intent = new Intent(context, User_Login.class);
			context.startActivity(intent);
			return;        	
        }	
	}
	
	public static void leaveWG(Context context, SharedPreferences settings, User user) 
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("0", ""))); 
		 user.update(Integer.valueOf(settings.getString("user_id", "")), nameValuePairs);
		  
		 SharedPreferences.Editor editor = settings.edit();      		    
		 editor.clear();
		 editor.commit();        		    
		 Intent intent = new Intent(context, Main.class);
		 context.startActivity(intent);
	}
	
	public static String getWGAdminId(SharedPreferences settings)
	{
		WG wg = new WG(settings);
		ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", ""));
		
		return wgList.get(0).get("adminId");
	}	
}
