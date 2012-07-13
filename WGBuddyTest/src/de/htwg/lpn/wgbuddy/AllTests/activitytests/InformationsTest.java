package de.htwg.lpn.wgbuddy.AllTests.activitytests;

import de.htwg.lpn.wgbuddy.Informations;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ContextMenu.ContextMenuInfo;
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

	public void testOnOptionsItemSelectedAbout()
	{
		boolean erg = mActivity.onOptionsItemSelected(new MenuItem()
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
		boolean erg = mActivity.onOptionsItemSelected(new MenuItem()
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