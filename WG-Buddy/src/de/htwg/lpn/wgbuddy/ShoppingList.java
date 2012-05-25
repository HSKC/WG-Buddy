package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.ShoppingItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;


public class ShoppingList<T> extends Activity 
{
	private Store store;
	
	private ListView shoppingList;
	private View addButton;
	private View refreshButton;
	private Spinner typeSpinner;
	private Spinner sortSpinner;
	
	private String type = "";
	private String sort = "";
	
	private void loadList()
	{
        String where = "";
        
        if(type.equals("Offen"))
        {
        	where = "?status=-1";
        }
        else if(type.equals("InProgress"))
        {
        	where = "?status=0";
        }
        else if(type.equals("Done"))
        {
        	where = "?status=1";
        }
		
		ArrayList<HashMap<String, String>> list = JSONStuff.getMapListOfJsonArray("http://wgbuddy.domoprojekt.de/shopping.php" + where, "Item");
        SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.shoppinglist_entry, new String[] { "id", "id", "name", "comment", "rating", "createdDate" }, new int[] { R.id.shoppingListEntryCompletedButton, R.id.shoppingListEntryDeleteButton, R.id.shoppingBigText, R.id.shoppingSmallText, R.id.ratingBar, R.id.createdDate});
        
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
				else if(view.getId() == R.id.shoppingListEntryCompletedButton)
				{
					ImageButton button = (ImageButton) view;
					Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					button.setOnClickListener(new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							ShoppingItem si = new ShoppingItem(store);
							Integer id = (Integer) v.getTag();
							
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("status", "1"));
									
							si.update(id, nameValuePairs);
							
							refreshButton.performClick();
						}
					});
					return true;
				}
				else if(view.getId() == R.id.shoppingListEntryDeleteButton)
				{
					ImageButton button = (ImageButton) view;
					Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					button.setOnClickListener(new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							ShoppingItem si = new ShoppingItem(store);
							Integer id = (Integer) v.getTag();
							si.delete(id);
							
							refreshButton.performClick();
						}
					});
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
        shoppingList.setAdapter(sa);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist);
        
        store = (Store) getApplicationContext();
        
        shoppingList = (ListView) findViewById(R.id.shoppinglist);
        addButton = (Button) findViewById(R.id.shoppinglistAddButton);
        refreshButton = (Button) findViewById(R.id.shoppinglistRefreshButton);
        typeSpinner = (Spinner) findViewById(R.id.shoppingListTypeSpinner);
        sortSpinner = (Spinner) findViewById(R.id.shoppingListSortSpinner);
       
        loadList();
        
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
        
        
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListSort_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				sortSpinner.setSelection(pos);				
				String selectedSort = sortSpinner.getItemAtPosition(pos).toString();
				if(!selectedSort.equals(sort))
				{	
					sort = selectedSort;
					loadList();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
        
        
        ArrayAdapter<CharSequence> TypeAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListType_array, android.R.layout.simple_spinner_item);
        TypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(TypeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				typeSpinner.setSelection(pos);
				String selectedType = typeSpinner.getItemAtPosition(pos).toString();
				Object obj = sortSpinner.getItemAtPosition(pos);
				if(!selectedType.equals(type))
				{	
					type = selectedType;
					loadList();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
    }
}
