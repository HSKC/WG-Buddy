package de.htwg.lpn.wgbuddy.AllTests.tests;

import de.htwg.lpn.wgbuddy.Main;
import de.htwg.lpn.wgbuddy.User_Login;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class User_LoginTest extends ActivityInstrumentationTestCase2<User_Login> 
{
	private EditText usernameTextView;
	private EditText passwordTextView;
	private Button saveButton;
	private Button createButton;
	private Button activateButton;
	private Button lostPasswordButton;
	private Button changePasswordButton;
	private User_Login activity;
	
	public User_LoginTest()
	{
		super("de.htwg.lpn.wgbuddy.User_Login", User_Login.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		usernameTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_nameEdit);
		passwordTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_passwordEdit);
		saveButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_saveButton);
		createButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_createButton);
		activateButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_activateButton);
		lostPasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_passwordButton);
		changePasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_changePasswordButton);
	}
	
	public void testPreconditions()
	{
		assertNotNull(usernameTextView);
		assertNotNull(passwordTextView);
		assertNotNull(saveButton);
		assertNotNull(createButton);
		assertNotNull(activateButton);
		assertNotNull(lostPasswordButton);
		assertNotNull(changePasswordButton);
		
	}

}
