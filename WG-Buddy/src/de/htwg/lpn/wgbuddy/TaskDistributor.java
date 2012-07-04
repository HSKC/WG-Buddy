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
import de.htwg.lpn.wgbuddy.utility.RandomUser;
import de.htwg.lpn.wgbuddy.utility.Utilities;

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
	private Button taskListButton;
	
	 @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdistributor);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        init();
        
        clickOnStartButton();
   
	    createList();
		
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

	private void createList() 
	{
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
		
		
		taskListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(TaskDistributor.this, UserTaskList.class);
				startActivity(intent);				
			}
			
		});
	}

	private void clickOnStartButton() 
	{
		start.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{			
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
				
				RandomUser rand = RandomUser.getInstance();
				rand.setUserlist(checkedUser);
				
				Intent intent = new Intent(TaskDistributor.this, Create_Task.class);
				startActivity(intent);				
			}

			
		});
	}

	private void init() 
	{
		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        userList = (ListView) findViewById(R.id.taskUserList);
        start = (Button) findViewById(R.id.taskGoButton);
        user = new User(settings); 
        users = user.get("?wgId=" + settings.getString("wg_id", ""));
        taskListButton = (Button) findViewById(R.id.myTaskButton);
	}
	 
	
}
