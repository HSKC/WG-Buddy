package de.htwg.lpn.wgbuddy.AllTests.tests;

import de.htwg.lpn.wgbuddy.Main;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

public class MainTest extends ActivityInstrumentationTestCase2<Main> 
{
	private Button shoppinglist;
	private Button taskdistributor;
	private Button messenger;
	private Button preferences;
	private Button information;
	private Button invite;
	private Button logout;
	private TextView heading;
	private Main activity;
	
	public MainTest()
	{
		super("de.htwg.lpn.wgbuddy.Main", Main.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{
		super.setUp();
		activity = this.getActivity();
		shoppinglist = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_shoppingListButton);
		taskdistributor = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_taskDistributorButton);
		messenger = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_messengerButton);
		preferences = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_preferencesButton);
		information = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_informationButton);
		invite = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_addButton);
		logout = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_logoutButton);
		heading = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_headingText);
	}
	
	public void testPreconditions() 
	{
		assertNotNull(shoppinglist);
		assertNotNull(taskdistributor);
		assertNotNull(messenger);
		assertNotNull(preferences);
		assertNotNull(information);
		assertNotNull(invite);
		assertNotNull(logout);
		assertNotNull(heading);
	}
}
