package de.htwg.lpn.wgbuddy.AllTests.activitytests;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
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
	
	public void testOnOptionsItemSelectedAbout()
	{
		boolean erg = activity.onOptionsItemSelected(new MenuItem()
		{

			@Override
			public char getAlphabeticShortcut()
			{
				return 0;
			}

			@Override
			public int getGroupId() 
			{
				return 0;
			}

			@Override
			public Drawable getIcon() 
			{
				return null;
			}

			@Override
			public Intent getIntent() 
			{
				return null;
			}

			@Override
			public int getItemId() 
			{
				return de.htwg.lpn.wgbuddy.R.id.about;
			}

			@Override
			public ContextMenuInfo getMenuInfo() 
			{
				return null;
			}

			@Override
			public char getNumericShortcut() 
			{
				return 0;
			}

			@Override
			public int getOrder() 
			{
				return 0;
			}

			@Override
			public SubMenu getSubMenu() 
			{
				return null;
			}

			@Override
			public CharSequence getTitle() 
			{
				return null;
			}

			@Override
			public CharSequence getTitleCondensed() 
			{
				return null;
			}

			@Override
			public boolean hasSubMenu() 
			{
				return false;
			}

			@Override
			public boolean isCheckable() 
			{
				return false;
			}

			@Override
			public boolean isChecked() 
			{
				return false;
			}

			@Override
			public boolean isEnabled() 
			{
				return false;
			}

			@Override
			public boolean isVisible() 
			{
				return false;
			}

			@Override
			public MenuItem setAlphabeticShortcut(char alphaChar) 
			{
				return null;
			}

			@Override
			public MenuItem setCheckable(boolean checkable) 
			{
				return null;
			}

			@Override
			public MenuItem setChecked(boolean checked) 
			{
				return null;
			}

			@Override
			public MenuItem setEnabled(boolean enabled) 
			{
				return null;
			}

			@Override
			public MenuItem setIcon(Drawable icon) 
			{
				return null;
			}

			@Override
			public MenuItem setIcon(int iconRes) 
			{
				return null;
			}

			@Override
			public MenuItem setIntent(Intent intent) 
			{
				return null;
			}

			@Override
			public MenuItem setNumericShortcut(char numericChar) 
			{
				return null;
			}

			@Override
			public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) 
			{
				return null;
			}

			@Override
			public MenuItem setShortcut(char numericChar, char alphaChar) 
			{
				return null;
			}

			@Override
			public MenuItem setTitle(CharSequence title) 
			{
				return null;
			}

			@Override
			public MenuItem setTitle(int title) 
			{
				return null;
			}

			@Override
			public MenuItem setTitleCondensed(CharSequence title) 
			{
				return null;
			}

			@Override
			public MenuItem setVisible(boolean visible)
			{
				return null;
			}
			
		});
		
		assertEquals(true, erg);
	}
	
	public void testOnOptionsItemSelectedOther()
	{
		boolean erg = activity.onOptionsItemSelected(new MenuItem()
		{

			@Override
			public char getAlphabeticShortcut() 
			{
				return 0;
			}

			@Override
			public int getGroupId() 
			{
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				return null;
			}

			@Override
			public Intent getIntent() 
			{
				return null;
			}

			@Override
			public int getItemId() 
			{
				return 1;
			}

			@Override
			public ContextMenuInfo getMenuInfo() 
			{
				return null;
			}

			@Override
			public char getNumericShortcut() 
			{
				return 0;
			}

			@Override
			public int getOrder() 
			{
				return 0;
			}

			@Override
			public SubMenu getSubMenu() 
			{
				return null;
			}

			@Override
			public CharSequence getTitle() 
			{
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				return null;
			}

			@Override
			public boolean hasSubMenu() 
			{
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				return false;
			}

			@Override
			public boolean isChecked()
			{
				return false;
			}

			@Override
			public boolean isEnabled() 
			{
				return false;
			}

			@Override
			public boolean isVisible() 
			{
				return false;
			}

			@Override
			public MenuItem setAlphabeticShortcut(char alphaChar)
			{
				return null;
			}

			@Override
			public MenuItem setCheckable(boolean checkable)
			{
				return null;
			}

			@Override
			public MenuItem setChecked(boolean checked) 
			{
				return null;
			}

			@Override
			public MenuItem setEnabled(boolean enabled) 
			{
				return null;
			}

			@Override
			public MenuItem setIcon(Drawable icon) 
			{
				return null;
			}

			@Override
			public MenuItem setIcon(int iconRes)
			{
				return null;
			}

			@Override
			public MenuItem setIntent(Intent intent)
			{
				return null;
			}

			@Override
			public MenuItem setNumericShortcut(char numericChar)
			{
				return null;
			}

			@Override
			public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) 
			{
				return null;
			}

			@Override
			public MenuItem setShortcut(char numericChar, char alphaChar) 
			{
				return null;
			}

			@Override
			public MenuItem setTitle(CharSequence title) 
			{
				return null;
			}

			@Override
			public MenuItem setTitle(int title) 
			{
				return null;
			}

			@Override
			public MenuItem setTitleCondensed(CharSequence title)
			{
				return null;
			}

			@Override
			public MenuItem setVisible(boolean visible)
			{
				return null;
			}
			
		});
		
		assertEquals(false, erg);
	}
}
