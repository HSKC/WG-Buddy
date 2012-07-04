package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.ObjectBase;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RatingBar;

public class UserTaskCompleteEntry extends Activity
{
	private EditText name;
	private EditText comment;
	private EditText createdDate;
	private EditText deadline;
	private RatingBar ratingBar;
	private CheckBox checkBox;
	private SharedPreferences settings;
	private Task task;
	private ArrayList<HashMap<String, String>> tasks;
	private User user;
	private ArrayList<HashMap<String, String>> users;

	@Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.usertask_completeentry);
		 
		 settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
		 
		 init();
		  
		 isChecked();	 
	}

	private void isChecked() 
	{
		if(checkBox.isChecked())
		 {
			 checkBox.setEnabled(false);
		 }
		 else
		 {
			 checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			 {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						for(int i = 0; i < tasks.size(); i++)
						{
							if(tasks.get(i).get("name").compareTo(name.getText().toString()) == 0)
							{
								updateTask(i);
							}
						}
					}
					
				}

				
			});
		 }
	}

	private void updateTask(int i)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("status", "1"));
		task.update(Integer.valueOf(tasks.get(i).get("id")), nameValuePairs);
		updatePoints(tasks.get(i).get("userId"));
		Intent intent = new Intent(UserTaskCompleteEntry.this, UserTaskList.class);
		startActivity(intent);
	}
	
	private void init() 
	{
		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
		name = (EditText) findViewById(R.id.usertask_completeentrynametext);
		comment = (EditText) findViewById(R.id.usertask_completeentrycommenttext);
		createdDate = (EditText) findViewById(R.id.usertask_completeentrycreatedatetext);
		deadline = (EditText) findViewById(R.id.usertask_completeentrydeadlinetext);
		ratingBar = (RatingBar) findViewById(R.id.usertask_copleteentryratingBar);
		checkBox = (CheckBox) findViewById(R.id.usertask_completeentryclearedcheckBox);
		 
		name.setText((CharSequence) getIntent().getExtras().getString("name"));
		comment.setText((CharSequence) getIntent().getExtras().getString("comment"));
		createdDate.setText((CharSequence) getIntent().getExtras().getString("createdDate"));
		deadline.setText((CharSequence) getIntent().getExtras().getString("deadline"));
		ratingBar.setRating((Double.valueOf(getIntent().getExtras().getString("points")).floatValue()));
		checkBox.setChecked((getIntent().getExtras().getString("status").compareTo("1") == 0) ? true : false); 
		 
		user = new User(settings);
		users = user.get("?wgId=" + settings.getString("wg_id", ""));
		 
		task = new Task(settings); 
	    tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + settings.getString("user_id", ""));
	}
	
	protected void updatePoints(String id) 
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("points", Double.toString(ratingBar.getRating() + findUserPoints(id))));
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
}
