package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;


public class ShoppingList extends Activity 
{
	private ListView shoppingList;
	private View addButton;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.shoppinglist);
	        
	        shoppingList = (ListView) findViewById(R.id.shoppinglist);
	        
	        ArrayList<HashMap<String, String>> list = JSONStuff.getMapListOfJsonArray( "http://wgbuddy.domoprojekt.de/shopping.php", "Item"); 
	        
	        SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.shoppinglist_entry, new String[] { "name", "comment", "rating", "createdDate" }, new int[] { R.id.shoppingBigText, R.id.shoppingSmallText, R.id.ratingBar, R.id.createdDate});
	        
	        
	        
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
					else
					{
						return false;
					}
				}
			};
			sa.setViewBinder(vb);
	        shoppingList.setAdapter(sa);
	        
	        
	        
	        addButton = (Button) findViewById(R.id.shoppinglistAddButton);
            
            addButton.setOnClickListener
            (
        		new OnClickListener() 
        		{
    				
    				@Override
    				public void onClick(View v) 
    				{
    					Intent intent = new Intent(ShoppingList.this, Create_ShoppingItem.class);
    					startActivity(intent);
    				}
        		}
            );
        
	    }
}
