package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.ShoppingItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private SharedPreferences settings = null;
	
	private ListView shoppingList;
	private View addButton;
	private View refreshButton;
	private View optionsButton;
	
	private Integer type = 0;
	private Integer sort = 0;
	private Integer direction = 0;
	
	ArrayAdapter<CharSequence> typeAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;	
	ArrayAdapter<CharSequence> directionAdapter = null;
	
	private void loadList()
	{
        String where = "";
        String order = "";
        String directionString = "";
        
        if(type == 1)
        {
        	where = "status=-1";
        }
        else if(type == 2)
        {
        	where = "status=0";
        }
        else if(type == 3)
        {
        	where = "status=1";
        }
        
        if(sort == 0)
        {
        	order = "orderby=name";
        }
        else if(sort == 1)
        {
        	order = "orderby=createdDate";
        }
        else if(sort == 2)
        {
        	order = "orderby=rating";
        }
        else if(sort == 3)
        {
        	order = "orderby=userId";
        }
        
        if(direction == 0)
        {
        	directionString = "direction=ASC";
        }
        else 
        {
        	directionString = "direction=DESC";
        }
        
        
        String url = settings.getString("pref_webserver", "") + "shopping.php?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "")? where + "&" : "") + order + "&" + directionString;     

        
		ArrayList<HashMap<String, String>> list = JSONStuff.getMapListOfJsonArray(url, "Item");
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
							ShoppingItem si = new ShoppingItem(settings);
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
							ShoppingItem si = new ShoppingItem(settings);
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
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        
        shoppingList = (ListView) findViewById(R.id.shoppinglist);
        addButton = (Button) findViewById(R.id.shoppinglistAddButton);
        refreshButton = (Button) findViewById(R.id.shoppinglistRefreshButton);
        optionsButton = (Button) findViewById(R.id.shoppinglistOptionsButton);        
        
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListType_array, android.R.layout.simple_spinner_item);
        sortAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListSort_array, android.R.layout.simple_spinner_item);
        directionAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListDirection_array, android.R.layout.simple_spinner_item);
        
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
        
        optionsButton.setOnClickListener
        (
    		new OnClickListener() 
    		{
				
				@Override
				public void onClick(View v) 
				{
					AlertDialog.Builder builder;

					Context mContext = ShoppingList.this;
					builder = new AlertDialog.Builder(mContext);
					
					LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
					View layout = inflater.inflate(R.layout.shoppinglist_optionsdialog, (ViewGroup) findViewById(R.id.shoppingList_optionsDialogLayout));
					
					builder.setView(layout);
					
					final AlertDialog alertDialog = builder.create();
					alertDialog.show();			        
			        
			        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        
			        Spinner typeSpinner = (Spinner) layout.findViewById(R.id.shoppingListTypeSpinner);
			        
			        typeSpinner.setAdapter(typeAdapter);
			        typeSpinner.setSelection(type);
			        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			        {
						public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
						{
							type = pos;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) 
						{
						}
					});
			        
		        	sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        
					
					Spinner sortSpinner = (Spinner) layout.findViewById(R.id.shoppingListSortSpinner);
			        
					sortSpinner.setAdapter(sortAdapter);
					sortSpinner.setSelection(sort);
			        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			        {
						public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
						{
							sort = pos;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) 
						{
						}
					});
			        
		        	directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        
					
					Spinner directionSpinner = (Spinner) layout.findViewById(R.id.shoppingListDirectionSpinner);
			        
					directionSpinner.setAdapter(directionAdapter);
					directionSpinner.setSelection(direction);
			        directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			        {
						public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
						{
							direction = pos;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) 
						{
						}
					});
			        
			        
			        Button okButton = (Button) layout.findViewById(R.id.shoppingListOptionsDialogButton);
			        okButton.setOnClickListener(new OnClickListener() 
			        {
						
						@Override
						public void onClick(View v) 
						{							
							alertDialog.dismiss();
							loadList();
						}
					});
				}
    		}
        );
    }
}
