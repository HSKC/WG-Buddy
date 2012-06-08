package de.htwg.lpn.wgbuddy.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import de.htwg.lpn.wgbuddy.R;
import de.htwg.lpn.wgbuddy.TaskDistributor;


public class TaskDistributorTest extends ActivityInstrumentationTestCase2<TaskDistributor> {
	
	private ListView tUserList;
	private Button tStart;
	private TaskDistributor mActivity;
	
	public TaskDistributorTest()
	{
		super("de.htwg.lpn.wgbuddy.TaskDistributor", TaskDistributor.class);
	}

	public void setUp() throws Exception {
		 mActivity = this.getActivity();
		 tUserList = (ListView) mActivity.findViewById(R.id.taskUserList);
		 tStart = (Button) mActivity.findViewById(R.id.taskGoButton);
	}

	public void test() {
		assertNotNull(tUserList);
		assertNotNull(tStart);
	}

}
