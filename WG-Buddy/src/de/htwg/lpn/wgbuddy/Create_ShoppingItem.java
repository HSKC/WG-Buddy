package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.JSON;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TimePicker;

public class Create_ShoppingItem  extends Activity
{
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
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("wgId", "2"));
			        nameValuePairs.add(new BasicNameValuePair("userId", "2"));
			        nameValuePairs.add(new BasicNameValuePair("name", nameEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("comment", descriptionEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(ratingBar.getRating())));
			        
			        String date = String.valueOf(deadlineDatePicker.getYear()) + "-" + String.valueOf(deadlineDatePicker.getMonth() + 1).toString() + "-" + String.valueOf(deadlineDatePicker.getDayOfMonth()) + " " + deadlineTimePicker.getCurrentHour().toString() + ":" + deadlineTimePicker.getCurrentMinute().toString() + ":00";
			        nameValuePairs.add(new BasicNameValuePair("deadline", date));			        
			        nameValuePairs.add(new BasicNameValuePair("status", "-1"));
					
			        String url = "http://wgbuddy.domoprojekt.de/shopping.php?insert";
			        
					JSON.postData(url, nameValuePairs);					
					
					Intent intent = new Intent(Create_ShoppingItem.this, ShoppingList.class);
					startActivity(intent);
				}
    		}
        );
    }
}
