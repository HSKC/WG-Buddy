package de.htwg.lpn.wgbuddy.AllTests.utility;

import java.util.TreeMap;

import android.test.AndroidTestCase;
import de.htwg.lpn.wgbuddy.utility.RandomUser;

public class RandomUserTest extends AndroidTestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testRandomUser()
	{
		TreeMap<String, Double> userList = new TreeMap<String, Double>();

		userList.put("Felix", 19.);
		assertEquals("Felix", RandomUser.getRandomUser(userList));
	}
}
