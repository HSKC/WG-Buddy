package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.model.WGBuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;


public class ShoppingList extends Activity 
{
	private WGBuddy wgBuddy;
	
	private ListView shoppingList;
	private View addButton;
	private View refreshButton;
	private ImageButton deleteButton;	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.shoppinglist);
	        
	        wgBuddy = (WGBuddy) getApplicationContext();
	        
	        shoppingList = (ListView) findViewById(R.id.shoppinglist);
	        
	        ArrayList<HashMap<String, String>> list = JSONStuff.getMapListOfJsonArray( "http://wgbuddy.domoprojekt.de/shopping.php", "Item"); 
	        
	        SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.shoppinglistentry, new String[] { "id", "name", "comment", "rating", "createdDate" }, new int[] { R.id.deleteButton, R.id.shoppingBigText, R.id.shoppingSmallText, R.id.ratingBar, R.id.createdDate});
	        
	        
	        ViewBinder vb = new ViewBinder() 
	        {
				
				@Override
				public boolean setViewValue(View view, Object data, String textRepresentation) 
				{
					if(view.getId() == R.id.ratingBar)
					{
						RatingBar rb = (RatingBar) view;
						rb.setRating(Integer.valueOf(data.toString()));
						return true;
					}
					else if(view.getId() == R.id.deleteButton)
					{
						ImageButton button = (ImageButton) view;
						button.setTag(Integer.valueOf(data.toString()));
						return true;
					}
					else
					{
						return false;
					}
				}
			};
			sa.setViewBinder(vb);
	        shoppingList.setAdapter(sa);
	        
	        shoppingList.setClickable(true);
	        
	        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
	        {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
				{
					Integer id = (Integer) arg1.getTag();
					ShoppingItem si = new ShoppingItem(wgBuddy);
					si.delete(id);
				}
			});
	
	        
	        addButton = (Button) findViewById(R.id.shoppinglistAddButton);
	        refreshButton = (Button) findViewById(R.id.shoppinglistRefreshButton);
            
            addButton.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(ShoppingList.this, NewShoppingItem.class);
    					startActivity(intent);
    				}
        		}
            );
            
            refreshButton.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(ShoppingList.this, ShoppingList.class);
    					startActivity(intent);
    				}
        		}
            );
        
	    }
}
