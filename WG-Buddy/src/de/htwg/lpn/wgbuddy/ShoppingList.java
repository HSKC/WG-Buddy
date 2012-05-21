package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ShoppingList extends Activity 
{
	private LinearLayout shoppingList;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.shoppinglist);
	        
	        shoppingList = (LinearLayout) findViewById(R.id.shoppinglist);
	        
	        ArrayList<String> items= new ArrayList<String>();
	        
	        items.add("Bier");
	        items.add("Mehr Bier");
	        items.add("Baconstrips");
	        
	        for (String string : items) 
	        {
	        	View view = getLayoutInflater().inflate(R.layout.shoppinglistentry, shoppingList, false);
		        
	        	((TextView)view.findViewById(R.id.shoppingBigText)).setText(string);
	        	
		        shoppingList.addView(view);

			}
	        

	        
	        
	    }
}
