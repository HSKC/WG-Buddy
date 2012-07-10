package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.wgbuddy.WG_Login;

public class WG_LoginTest extends ActivityInstrumentationTestCase2<WG_Login> 
{
	private EditText nameTextView;
	private EditText passwordTextView;
	private Button connectButton;
	private Button createButton;
	private WG_Login activity;
	
	public WG_LoginTest()
	{
		super("de.htwg.lpn.wgbuddy.WG_Login", WG_Login.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		nameTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.wgpref_nameEdit);
		passwordTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.wgpref_passwordEdit);
		connectButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.wgpref_connectButton);
		createButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.wgpref_createButton);
	}
	
	public void testPreconditions()
	{
		assertNotNull(nameTextView);
		assertNotNull(passwordTextView);
		assertNotNull(connectButton);
		assertNotNull(createButton);
	}

}
