package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TaskDistributor extends Activity 
{
	
	private ListView userList;
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.taskdistributor);
	        
	        ArrayList<HashMap<String, String>> users = JSONStuff.getMapListOfJsonArray( "http://wgbuddy.domoprojekt.de/sampleusers.json", "users");   
	        
	        ArrayList<String> names = new ArrayList<String>();
	        
	        for (HashMap<String, String> hashMap : users) 
	        {
	        	names.add(hashMap.get("name"));
			}
	        
	        userList = (ListView) findViewById(R.id.taskUserList);
	        
	        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_multichoice,names);
	        
	        userList.setAdapter(adapter);
	        //TODO: Alle schon auswählen
	    }
}
