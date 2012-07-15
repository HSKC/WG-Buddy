package de.htwg.lpn.wgbuddy.AllTests.utility;

import java.util.TreeMap;

import de.htwg.lpn.wgbuddy.utility.RandomUser;
import android.test.AndroidTestCase;


public class RandomUserTest extends AndroidTestCase 
{
	private RandomUser random;

	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();        
		random = new RandomUser();
	}

	public void testRandomUser()
	{
		TreeMap<String, Double> userList = new TreeMap<String, Double>();
		
		userList.put("Felix", 19.);
		assertEquals("Felix", random.getRandomUser(userList));
	}
}
