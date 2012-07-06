package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.wgbuddy.utility.Dialogs;

public class UserTaskList extends Activity 
{
	private ListView userTaskList;
	private SharedPreferences settings;
	private Task task;
	private ArrayList<HashMap<String, String>> tasks;
	private ArrayList<String> idList;

	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.usertasklist);
	     init();
	     
	     createList();
	 }
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.basicrefresh_menu, menu);
		    return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			Intent intent;
			switch (item.getItemId()) 
	        {   
		        case R.id.refresh:
		        	finish();
		        	startActivity(getIntent());
		        	return true;
			
		        case R.id.about:
		        	Dialogs.getAboutDialog(UserTaskList.this, settings);
		        	return true;
		        	
		        case R.id.menu:
		        	intent = new Intent(UserTaskList.this, WGBuddyActivity.class);
					startActivity(intent);	
		        	return true;
		        	
		        default:
		        	return super.onOptionsItemSelected(item);
	        }
	    }

	private void createList() 
	{
		 SimpleAdapter sa = new SimpleAdapter(this, tasks, R.layout.usertask_entry, new String[]{"name", "id"}, new int[]{R.id.usertask_entryName, R.id.textid});
	     
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
				else if(view.getId() == R.id.textid)
				{
					
					idList.add(data.toString());
					
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
		userTaskList.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{				
				String id = idList.get(arg2);
				String par = "?wgId=" + settings.getString("wg_id", "") + "&userId=" + settings.getString("user_id", "") + "&id=" + id;
				ArrayList<HashMap<String, String>> selectedTask = task.get(par);
			
				Intent intent = new Intent(UserTaskList.this, UserTaskCompleteEntry.class);
				intent.putExtra("name", selectedTask.get(0).get("name"));
				intent.putExtra("comment", selectedTask.get(0).get("comment")); 
				intent.putExtra("points", selectedTask.get(0).get("points"));
				intent.putExtra("status", selectedTask.get(0).get("status"));
				startActivity(intent);
			}
		});
	}

	private void init() 
	{
		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
	    userTaskList = (ListView) findViewById(R.id.userTaskList);    
	     
	    task = new Task(settings); 
	    tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + settings.getString("user_id", ""));
	    idList = new ArrayList<String>();
	}
}
