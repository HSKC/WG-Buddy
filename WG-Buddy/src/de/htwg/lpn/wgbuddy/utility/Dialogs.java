package de.htwg.lpn.wgbuddy.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class Dialogs 
{
	public static void getActivateAccountDialog(final Context context, final SharedPreferences settings) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final EditText editText = new EditText(context);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint("Schlüssel");
		
		builder.setMessage("Der Account wurde noch nicht aktiviert. Bitte geben Sie den Schlüssel ein, welcher Ihnen per E-Mail zugeschickt wurde.");
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton("Account aktivieren", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   	String key = Utilities.md5(editText.getText().toString().trim());
	        	   			
   			if(key.length() <= 0)
   			{
   				Utilities.message(context, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
   				return;
   			}
   			
   			User user = new User(settings);
   			ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

   			if(userList.size() <= 0)
   			{
   				Utilities.message(context, "Schlüssel falsch.", "OK");	//TODO :string.xml
   				return;
   			}
   			
   			for(HashMap<String, String> userEntry : userList)
   			{
//   				Date changeTimestamp = null;
//   				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//   	            try 
//   	            {
//						changeTimestamp = sdfToDate.parse(userEntry.get("changeTimestamp"));
//					} 
//   	            catch (ParseException e) 
//   	            {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//   				
//   	            if(changeTimestamp == null)
//   	            {
//   	            	Utilities.message(Preferences_User.this, "Leider ist ein unerwarteter Fehler aufgetreten.", "OK");	//TODO :string.xml
//       				return;
//   	            }
//   	            
//       			Date now = new Date();
//       			
//       			Calendar calendar = new GregorianCalendar();
//       			calendar.setTime(changeTimestamp);
//
//       			// Add 3 days
//       			calendar.add(Calendar.DAY_OF_MONTH, 3);
//       			
//       			// Falls der Schlüssel älter als drei Tage ist.
//       			if(calendar.getTime().getTime() < now.getTime())
//       			{
//       				Utilities.message(Preferences_User.this, "Kein gültiger Schlüssel. Möglicherweise ist dieser zu alt. Sie können jedoch einen neuen anfordern.", "OK");	//TODO :string.xml
//       				return;
//       			}
   				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    			nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
    			nameValuePairs.add(new BasicNameValuePair("changeTimestamp", ""));
				user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);
   				}
       		}
		});
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void getLostPasswordDialog(final Context context, final SharedPreferences settings) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final EditText editText = new EditText(context);
		editText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		editText.setHint("E-Mail");
		
		builder.setMessage("Bitte gebe deine E-Mail Adresse ein. Ein zufällig generierter  Schlüssel wird dann an deine E-Mail Adresse geschickt.");
		builder.setCancelable(true);
		builder.setView(editText);
		builder.setPositiveButton("Schlüssel anfordern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   User user = new User(settings);
        	   user.getNewKey(editText.getText().toString());
        	   
        	   getChangePasswordWithKeyDialog(context, settings);
           }
		});
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void getChangePasswordWithKeyDialog(final Context context, final SharedPreferences settings) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);		
		final EditText password2EditText = new EditText(context);
		
		keyEditText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		keyEditText.setHint("Schlüssel");
		
		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint("Passwort");
		
		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint("Passwort wiederholen");
		
		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);
		
		builder.setMessage("Geben Sie bitte entweder den Ihnen zugeschickten Schlüssel, sowie ein neues Passwort ein.");
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton("Passwort ändern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {		        	   
	        	String key = keyEditText.getText().toString().trim();
	        	String password = Utilities.md5(passwordEditText.getText().toString().trim());
	        	String password2 = Utilities.md5(password2EditText.getText().toString().trim());
    			
    			if(key.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
    			{
    				Utilities.message(context, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			User user = new User(settings);
    			ArrayList<HashMap<String, String>> userList = user.get("?changeKey=" + key);

    			if(userList.size() <= 0)
    			{
    				Utilities.message(context, "Schlüssel falsch.", "OK");	//TODO :string.xml
    				return;		
    			}
    			
    			for(HashMap<String, String> userEntry : userList)
    			{
//    				Date changeTimestamp = null;
//    				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//    	            try 
//    	            {
//						changeTimestamp = sdfToDate.parse(userEntry.get("changeTimestamp"));
//					} 
//    	            catch (ParseException e) 
//    	            {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    				
//    	            if(changeTimestamp == null)
//    	            {
//    	            	Utilities.message(Preferences_User.this, "Leider ist ein unerwarteter Fehler aufgetreten.", "OK");	//TODO :string.xml
//        				return;
//    	            }
//    	            
//        			Date now = new Date();
//        			
//        			Calendar calendar = new GregorianCalendar();
//        			calendar.setTime(changeTimestamp);
//
//        			// Add 3 days
//        			calendar.add(Calendar.DAY_OF_MONTH, 3);
//        			
//        			// Falls der Schlüssel älter als drei Tage ist.
//        			if(calendar.getTime().getTime() < now.getTime())
//        			{
//        				Utilities.message(Preferences_User.this, "Kein gültiger Schlüssel. Möglicherweise ist dieser zu alt. Sie können jedoch einen neuen anfordern.", "OK");	//TODO :string.xml
//        				return;
//        			}
        			
        			if(!password.equals(password2))
        			{
        				Utilities.message(context, "Passwörter stimmen nicht überein.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        			nameValuePairs.add(new BasicNameValuePair("password", password));
        			nameValuePairs.add(new BasicNameValuePair("changeKey", ""));
        			nameValuePairs.add(new BasicNameValuePair("changeTimestamp", ""));
    				user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);
    				
    				Utilities.message(context, "Passwort wurde erfolgreich geändert.", "OK");
    			}
           }
		});
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void getChangePasswordDialog(final Context context, final SharedPreferences settings) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);		
		final EditText password2EditText = new EditText(context);
		
		keyEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		keyEditText.setHint("Altes Passwort");
		
		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint("Passwort");
		
		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint("Passwort wiederholen");
		
		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);
		
		builder.setMessage("Geben Sie bitte ihr altes Password, sowie ein neues Passwort ein.");
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton("Passwort ändern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {		        	   
	        	String key = keyEditText.getText().toString().trim();
	        	String oldPassword = Utilities.md5(keyEditText.getText().toString().trim());
	        	String password = Utilities.md5(passwordEditText.getText().toString().trim());
	        	String password2 = Utilities.md5(password2EditText.getText().toString().trim());
    			
    			if(key.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
    			{
    				Utilities.message(context, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			User user = new User(settings);
    			ArrayList<HashMap<String, String>> userList = user.get("?id=" + settings.getString("user_id", "") + "&password=" + oldPassword);				
				
				if(userList.size() <= 0)
    			{
    				Utilities.message(context, "Passwort falsch.", "OK");	//TODO :string.xml
    				return;
    			}    					
    			
    			
    			for(HashMap<String, String> userEntry : userList)
    			{
        			if(!password.equals(password2))
        			{
        				Utilities.message(context, "Passwörter stimmen nicht überein.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        			nameValuePairs.add(new BasicNameValuePair("password", password));
    				user.update(Integer.valueOf(userEntry.get("id")), nameValuePairs);
    				
    				SharedPreferences.Editor editor = settings.edit();
    		        editor.putString("user_password", password);
    		        editor.commit();
    		        
    		        Utilities.message(context, "Passwort wurde erfolgreich geändert.", "OK");
    			}
           }
		});
		
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void getChangeWGPasswordDialog(final Context context, final SharedPreferences settings) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final EditText keyEditText = new EditText(context);
		final EditText passwordEditText = new EditText(context);		
		final EditText password2EditText = new EditText(context);
		
		keyEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		keyEditText.setHint("Altes Passwort");
		
		passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEditText.setHint("Passwort");
		
		password2EditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		password2EditText.setHint("Passwort wiederholen");
		
		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(keyEditText);
		layout.addView(passwordEditText);
		layout.addView(password2EditText);
		
		builder.setMessage("Geben Sie bitte das alte WG Passwort, sowie ein neues WG Passwort ein.");
		builder.setCancelable(false);
		builder.setView(layout);
		builder.setPositiveButton("Passwort ändern", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {		        	   
	        	String oldPassword = keyEditText.getText().toString().trim();
	        	String password = passwordEditText.getText().toString().trim();
	        	String password2 = password2EditText.getText().toString().trim();
    			
    			if(oldPassword.length() <= 0 && password.length() <= 0 && password2.length() <= 0)
    			{
    				Utilities.message(context, "Bitte alle Felder ausfüllen.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			WG wg = new WG(settings);
    			ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", "") + "&password=" + oldPassword);
    				
				if(wgList.size() <= 0)
    			{
    				Utilities.message(context, "Passwort falsch.", "OK");	//TODO :string.xml
    				return;
    			}
    			
    			for(HashMap<String, String> wgEntry : wgList)
    			{
        			if(!password.equals(password2))
        			{
        				Utilities.message(context, "Passwörter stimmen nicht überein.", "OK");	//TODO :string.xml
        				return;
        			}
        			
        			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        			nameValuePairs.add(new BasicNameValuePair("password", password));
    				wg.update(Integer.valueOf(wgEntry.get("id")), nameValuePairs);
    				
    				Utilities.message(context, "Passwort wurde erfolgreich geändert.", "OK");
    			}
           }
		});
		
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void getChangeAdminDialog(final Context context, final SharedPreferences settings, final Boolean leave) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		final Spinner spinner = new Spinner(context);
		
		
		User user = new User(settings);
        ArrayList<HashMap<String, String>> userList = user.get("?wgId=" + settings.getString("wg_id", ""));
        ArrayList<String> names = new ArrayList<String>();
        for(HashMap<String, String> map : userList)
        {
        	if(map.get("id").compareTo(Utilities.getWGAdminId(settings)) != 0)
        	{
        		names.add(map.get("username"));
        	}
        }
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, names);
        spinner.setAdapter(adapter);	
		
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
		
		builder.setMessage("Sie müssen einen neuen Admin auswählen.");
		builder.setCancelable(false);
		builder.setView(spinner);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   User user = new User(settings);
        	   String username = (String) spinner.getSelectedItem();
        	   ArrayList<HashMap<String, String>> userList = user.get("?username=" + username);
        	   
        	   WG wg = new WG(settings);
        	   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        	   nameValuePairs.add(new BasicNameValuePair("adminId", userList.get(0).get("id")));
        	   wg.update(Integer.valueOf(settings.getString("wg_id", "")), nameValuePairs);
        	   
        	   if(leave)
        	   {
        		   Utilities.leaveWG(context, settings, user);
        	   }
        	   else
        	   {
        		   ((Activity) context).finish();
        		   ((Activity) context).startActivity(((Activity) context).getIntent());
        	   }
           }
		});
		
		builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		       
		AlertDialog alert = builder.create();
		alert.show();	
	}
	
}
