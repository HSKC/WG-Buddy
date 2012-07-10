package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import de.htwg.lpn.wgbuddy.User_Create;

public class User_CreateTest extends ActivityInstrumentationTestCase2<User_Create> 
{
	private TextView usernameTextView;
	private TextView emailTextView;
	private TextView email2TextView;
	private TextView passwordTextView;
	private TextView password2TextView;
	private User_Create activity;

	
	public User_CreateTest()
	{
		super("de.htwg.lpn.wgbuddy.User_Create", User_Create.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		usernameTextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_user_usernameEdit);
		emailTextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_user_emailEdit);
		email2TextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_user_email2Edit);
		passwordTextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_user_passwordEdit);
		password2TextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_user_password2Edit);
	}
	
	public void testPreconditions()
	{
		assertNotNull(usernameTextView);
		assertNotNull(emailTextView);
		assertNotNull(email2TextView);
		assertNotNull(passwordTextView);
		assertNotNull(password2TextView);
	}

}
