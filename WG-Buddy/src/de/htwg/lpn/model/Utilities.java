package de.htwg.lpn.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utilities 
{
	public static String getStringFormat(String string)
	{
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
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
}
