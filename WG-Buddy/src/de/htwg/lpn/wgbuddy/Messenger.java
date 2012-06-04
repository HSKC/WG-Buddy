package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.htwg.lpn.model.Message;
import de.htwg.lpn.model.ShoppingItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class Messenger extends Activity
{
	private ListView messageList;
	private SharedPreferences settings;
	private Button addButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        
        messageList = (ListView) findViewById(R.id.messengerList);
        addButton = (Button) findViewById(R.id.messenger_add);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        addButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Messenger.this, Create_Message.class);
				startActivity(intent);
			}
		});
        
        
	    getList();   
	}
	 
	private void getList()
	{
		String where = "";
        String order = "orderby=createdDate";
        String directionString = "direction=ASC";
        
        String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "")? where + "&" : "") + order + "&" + directionString;

        Message me = new Message(settings);
        ArrayList<HashMap<String, String>> list = me.get(parameter);
       
        SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.messenger_entry, new String[] { "title","userName","message","createdDate" }, new int[] { R.id.messenger_titleText,R.id.messenger_userText,R.id.messenger_messageText,R.id.messenger_timeText});
        
        final Map<String,Integer> users = new HashMap<String,Integer>();
        
       
        
        ViewBinder vb = new ViewBinder() 
        {
			
        	
        	
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation) 
			{
				
				if(view.getId() == R.id.messenger_timeText)
				{
					String datum = (String) data;
					String time = datum.split(" ")[1];
					datum = datum.split(" ")[0];
					
					TextView timeview = (TextView) view;
					timeview.setText(time+"\n"+datum.split("-")[2]+"."+datum.split("-")[1]+"."+datum.split("-")[0]);
					return true;
				}
				if(view.getId() == R.id.messenger_userText)
				{
					String name = (String) data;
					TextView nameview = (TextView) view;
					nameview.setText(getResources().getString(R.string.messenger_from) +" "+name);
					
					if(! users.containsKey(name))
					{
						users.put(name, users.size());
					}
					
					Context context = getApplicationContext();
					String[] colors = context.getResources().getStringArray(R.array.messengercolors_array);
					//nameview.setTextColor(Color.parseColor(colors[users.get(name)]));
					
					View parent = (View) view.getParent();
					parent = (View) parent.getParent();
					parent = (View) parent.getParent();
					
					Drawable d = context.getResources().getDrawable(R.drawable.message_border);
					d.setColorFilter(Color.parseColor(colors[users.get(name)]), Mode.MULTIPLY);
					parent.setBackgroundDrawable(d);
					return true;
				}
				else
				{
					return false;
				}
			}
			
		};
		sa.setViewBinder(vb);
		sa.notifyDataSetChanged();
		messageList.setAdapter(sa);
		
		messageList.setSelection(messageList.getCount() - 1);
	}
}
