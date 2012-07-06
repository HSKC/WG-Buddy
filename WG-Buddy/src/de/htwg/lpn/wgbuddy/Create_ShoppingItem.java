package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.JSON;
import de.htwg.lpn.wgbuddy.utility.Utilities;

public class Create_ShoppingItem  extends Activity
{
	private SharedPreferences settings = null;
	
	private Button addButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private RatingBar ratingBar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_shoppingitem);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        addButton = (Button) findViewById(R.id.shoppingItemAddButton);
        nameEditText = (EditText) findViewById(R.id.ShoppingItemNameEditText);
        descriptionEditText = (EditText) findViewById(R.id.ShoppingDescriptionEditText);
        ratingBar = (RatingBar) findViewById(R.id.shoppingItemRatingBar);
        
        addButton.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					
					ProgressDialog.show(Create_ShoppingItem.this, "Working..", "", true, false);
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
			        nameValuePairs.add(new BasicNameValuePair("userId", "0"));
			        nameValuePairs.add(new BasicNameValuePair("name", nameEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("comment", descriptionEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(ratingBar.getRating())));
			        nameValuePairs.add(new BasicNameValuePair("status", "-1"));					
		        
					ShoppingItem shopping = new ShoppingItem(settings);
					shopping.insert(nameValuePairs,Create_ShoppingItem.this);	
					
			        if(WGBuddyActivity.usepush)
			        {
						List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
				        nameValuePairs2.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
				        nameValuePairs2.add(new BasicNameValuePair("msgType", "collapsed"));
				        nameValuePairs2.add(new BasicNameValuePair("messageText", "ShoppingItem"));
						
						String message2 = "http://wgbuddy.domoprojekt.de/googleService.php"; 
						
						JSON.postData(message2,nameValuePairs2,Create_ShoppingItem.this);
			        }
					
					Intent intent = new Intent(Create_ShoppingItem.this, ShoppingList.class);
					startActivity(intent);
				}
    		}
        );
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
	        	Dialogs.getAboutDialog(Create_ShoppingItem.this, settings);
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(Create_ShoppingItem.this, WGBuddyActivity.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
}
