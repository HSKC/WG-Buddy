package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import de.htwg.lpn.wgbuddy.WG_Create;

public class WG_CreateTest extends ActivityInstrumentationTestCase2<WG_Create> 
{
	private TextView nameTextView;
	private TextView passwordTextView;
	private TextView password2TextView;
	private WG_Create activity;
	
	public WG_CreateTest()
	{
		super("de.htwg.lpn.wgbuddy.WG_Create", WG_Create.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		nameTextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_wg_nameEdit);
		passwordTextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_wg_passwordEdit);
		password2TextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.create_wg_password2Edit);
	}
	
	public void testPreconditions()
	{
		assertNotNull(nameTextView);
		assertNotNull(passwordTextView);
		assertNotNull(password2TextView);
	}
}
