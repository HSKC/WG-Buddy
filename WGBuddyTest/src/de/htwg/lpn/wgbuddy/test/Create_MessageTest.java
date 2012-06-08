package de.htwg.lpn.wgbuddy.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import de.htwg.lpn.wgbuddy.Create_Message;
import de.htwg.lpn.wgbuddy.R;

public class Create_MessageTest extends	ActivityInstrumentationTestCase2<Create_Message> 
{
	private Button tSend;
	private EditText tTitle;
	private EditText tMessage;
	private Create_Message mActivity;

	public Create_MessageTest(Class<Create_Message> activityClass) 
	{
		super("de.htwg.lpn.wgbuddy.Create_Message", Create_Message.class);
	}


	protected void setUp() throws Exception 
	{
		super.setUp();
        mActivity = this.getActivity();
        tSend = (Button) mActivity.findViewById(R.id.messagecreate_sendButton);
        tTitle = (EditText) mActivity.findViewById(R.id.messagecreate_titleEdit);
        tMessage = (EditText) mActivity.findViewById(R.id.messagecreate_messageEdit);
	}


	public void testPreconditions() 
	{
		assertNotNull(tSend);
		assertNotNull(tTitle);
		assertNotNull(tMessage);
	}

}
