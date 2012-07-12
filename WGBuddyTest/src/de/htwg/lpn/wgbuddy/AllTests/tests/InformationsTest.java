package de.htwg.lpn.wgbuddy.AllTests.tests;

import de.htwg.lpn.wgbuddy.Informations;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;

public class InformationsTest extends ActivityInstrumentationTestCase2<Informations> 
{

	private Informations mActivity;
	private TextView mWgName;
	private TextView mWgAdmin;
	private TextView mWgPassword;
	private ListView mWgUserList;
	
	public InformationsTest() 
	{
		super("de.htwg.lpn.wgbuddy.Informations", Informations.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();        
		mActivity = this.getActivity();
		mWgName = (TextView) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.informations_wglist_name);
		mWgAdmin = (TextView) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.informations_wgAdmin);
		mWgPassword = (TextView) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.informations_wgPassword);
		mWgUserList = (ListView) mActivity.findViewById(de.htwg.lpn.wgbuddy.R.id.informations_wg_list);
	}

	public void testPreconditions() 
	{
		assertNotNull(mWgName);
		assertNotNull(mWgAdmin);
		assertNotNull(mWgPassword);
		assertNotNull(mWgUserList);
	}

}
