package de.htwg.lpn.wgbuddy.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import de.htwg.lpn.wgbuddy.WGBuddyActivity;



public class WGBuddyActivityTest extends ActivityInstrumentationTestCase2<WGBuddyActivity> 
{
	private WGBuddyActivity mActivity;
	private Button tShoppinglist;
	private Button tTaskdistributor;
	private Button tPreferences;
	private Button tInvite;
	private Button tMessenger;
	private Button tLogout;
	private TextView tHeading;

	public WGBuddyActivityTest() 
	{
		super("de.htwg.lpn.wgbuddy.WGBuddyActivity", WGBuddyActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception 
    {
        super.setUp();
        mActivity = this.getActivity();
        tShoppinglist = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_shoppingListButton);
        tTaskdistributor = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_taskDistributorButton);
        tPreferences = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_preferencesButton);
        tInvite = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_addButton);
        tMessenger = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_messengerButton);
        tLogout = (Button) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_logoutButton);
        tHeading = (TextView) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.main_headingText);
	}

	public void testPreconditions() 
	{
		assertNotNull(tShoppinglist);
		assertNotNull(tTaskdistributor);
		assertNotNull(tPreferences);
		assertNotNull(tInvite);
		assertNotNull(tMessenger);
		assertNotNull(tLogout);
		assertNotNull(tHeading);	
	}	
}
