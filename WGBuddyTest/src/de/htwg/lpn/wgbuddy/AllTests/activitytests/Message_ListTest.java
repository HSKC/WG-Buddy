package de.htwg.lpn.wgbuddy.AllTests.activitytests;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
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
