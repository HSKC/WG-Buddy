package de.htwg.lpn.wgbuddy.AllTests.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import de.htwg.lpn.wgbuddy.Task_Create;

public class Task_CreateTest extends ActivityInstrumentationTestCase2<Task_Create> 
{
	private TextView name_TextView;
	private TextView comment_TextView;
	private RatingBar points_RatingBar;
	private Button start_Button;
	private Button userList_Button;
	private ListView user_ListView;
	private Task_Create activity;
	
	public Task_CreateTest()
	{
		super("de.htwg.lpn.wgbuddy.Task_Create", Task_Create.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		name_TextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.taskNameText);
		comment_TextView = (TextView) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userTaskComment);
		points_RatingBar = (RatingBar) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.taskRatingBar);
		start_Button = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.usertaskgoButton);
		userList_Button = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.task_userListButton); 
	}
	
	public void testPreconditions()
	{
		assertNotNull(name_TextView);
		assertNotNull(comment_TextView);
		assertNotNull(points_RatingBar);
		assertNotNull(start_Button);
		assertNotNull(userList_Button);
	}
}
