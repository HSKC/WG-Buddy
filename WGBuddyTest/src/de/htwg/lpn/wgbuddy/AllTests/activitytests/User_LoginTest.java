package de.htwg.lpn.wgbuddy.AllTests.activitytests;

import de.htwg.lpn.wgbuddy.Main;
import de.htwg.lpn.wgbuddy.User_Login;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class User_LoginTest extends ActivityInstrumentationTestCase2<User_Login> 
{
	private EditText usernameTextView;
	private EditText passwordTextView;
	private Button saveButton;
	private Button createButton;
	private Button activateButton;
	private Button lostPasswordButton;
	private Button changePasswordButton;
	private User_Login activity;
	
	public User_LoginTest()
	{
		super("de.htwg.lpn.wgbuddy.User_Login", User_Login.class);
	}
	
	@Override    
	protected void setUp() throws Exception 
	{        
		super.setUp();
		activity = this.getActivity();
		usernameTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_nameEdit);
		passwordTextView = (EditText) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_passwordEdit);
		saveButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_saveButton);
		createButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_createButton);
		activateButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_activateButton);
		lostPasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_passwordButton);
		changePasswordButton = (Button) activity.findViewById(de.htwg.lpn.wgbuddy.R.id.userpref_changePasswordButton);
	}
	
	public void testPreconditions()
	{
		assertNotNull(usernameTextView);
		assertNotNull(passwordTextView);
		assertNotNull(saveButton);
		assertNotNull(createButton);
		assertNotNull(activateButton);
		assertNotNull(lostPasswordButton);
		assertNotNull(changePasswordButton);
		
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
