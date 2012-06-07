package de.htwg.lpn.wgbuddy.test;

import java.util.TreeMap;

import android.test.ActivityInstrumentationTestCase2;
import de.htwg.lpn.wgbuddy.WGBuddyActivity;
import de.htwg.lpn.wgbuddy.utility.RandomUser;



public class testRandomUser extends ActivityInstrumentationTestCase2<WGBuddyActivity> 
{
	
	private WGBuddyActivity mActivity;
	private RandomUser rand;

	public testRandomUser() {
		super("de.htwg.lpn.wgbuddy.WGBuddyActivity", WGBuddyActivity.class);

	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        rand = new RandomUser();
	}

	public void testcreatePreselectionMap()
	{
		TreeMap<String, Double> testMap = new TreeMap<String, Double>();
		testMap.put("Felix", 0.0);
		assertEquals("Felix", rand.getRandomUser(testMap));
	}
}
