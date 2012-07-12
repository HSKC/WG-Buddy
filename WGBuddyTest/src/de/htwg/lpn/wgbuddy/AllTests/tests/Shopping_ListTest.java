package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.wgbuddy.Shopping_List;

public class Shopping_ListTest extends ActivityInstrumentationTestCase2<Shopping_List> 
{
	private ListView shoppingList;
	private RadioGroup filterRadioGroup;
	private Shopping_List activity;
	
	public Shopping_ListTest()
	{
		super("de.htwg.lpn.wgbuddy.Shopping_List", Shopping_List.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		shoppingList = (ListView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.shoppinglist);
		filterRadioGroup = (RadioGroup) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.shoppingList_radio_filter);
	}
	
	public void testPreconditions()
	{
		assertNotNull(shoppingList);
		assertNotNull(filterRadioGroup);
	}
}
