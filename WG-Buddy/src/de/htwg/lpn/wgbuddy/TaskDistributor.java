package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class TaskDistributor extends Activity 
{
	
	private ListView userList;
	private Button start;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.taskdistributor);
	        
	        userList = (ListView) findViewById(R.id.taskUserList);
	        start = (Button) findViewById(R.id.taskGoButton);
	        start.setOnClickListener(new OnClickListener() 
	        {
				
				@Override
				public void onClick(View v) 
				{
					ArrayList<String>checkedusers = new ArrayList<String>();
					
					ListView userList = (ListView) findViewById(R.id.taskUserList);
//					for(int i = 0; i < userList.getAdapter().getCount(); i++)
//					{
//						CheckBox cb = (CheckBox) userList.getAdapter()..getItem(i);
//						if(cb.isChecked())
//						{
//							checkedusers.add(((CheckBox)userList.getAdapter().getItem(i)).getText().toString());
//						}	
//					}
//					
//					System.out.print(checkedusers);
				}
			});
	        
	        ArrayList<HashMap<String, String>> users = JSONStuff.getMapListOfJsonArray( "http://wgbuddy.domoprojekt.de/sampleusers.json", "users");   
	        
	        SimpleAdapter sa = new SimpleAdapter(this, users, R.layout.taskdistributoruserlistentry, new String[] { "name"}, new int[] { R.id.task_userlistcheckbox});
	        
	        ViewBinder vb = new ViewBinder() 
	        {
				
				@Override
				public boolean setViewValue(View view, Object data, String textRepresentation) 
				{
					if(view.getId() == R.id.task_userlistcheckbox)
					{
						CheckBox cb = (CheckBox) view;
						cb.setText(textRepresentation);
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
}
