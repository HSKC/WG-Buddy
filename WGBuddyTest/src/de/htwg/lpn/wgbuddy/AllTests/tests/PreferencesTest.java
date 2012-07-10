package de.htwg.lpn.wgbuddy.AllTests.tests;

import de.htwg.lpn.wgbuddy.Preferences;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class PreferencesTest extends ActivityInstrumentationTestCase2<Preferences> 
{
	private Button changePasswordButton;
	private Button leaveWGButton;
	private Button changeAdminButton;
	private Button changeWGPasswordButton;
	private Preferences activity;
	
	public PreferencesTest()
	{
		super("de.htwg.lpn.wgbuddy.Preferences", Preferences.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		changePasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.preferences_changePasswordButton);
		leaveWGButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.preferences_leaveButton);
		changeAdminButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.preferences_changeAdminButton);
		changeWGPasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.preferences_changeWGPasswordButton);
	}
	
	public void testPreconditions()
	{
		assertNotNull(changePasswordButton);
		assertNotNull(leaveWGButton);
		assertNotNull(changeAdminButton);
		assertNotNull(changeWGPasswordButton);
	}
}
