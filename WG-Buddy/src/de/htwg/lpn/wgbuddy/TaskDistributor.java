package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.random.RandomUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

public class TaskDistributor extends Activity 
{
	
	private ListView userList;
	private Button start;
	private SharedPreferences settings;
	private User user;
	private ArrayList<HashMap<String, String>> users;
	
	 @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdistributor);
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        userList = (ListView) findViewById(R.id.taskUserList);
        start = (Button) findViewById(R.id.taskGoButton);
        start.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{				
				ListView userList = (ListView) findViewById(R.id.taskUserList);
				ListAdapter adapter = userList.getAdapter();				
				Integer count = adapter.getCount();
				TreeMap<String, Double> checkedUser = new TreeMap<String, Double>();
				
				for(int i = 0; i < count; i++)
				{
					View entry = userList.getChildAt(i);
					CheckBox checkbox = (CheckBox) entry.findViewById(R.id.task_userlistcheckbox);
					if(checkbox.isChecked())
					{						
						HashMap<String, String> userData = (HashMap<String, String>) adapter.getItem(i);
						checkedUser.put(userData.get("username"), Double.valueOf(userData.get("points")));
					}
				}
				
				RandomUser randomUser = new RandomUser();
				
				String chosenUser = randomUser.getRandomUser(checkedUser);
				
				sendMail(chosenUser);
				
				setNewUserPoints(findUserId(chosenUser));
				
				Toast.makeText(TaskDistributor.this, "User" + chosenUser + 
						" wurde ausgewählt und benachrichtigt", Toast.LENGTH_SHORT).show();
			}

			
		});
        
        user = new User(settings); 
        users = user.get("?wgId=" + settings.getString("wg_id", ""));
   
	    SimpleAdapter sa = new SimpleAdapter(this, users, R.layout.taskdistributor_userentry, new String[] { "username"}, new int[] { R.id.task_userlistcheckbox});
        
        ViewBinder vb = new ViewBinder() 
        {
			
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) 
			{
				if(view.getId() == R.id.task_userlistcheckbox)
				{
					CheckBox cb = (CheckBox) view;
					cb.setText((CharSequence) data);
					cb.setChecked(true);
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
        
		userList.setAdapter(sa);
		
		
		
        
//	        ArrayList<String> names = new ArrayList<String>();
//	        
//	        for (HashMap<String, String> hashMap : users) 
//	        {
//	        	names.add(hashMap.get("name"));
//			}
//	        
//	        userList = (ListView) findViewById(R.id.taskUserList);
//	        
//	        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_multichoice,names);
//	        
//	        userList.setAdapter(adapter);
//	        //TODO: Alle schon auswählen
    }

	protected String findUserId(String chosenUser) 
	{
		String id = "";
		for(int i = 0; i < users.size(); i++)
		{
			if(users.get(i).get("username").compareTo(chosenUser) == 0)
			{
				id = users.get(i).get("id");
			}
		}
		return id;
	}

	protected void setNewUserPoints(String id) 
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		RatingBar rating = (RatingBar) findViewById(R.id.taskRatingBar);
		nameValuePairs.add(new BasicNameValuePair("points", Double.toString(rating.getRating() + findUserPoints(id))));
		user.update(Integer.valueOf(id), nameValuePairs);
	}

	private Double findUserPoints(String id) 
	{
		Double points = 0.0;
		for(int i = 0; i < users.size(); i++)
		{
			if(users.get(i).get("id").compareTo(id) == 0)
			{
				points = Double.valueOf(users.get(i).get("points"));
			}
		}
		return points;
	}

	protected String findUserEmail(String chosenUser) 
	{
		String email = "";
		for(int i = 0; i < users.size(); i++)
		{
			if(users.get(i).get("username").compareTo(chosenUser) == 0)
			{
				email = users.get(i).get("mail");
			}
		}
		return email;
	}
	
	private void sendMail(String chosenUser) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		EditText taskName = (EditText) findViewById(R.id.taskNameText);
		
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, findUserEmail(chosenUser));
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "neue Aufgabe von WGBuddy");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Du wurdes für die Aufgabe " + taskName.getText() + " ausgewählt");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "verschicke Mail"));
	}
}
