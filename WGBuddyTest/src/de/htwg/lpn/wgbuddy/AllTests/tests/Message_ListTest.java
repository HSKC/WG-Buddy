package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import de.htwg.lpn.wgbuddy.Message_List;

public class Message_ListTest extends ActivityInstrumentationTestCase2<Message_List> 
{
	private ListView messageList;
	private Message_List activity;
	
	public Message_ListTest()
	{
		super("de.htwg.lpn.wgbuddy.Message_List", Message_List.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{
		super.setUp();
		activity = this.getActivity();
		messageList = (ListView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.messengerList);
	}
	
	public void testPreselection()
	{
		assertNotNull(messageList);
	}
}
