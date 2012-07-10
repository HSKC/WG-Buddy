package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import de.htwg.lpn.wgbuddy.Task_List;

public class Task_ListTest extends ActivityInstrumentationTestCase2<Task_List>
{
	private ListView taskList;
	private RadioGroup filterRadioGroup;
	private Task_List activity;

	public Task_ListTest()
	{
		super("de.htwg.lpn.wgbuddy.Task_List", Task_List.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		taskList = (ListView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userTaskList);
		filterRadioGroup = (RadioGroup) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.taskList_radio_filter);
	}
	
	public void testPreconditions()
	{
		assertNotNull(taskList);
		assertNotNull(filterRadioGroup);
	}

}
