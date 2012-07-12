package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.wgbuddy.Message_Create;

public class Message_CreateTest extends	ActivityInstrumentationTestCase2<Message_Create>
{
	private Button sendButton;
	private EditText titleEditText;
	private EditText messageEditText;
	private Message_Create activity;
	
	public Message_CreateTest()
	{
		super("de.htwg.lpn.wgbuddy.Message_Create", Message_Create.class);
	}
	
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		sendButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.messagecreate_sendButton);
		titleEditText = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.messagecreate_titleEdit);
		messageEditText = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.messagecreate_messageEdit);
	}
	
	public void testPreconditions()
	{
		assertNotNull(messageEditText);
		assertNotNull(sendButton);
		assertNotNull(titleEditText);
	}
}
