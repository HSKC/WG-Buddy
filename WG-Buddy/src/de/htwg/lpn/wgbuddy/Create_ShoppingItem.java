package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.model.Utilities;
import de.htwg.lpn.wgbuddy.utility.JSON;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TimePicker;

public class Create_ShoppingItem  extends Activity
{
	private SharedPreferences settings = null;
	
	private Button addButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private RatingBar ratingBar;
	private DatePicker deadlineDatePicker;
	private TimePicker deadlineTimePicker;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_shoppingitem);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        final Activity that = this;
        
        addButton = (Button) findViewById(R.id.shoppingItemAddButton);
        nameEditText = (EditText) findViewById(R.id.ShoppingItemNameEditText);
        descriptionEditText = (EditText) findViewById(R.id.ShoppingDescriptionEditText);
        ratingBar = (RatingBar) findViewById(R.id.shoppingItemRatingBar);
        deadlineDatePicker = (DatePicker) findViewById(R.id.shoppingDeadlineDatePicker);
        deadlineTimePicker = (TimePicker) findViewById(R.id.shoppingDeadLineTimePicker);
        
        addButton.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					
					 ProgressDialog pd = ProgressDialog.show(that, "Working..", "", true, false);
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
			        nameValuePairs.add(new BasicNameValuePair("userId", "0"));
			        nameValuePairs.add(new BasicNameValuePair("name", nameEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("comment", descriptionEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(ratingBar.getRating())));
			        
			        String date = String.valueOf(deadlineDatePicker.getYear()) + "-" + String.valueOf(deadlineDatePicker.getMonth() + 1).toString() + "-" + String.valueOf(deadlineDatePicker.getDayOfMonth()) + " " + deadlineTimePicker.getCurrentHour().toString() + ":" + deadlineTimePicker.getCurrentMinute().toString() + ":00";
			        nameValuePairs.add(new BasicNameValuePair("deadline", date));			        
			        nameValuePairs.add(new BasicNameValuePair("status", "-1"));					
		        
					ShoppingItem shopping = new ShoppingItem(settings);
					shopping.insert(nameValuePairs);	
					
					String message = "http://wgbuddy.domoprojekt.de/?wgId=" + settings.getString("wg_id", "") + "&" + "msgType=collapsed" + "&" + "messageText=ShoppingItem";
					
					JSON.postData(message);
					
			        if(WGBuddyActivity.usepush)
			        {
						List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
				        nameValuePairs2.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
				        nameValuePairs2.add(new BasicNameValuePair("msgType", "collapsed"));
				        nameValuePairs2.add(new BasicNameValuePair("messageText", "ShoppingItem"));
						
						String message2 = "http://wgbuddy.domoprojekt.de/googleService.php"; 
						
						JSON.postData(message2,nameValuePairs2);
			        }
					
					Intent intent = new Intent(Create_ShoppingItem.this, ShoppingList.class);
					startActivity(intent);
				}
    		}
        );
    }
}
