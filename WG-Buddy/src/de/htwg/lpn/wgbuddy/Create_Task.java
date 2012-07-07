package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.RandomUser;

public class Create_Task extends Activity 
{
	private SharedPreferences settings;
	
	private TextView name;
	private TextView comment;
	private RatingBar points;
	private Button start;
	
	private Button userListButton;
	private ListView userList;	
	
	private Task task;
	private User user;

	private ArrayList<HashMap<String, String>> users;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.create_task);
	    
	    settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
		
	    name = (TextView) findViewById(R.id.taskNameText);
	    comment = (TextView) findViewById(R.id.userTaskComment);
	    points = (RatingBar) findViewById(R.id.taskRatingBar);
	    //userList = (ListView) findViewById(R.id.taskUserList);
	    start = (Button) findViewById(R.id.usertaskgoButton);
	    
	    userListButton = (Button) findViewById(R.id.task_userListButton);
	    userList = new ListView(this);
		
		task = new Task(settings);		
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
		
		userListButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getTaskUserDialog();				
			}
		});
        
        clickOnStartButton();
    }

	private void clickOnStartButton() 
	{
		start.setOnClickListener(new OnClickListener() 
        {
        	@Override
			public void onClick(View v) 
			{	
        		if(name.getText().toString().compareTo("") != 0 && comment.getText().toString().compareTo("") != 0)
        		{
    				ListAdapter adapter = userList.getAdapter();				
    				Integer count = userList.getChildCount();
    				TreeMap<String, Double> checkedUser = new TreeMap<String, Double>();
				
    				if(count == 0)
    				{
    					for(HashMap<String, String> map : users)
    					{
    						checkedUser.put(map.get("username"), Double.valueOf(map.get("points")));
    					}
    				}
    				else
    				{
	    				for(int i = 0; i < count; i++)
	    				{
	    					View entry = userList.getChildAt(i);
	    					if(entry != null)
	    					{
		    					CheckBox checkbox = (CheckBox) entry.findViewById(R.id.task_userlistcheckbox);
		    					if(checkbox.isChecked())
		    					{						
		    						@SuppressWarnings("unchecked")
		    						HashMap<String, String> userData = (HashMap<String, String>) adapter.getItem(i);
		    						checkedUser.put(userData.get("username"), Double.valueOf(userData.get("points")));
		    					}
	    					}
	    				}
    				}
        			
        			RandomUser randomUser = RandomUser.getInstance();
					randomUser.setUserlist(checkedUser);
					
					String chosenUserName = randomUser.getRandomUser();
					
					Toast.makeText(Create_Task.this, "User" + chosenUserName + 
							" wurde ausgewählt und benachrichtigt", Toast.LENGTH_SHORT).show();
					
					createNewTask(chosenUserName);
					
					Intent intent = new Intent(Create_Task.this, TaskList.class);
					startActivity(intent);
        		}
        		else
        		{
					Toast.makeText(Create_Task.this, "Die Felder Aufgabe und Kommentar müssen beschrieben sein", Toast.LENGTH_SHORT).show();
        		}
	        }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.basic_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent intent;
		switch (item.getItemId()) 
        {        	
	        case R.id.about:
	        	Dialogs.getAboutDialog(Create_Task.this, settings);
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(Create_Task.this, WGBuddyActivity.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
	
	private void createNewTask(String chosenUserName) 
	{
		String userId = findUserId(chosenUserName);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
		nameValuePairs.add(new BasicNameValuePair("userId", userId));
		nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("comment", comment.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("points", new Double(points.getRating()).toString()));
		nameValuePairs.add(new BasicNameValuePair("status", "0"));
		task.insert(nameValuePairs);
		
		ArrayList<HashMap<String, String>> tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + userId);
		
		//sendMail(tasks);
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
	
	public void getTaskUserDialog()
	{
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(this);

		builder.setView(userList);
		builder.setPositiveButton(getString(R.string.utilities_ok), new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   
           }
		});

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
