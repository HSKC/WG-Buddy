package de.htwg.lpn.wgbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WGBuddyActivity extends Activity 
{

	private Button shoppinglist;
	private Button taskdistributor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        shoppinglist = (Button) findViewById(R.id.shoppingListButton);
        taskdistributor = (Button) findViewById(R.id.taskDistributorButton);
        
        shoppinglist.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(WGBuddyActivity.this,ShoppingList.class);
					startActivity(intent);
				}
    		}
        );
        
        taskdistributor.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(WGBuddyActivity.this,TaskDistributor.class);
					startActivity(intent);
				}
    		}
        );
    }
}