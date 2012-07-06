package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwg.lpn.model.User;
import de.htwg.lpn.model.WG;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class Information extends Activity 
{
    private SharedPreferences settings;
    private TextView wgName;
	private TextView wgAdmin;
	private TextView wgPassword;
	private ListView wgUserList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.information);  
        
        wgName = (TextView) findViewById(R.id.information_wgName);
        wgAdmin = (TextView) findViewById(R.id.information_wgAdmin);
        wgPassword = (TextView) findViewById(R.id.information_wgPassword);
        wgUserList = (ListView) findViewById(R.id.information_wg_list);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);       
        User user = new User(settings);
        
        wgName.setText(settings.getString("wg_name", ""));
        
        ArrayList<HashMap<String, String>> adminList = user.get("?id=" + Utilities.getWGAdminId(settings));
        wgAdmin.setText(adminList.get(0).get("username"));
        
        WG wg = new WG(settings);
        ArrayList<HashMap<String, String>> wgList = wg.get("?id=" + settings.getString("wg_id", ""));
        wgPassword.setText(wgList.get(0).get("password"));

        
        ArrayList<HashMap<String, String>> userList = user.get("?wgId=" + settings.getString("wg_id", ""));        
        
        SimpleAdapter adapter = new SimpleAdapter(this, userList, R.layout.information_wglist_entry, new String[] { "username", "email" }, new int[] { R.id.information_wglist_name, R.id.information_wglist_email});
        wgUserList.setAdapter(adapter);        
        
    }	
}
