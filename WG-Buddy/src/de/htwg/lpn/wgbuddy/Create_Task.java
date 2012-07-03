package de.htwg.lpn.wgbuddy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.RandomUser;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Task extends Activity 
{
	private TextView name;
	private TextView comment;
	private DatePicker deadline;
	private RatingBar points;
	private SharedPreferences settings;
	private Task task;
	private User user;
	private ArrayList<HashMap<String, String>> users;
	private Button start;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.create_task);
	    
	    init();
        
        clickOnStartButton();
    }

	private void clickOnStartButton() 
	{
		start.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View v) 
			{				
				RandomUser randomUser = RandomUser.getInstance();
				
				String chosenUserName = randomUser.getRandomUser();
				
				Toast.makeText(Create_Task.this, "User" + chosenUserName + 
						" wurde ausgewählt und benachrichtigt", Toast.LENGTH_SHORT).show();
				
				createNewTask(chosenUserName);
				
				Intent intent = new Intent(Create_Task.this, TaskDistributor.class);
				startActivity(intent);
	        }

			
        });
	}

	private void init() 
	{
		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	    name = (TextView) findViewById(R.id.taskNameText);
	    comment = (TextView) findViewById(R.id.userTaskComment);
	    deadline = (DatePicker) findViewById(R.id.userTaskDatePicker);
	    points = (RatingBar) findViewById(R.id.taskRatingBar);
	    start = (Button) findViewById(R.id.usertaskgoButton);
		
		task = new Task(settings);
		
		user = new User(settings); 
        users = user.get("?wgId=" + settings.getString("wg_id", ""));
	}
	
	private void createNewTask(String chosenUserName) 
	{
		SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String deadlineString = new Integer(deadline.getDayOfMonth()).toString() + "." + new Integer(deadline.getMonth()) + "." + new Integer(deadline.getYear());
		String userId = findUserId(chosenUserName);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
		nameValuePairs.add(new BasicNameValuePair("userId", userId));
		nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("comment", comment.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("points", new Double(points.getRating()).toString()));
		nameValuePairs.add(new BasicNameValuePair("deadline", deadlineString));
		nameValuePairs.add(new BasicNameValuePair("voteDeadline", ""));
		nameValuePairs.add(new BasicNameValuePair("status", "0"));
		task.insert(nameValuePairs);
		
		ArrayList<HashMap<String, String>> tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + userId);
		
		sendMail(tasks);
	}

	private void sendMail(ArrayList<HashMap<String, String>> tasks) 
	{
		user.sendTask(tasks.get(0).get("id"));
	}
	
	protected String findUserId(String chosenUserName) 
	{
		ArrayList<HashMap<String, String>> chosenUser = user.get("?wgId=" + settings.getString("wg_id", "") + "&username=" + chosenUserName);
		return chosenUser.get(0).get("id");
	}

	protected String findUserEmail(String chosenUserName) 
	{
		ArrayList<HashMap<String, String>> chosenUser = user.get("?wgId=" + settings.getString("wg_id", "") + "&username=" + chosenUserName);
		return chosenUser.get(0).get("email");
	}
	
//	private void sendMail(String chosenUser) {
//		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//		EditText taskName = (EditText) findViewById(R.id.taskNameText);
//		
//		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, findUserEmail(chosenUser));
//		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "neue Aufgabe von WGBuddy");
//		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Du wurdes für die Aufgabe " + taskName.getText() + " ausgewählt");
//		emailIntent.setType("text/plain");
//		startActivity(Intent.createChooser(emailIntent, "verschicke E-Mail"));
//	}
}
