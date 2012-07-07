package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;

public class TaskList extends Activity
{
	private SharedPreferences settings;

	private ListView taskList;
	private RadioGroup filterRadioGroup;

	private Integer type = 0;
	private Integer sort = 0;
	private Integer direction = 0;
	private Integer filter = 0;

	ArrayAdapter<CharSequence> typeAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;
	ArrayAdapter<CharSequence> directionAdapter = null;
	
	private Task task;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasklist);

		settings = getSharedPreferences(WGBuddyActivity.PREFS_NAME, 0);
		Utilities.checkByPass(this, settings);
		
		task = new Task(settings);

		taskList = (ListView) findViewById(R.id.userTaskList);
		filterRadioGroup = (RadioGroup) findViewById(R.id.taskList_radio_filter);

		typeAdapter = ArrayAdapter.createFromResource(this,
				R.array.taskListType_array,
				android.R.layout.simple_spinner_item);
		sortAdapter = ArrayAdapter.createFromResource(this,
				R.array.shoppingListSort_array,
				android.R.layout.simple_spinner_item);
		directionAdapter = ArrayAdapter.createFromResource(this,
				R.array.shoppingListDirection_array,
				android.R.layout.simple_spinner_item);

		new ArrayList<String>();

		filterRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId)
					{
						if (checkedId == R.id.shoppingList_radio_filterAll)
						{
							filter = 0;
						} else
						{
							filter = 1;
						}
						getList();
					}
				});

		getList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tasklist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.add:
				intent = new Intent(this, Create_Task.class);
				startActivity(intent);
				return true;

			case R.id.refresh:
				getList();
				return true;

			case R.id.settings:
				getTaskListOptionsDialog();
				return true;

			case R.id.about:
				Dialogs.getAboutDialog(this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(this, WGBuddyActivity.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void getList()
	{
		String where = "";
		String order = "";
		String directionString = "";

		if (type == 1)
		{
			where = "status=0";
		} else if (type == 2)
		{
			where = "status=1";
		}

		if (filter == 1)
		{
			where += "&userId=" + settings.getString("user_id", "");
		}

		if (sort == 0)
		{
			order = "orderby=name";
		} else if (sort == 1)
		{
			order = "orderby=createdDate";
		} else if (sort == 2)
		{
			order = "orderby=rating";
		} else if (sort == 3)
		{
			order = "orderby=userId";
		}

		if (direction == 0)
		{
			directionString = "direction=ASC";
		} else
		{
			directionString = "direction=DESC";
		}

		String parameter = "?wgId=" + settings.getString("wg_id", "") + "&"
				+ ((where != "") ? where + "&" : "") + order + "&"
				+ directionString;

		User user = new User(settings);
		ArrayList<HashMap<String, String>> list = task.get(parameter);

		for (HashMap<String, String> map : list)
		{
			if (map.get("userId").equals("0"))
			{
				map.put("username", "Keiner");
			} else
			{
				ArrayList<HashMap<String, String>> userList = user.get("?id="
						+ map.get("userId"));
				if (userList.size() > 0)
				{
					map.put("username", userList.get(0).get("username"));
				} else
				{
					map.put("username", "Keiner");
				}
			}
		}

		SimpleAdapter sa = new SimpleAdapter(this, list,
				R.layout.shoppinglist_entry, new String[] { "id", "id", "name",
						"comment", "points", "createdDate", "username",
						"status" }, new int[] {
						R.id.shoppingListEntryCompletedButton,
						R.id.shoppingListEntryDeleteButton,
						R.id.shoppingBigText, R.id.shoppingSmallText,
						R.id.ratingBar, R.id.createdDate,
						R.id.shoppingUsername, R.id.shoppingList_Entry });

		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation)
			{
				if (view.getId() == R.id.ratingBar)
				{
					RatingBar rb = (RatingBar) view;
					rb.setRating(Float.valueOf(data.toString()));
					return true;
				} else if (view.getId() == R.id.shoppingList_Entry)
				{
					LinearLayout ll = (LinearLayout) view;

					if (textRepresentation.compareTo("0") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_red);

					} else if (textRepresentation.compareTo("1") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_green);
					}

					return true;
				} 
				else if (view.getId() == R.id.shoppingListEntryCompletedButton)
				{
					ImageButton button = (ImageButton) view;
					Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					
					ArrayList<HashMap<String, String>> t = task.get("?id=" + id.toString());	
					
					if(t.size() == 0)
					{
						button.setVisibility(View.INVISIBLE);
						return true;
					}
					
					if(Integer.valueOf(t.get(0).get("userId")) != Integer.valueOf(settings.getString("user_id", "")) || Integer.valueOf(t.get(0).get("status")) == 1)
					{
						button.setVisibility(View.INVISIBLE);
					}
					
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Integer id = (Integer) v.getTag();

							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("userId",
									settings.getString("user_id", "")));
							nameValuePairs.add(new BasicNameValuePair("status",
									"1"));

							task.update(id, nameValuePairs);

							getList();
						}
					});
					return true;
				} 
				else if (view.getId() == R.id.shoppingListEntryDeleteButton)
				{
					ImageButton button = (ImageButton) view;
					Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					
					ArrayList<HashMap<String, String>> t = task.get("?id=" + id.toString());	
					
					if(t.size() == 0)
					{
						button.setVisibility(View.INVISIBLE);
						return true;
					}
					
					if(Integer.valueOf(t.get(0).get("userId")) != Integer.valueOf(settings.getString("user_id", "")))
					{
						button.setVisibility(View.INVISIBLE);
					}
					
					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Integer id = (Integer) v.getTag();
							task.delete(id, TaskList.this);

							getList();
						}
					});
					return true;
				} else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		sa.notifyDataSetChanged();
		taskList.setAdapter(sa);

		taskList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
//				String id = idList.get(arg2);
//				String par = "?wgId=" + settings.getString("wg_id", "")
//						+ "&userId=" + settings.getString("user_id", "")
//						+ "&id=" + id;
//				ArrayList<HashMap<String, String>> selectedTask = null;// =
//																		// list.get(par);
//
//				Intent intent = new Intent(TaskList.this,
//						UserTaskCompleteEntry.class);
//				intent.putExtra("name", selectedTask.get(0).get("name"));
//				intent.putExtra("comment", selectedTask.get(0).get("comment"));
//				intent.putExtra("points", selectedTask.get(0).get("points"));
//				intent.putExtra("status", selectedTask.get(0).get("status"));
//				startActivity(intent);
			}
		});
	}

	public void getTaskListOptionsDialog()
	{
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(TaskList.this);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater
				.inflate(
						R.layout.list_optionsdialog,
						(ViewGroup) findViewById(R.id.shoppingList_optionsDialogLayout));
		

		builder.setView(layout);

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner typeSpinner = (Spinner) layout
				.findViewById(R.id.listTypeSpinner);

		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setSelection(type);
		typeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id)
					{
						type = pos;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
					}
				});

		sortAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner sortSpinner = (Spinner) layout
				.findViewById(R.id.shoppingListSortSpinner);

		sortSpinner.setAdapter(sortAdapter);
		sortSpinner.setSelection(sort);
		sortSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id)
					{
						sort = pos;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
					}
				});

		directionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner directionSpinner = (Spinner) layout
				.findViewById(R.id.listDirectionSpinner);

		directionSpinner.setAdapter(directionAdapter);
		directionSpinner.setSelection(direction);
		directionSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id)
					{
						direction = pos;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
					}
				});

		Button okButton = (Button) layout
				.findViewById(R.id.shoppingListOptionsDialogButton);
		okButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();
				getList();
			}
		});
	}
}
