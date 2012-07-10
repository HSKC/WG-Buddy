package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.wgbuddy.User_Activate;

public class User_ActivateTest extends ActivityInstrumentationTestCase2<User_Activate> 
{
	private Button sendButton;
	private Button newKeyButton;
	private EditText keyEditText;
	private User_Activate activity;
	
	public User_ActivateTest()
	{
		super("de.htwg.lpn.wgbuddy.User_Activate", User_Activate.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		sendButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.activate_user_send);
		newKeyButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.activate_user_newKey);
		keyEditText = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.activate_user_key);
	}
	
	public void testPreconditions()
	{
		assertNotNull(sendButton);
		assertNotNull(newKeyButton);
		assertNotNull(keyEditText);
	}

}
