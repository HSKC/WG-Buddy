package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;


public class ShoppingList extends Activity 
{
	private SharedPreferences settings = null;
	
	private ListView shoppingList;
	private RadioGroup filterRadioGroup;
	
	private Integer type = 0;
	private Integer sort = 0;
	private Integer direction = 0;
	private Integer filter = 0;
	
	ArrayAdapter<CharSequence> typeAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;	
	ArrayAdapter<CharSequence> directionAdapter = null;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist);
        
        settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
        Utilities.checkByPass(this, settings);
        
        shoppingList = (ListView) findViewById(R.id.shoppinglist); 
        filterRadioGroup = (RadioGroup) findViewById(R.id.shoppingList_radio_filter);
        
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListType_array, android.R.layout.simple_spinner_item);
        sortAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListSort_array, android.R.layout.simple_spinner_item);
        directionAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListDirection_array, android.R.layout.simple_spinner_item);
        
        filterRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				if(checkedId == R.id.shoppingList_radio_filterAll)
				{
					filter = 0;
				}
				else
				{
					filter = 1;
				}
				loadList();
			}
		});
        
        loadList();       
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.shoppinglist_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent intent;
		switch (item.getItemId()) 
        {
       		case R.id.add:
        		intent = new Intent(ShoppingList.this, Create_ShoppingItem.class);
				startActivity(intent);				
				return true;
        	
        	case R.id.refresh:
        		loadList();
				return true;
				
        	case R.id.settings:
        		getOptionsDialog();
				return true;
        	
	        case R.id.about:
	        	String text = "Dies ist eine von Jens Küblbeck, Felix Offergeld und Dominik Wieland entwickelte App.\n";
	        	text += "Version 1.0";
	        	text += "Das Benutzerhandbuch finden Sie unter dem Link ...";	        	
	        	Utilities.message(this, text, "OK");
	        	return true;
	        	
	        case R.id.menu:
	        	intent = new Intent(ShoppingList.this, WGBuddyActivity.class);
				startActivity(intent);	
	        	return true;
	        	
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
	
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
        
        if(filter == 1)
        {
        	where += "&userId=" + settings.getString("user_id", "");
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
        
        
        String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "")? where + "&" : "") + order + "&" + directionString;

        ShoppingItem si = new ShoppingItem(settings);
        User user = new User(settings);
        ArrayList<HashMap<String, String>> list = si.get(parameter);
        
        for(HashMap<String, String> map : list)
        {
        	if(map.get("userId").equals("0"))
        	{
        		map.put("username", "Keiner");
        	}
        	else
        	{
				ArrayList<HashMap<String, String>> userList = user.get("?id=" + map.get("userId"));
				if(userList.size() > 0)
				{
					map.put("username", userList.get(0).get("username"));
				}
				else
				{
					map.put("username", "Keiner");				
				}
        	}
        }
       
        SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.shoppinglist_entry, new String[] { "id", "id", "id", "name", "comment", "rating", "createdDate", "username", "status" }, new int[] { R.id.shoppingListEntryCompletedButton, R.id.shoppingListEntryShoppingButton, R.id.shoppingListEntryDeleteButton, R.id.shoppingBigText, R.id.shoppingSmallText, R.id.ratingBar, R.id.createdDate, R.id.shoppingUsername, R.id.shoppingList_Entry});
        
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
				else if(view.getId() == R.id.shoppingList_Entry)
				{
					LinearLayout ll = (LinearLayout) view;
					
					if(textRepresentation.compareTo("-1") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_red);
						
					}
					else if(textRepresentation.compareTo("0") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_orange);
					}
					else if(textRepresentation.compareTo("1") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_green);
					}
					
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
							nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "")));
							nameValuePairs.add(new BasicNameValuePair("status", "1"));
									
							si.update(id, nameValuePairs);
							
							loadList();
						}
					});
					return true;
				}
				else if(view.getId() == R.id.shoppingListEntryShoppingButton)
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
							nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "")));
							nameValuePairs.add(new BasicNameValuePair("status", "0"));
									
							si.update(id, nameValuePairs);
							
							loadList();
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
							si.delete(id,ShoppingList.this);
							
							loadList();
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
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {

	       Intent intent = new Intent(ShoppingList.this, WGBuddyActivity.class);
	       startActivity(intent);
	       
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void getOptionsDialog()
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
