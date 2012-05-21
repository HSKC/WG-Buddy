package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

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
	        
	        userList = (ListView) findViewById(R.id.taskUserList);
	    }
}
