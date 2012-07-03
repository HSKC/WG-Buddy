package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.model.Task;

public class UserTaskList extends Activity 
{
	private ListView userTaskList;
	private SharedPreferences settings;
	private Task task;
	private ArrayList<HashMap<String, String>> tasks;

	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.usertasklist);
	     init();
	     
	     createList();
	 }

	private void createList() 
	{
		 SimpleAdapter sa = new SimpleAdapter(this, tasks, R.layout.usertask_entry, new String[]{"name", "id"}, new int[]{R.id.usertask_entryName, R.id.usertaskEntryShowButton});
	     
	     ViewBinder vb = new ViewBinder() 
	     {
			
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) 
			{
				if(view.getId() == R.id.usertask_entryName)
				{
					TextView name = (TextView) view;
					name.setText((CharSequence) data);
					return true;
				}
				else if(view.getId() == R.id.usertaskEntryShowButton)
				{
					final String id = data.toString();
					Button showTaskButton = (Button) view;
					showTaskButton.setTag(id);
					showTaskButton.setOnClickListener(new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							
							String par = "?wgId=" + settings.getString("wg_id", "") + "&userId=" + settings.getString("user_id", "") + "&id=" + id;
							ArrayList<HashMap<String, String>> selectedTask = task.get(par);
							
							Intent intent = new Intent(UserTaskList.this, UserTaskCompleteEntry.class);
							intent.putExtra("name", selectedTask.get(0).get("name"));
							intent.putExtra("comment", selectedTask.get(0).get("comment"));
							intent.putExtra("createdDate", selectedTask.get(0).get("createdDate"));
							intent.putExtra("deadline", selectedTask.get(0).get("deadline"));
							intent.putExtra("points", selectedTask.get(0).get("points"));
							intent.putExtra("status", selectedTask.get(0).get("status"));
							startActivity(intent);
						}

					});
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		
		userTaskList.setAdapter(sa);
	}

	private void init() 
	{
		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	    userTaskList = (ListView) findViewById(R.id.userTaskList);    
	     
	    task = new Task(settings); 
	    tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + settings.getString("user_id", ""));
	}
}
