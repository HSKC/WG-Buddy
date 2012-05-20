package de.htwg.lpn.wgbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class ShoppingList extends Activity 
{
	private View view;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.shoppinglist);
	        
	        view = findViewById(R.id.shoppingListView);
	    }
}
