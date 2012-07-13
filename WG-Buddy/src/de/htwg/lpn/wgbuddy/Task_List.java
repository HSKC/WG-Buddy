package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.TextView;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

public class Task_List extends Activity
{
	private SharedPreferences settings;

	private ListView taskList;
	private RadioGroup filterRadioGroup;

	private Integer type = 0;
	private Integer sort = 1;
	private Integer direction = 1;
	private Integer filter = 0;

	ArrayAdapter<CharSequence> typeAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;
	ArrayAdapter<CharSequence> directionAdapter = null;

	private Task task;
	private HashMap<Integer, HashMap<String, String>> taskMap = new HashMap<Integer, HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);
		Utilities.checkByPass(this, settings);

		task = new Task(settings);

		taskList = (ListView) findViewById(R.id.userTaskList);
		filterRadioGroup = (RadioGroup) findViewById(R.id.taskList_radio_filter);

		typeAdapter = ArrayAdapter.createFromResource(this, R.array.taskListType_array, android.R.layout.simple_spinner_item);
		sortAdapter = ArrayAdapter.createFromResource(this, R.array.taskListSort_array, android.R.layout.simple_spinner_item);
		directionAdapter = ArrayAdapter.createFromResource(this, R.array.listDirection_array, android.R.layout.simple_spinner_item);

		new ArrayList<String>();

		filterRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.taskList_radio_filterAll)
				{
					filter = 0;
				}
				else
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
				intent = new Intent(this, Task_Create.class);
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
				intent = new Intent(this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(this, Main.class);
			startActivity(intent);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getList()
	{
		String where = "";
		String order = "";
		String directionString = "";

		if (type == 1)
		{
			where = "status=0";
		}
		else if (type == 2)
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
		}
		else if (sort == 1)
		{
			order = "orderby=createdDate";
		}
		else if (sort == 2)
		{
			order = "orderby=points";
		}
		else if (sort == 3)
		{
			order = "orderby=userId";
		}

		if (direction == 0)
		{
			directionString = "direction=ASC";
		}
		else
		{
			directionString = "direction=DESC";
		}

		String parameter = "?wgId=" + settings.getString("wg_id", "") + "&" + ((where != "") ? where + "&" : "") + order + "&" + directionString;
		ArrayList<HashMap<String, String>> list = task.get(parameter);

		Utilities.addUsernameToList(list, taskMap, settings);

		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.list_entry, new String[] { "id", "id", "name", "comment", "points", "createdDate", "username", "status" }, new int[] {
		R.id.list_completedButton, R.id.list_deleteButton, R.id.list_name, R.id.list_comment, R.id.list_rating, R.id.list_createdDate, R.id.list_username, R.id.list_entry });

		ViewBinder vb = new ViewBinder()
		{
			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.list_rating)
				{
					RatingBar rb = (RatingBar) view;
					rb.setRating(Float.valueOf(data.toString()));
					return true;
				}
				else if (view.getId() == R.id.list_createdDate)
				{
					String dateTime = Utilities.getDateTimeFormat((String) data);
					TextView timeview = (TextView) view;
					timeview.setText(dateTime);

					return true;
				}
				else if (view.getId() == R.id.list_entry)
				{
					LinearLayout ll = (LinearLayout) view;

					// Get Color for index of User. so everyone gets a different
					// color
					String[] colors = getResources().getStringArray(R.array.messengercolors_array);
					int color = 0;

					if (textRepresentation.compareTo("0") == 0)
					{
						color = Color.parseColor(colors[0]);
					}
					else
					{
						color = Color.parseColor(colors[2]);
					}

					// Get the white Shape out of the XML File an add a
					// Colofilter with the Color
					Drawable d = getResources().getDrawable(R.drawable.message_border);
					d.setColorFilter(color, Mode.MULTIPLY);

					// set the Shape as Background
					ll.setBackgroundDrawable(d);

					return true;
				}
				else if (view.getId() == R.id.list_completedButton)
				{
					ImageButton button = (ImageButton) view;
					final Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					button.setVisibility(View.VISIBLE);

					if (!taskMap.containsKey(id))
					{
						button.setVisibility(View.INVISIBLE);
						return true;
					}

					Integer userId = Integer.valueOf(taskMap.get(id).get("userId"));
					Integer status = Integer.valueOf(taskMap.get(id).get("status"));

					if (userId != Integer.valueOf(settings.getString("user_id", "")) || status == 1)
					{
						button.setVisibility(View.INVISIBLE);
					}

					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							ProgressDialog pd = ProgressDialog.show(Task_List.this, "", getString(R.string.utilities_pleaseWait));
							Handler handler = new Handler()
							{
								@Override
								public void handleMessage(Message msg)
								{
									super.handleMessage(msg);

									switch (msg.arg1)
									{
										case 0:
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_alreadyDeleted));
											break;
										case 1:
											Utilities.toastMessage(Task_List.this, getString(R.string.task_alreadyDone));
											break;
										case 2:
											Utilities.toastMessage(Task_List.this, getString(R.string.task_otherUser));
											break;
										case 3:
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_completedItem));
											break;
									}
								}
							};

							Callable<Message> callable = new Callable<Message>()
							{
								@Override
								public Message call() throws Exception
								{
									Message message = Message.obtain();

									ArrayList<HashMap<String, String>> selectedTask = task.get("?id=" + id.toString());

									if (selectedTask.size() == 0)
									{
										message.arg1 = 0;
										return message;
									}

									if (selectedTask.get(0).get("status").compareTo("1") == 0)
									{
										message.arg1 = 1;
										return message;
									}

									if (selectedTask.get(0).get("userId").compareTo(settings.getString("user_id", "")) != 0)
									{
										message.arg1 = 2;
										return message;
									}

									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "")));
									nameValuePairs.add(new BasicNameValuePair("status", "1"));

									task.update(id, nameValuePairs);

									User user = new User(settings);
									ArrayList<HashMap<String, String>> selectedUser = user.get("?id=" + settings.getString("user_id", ""));

									Float points = Float.valueOf(selectedUser.get(0).get("points")) + Float.valueOf(selectedTask.get(0).get("points"));

									ArrayList<NameValuePair> userNameValuePairs = new ArrayList<NameValuePair>();
									userNameValuePairs.add(new BasicNameValuePair("points", points.toString()));

									user.update(Integer.valueOf(settings.getString("user_id", "")), userNameValuePairs);

									if (Main.usepush)
									{
										GoogleService gs = new GoogleService(settings);
										gs.sendMessageToPhone("Task");
									}

									message.arg1 = 3;
									return message;
								}
							};

							WorkerThread workerThread = new WorkerThread(callable, pd, handler);
							workerThread.start();
						}
					});
					return true;
				}
				else if (view.getId() == R.id.list_deleteButton)
				{
					ImageButton button = (ImageButton) view;
					final Integer id = Integer.valueOf(data.toString());
					button.setTag(id);

					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(Task_List.this);
							builder.setMessage(Task_List.this.getString(R.string.utilities_deleteQuestion));

							builder.setPositiveButton(Task_List.this.getString(R.string.utilities_delete), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int index)
								{
									ProgressDialog pd = ProgressDialog.show(Task_List.this, "", getString(R.string.utilities_pleaseWait));
									Handler handler = new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											super.handleMessage(msg);
											getList();
											Utilities.toastMessage(Task_List.this, getString(R.string.task_deletedItem));
										}
									};

									Callable<Message> callable = new Callable<Message>()
									{
										@Override
										public Message call()
										{
											task.delete(id, Task_List.this);

											if (Main.usepush)
											{
												GoogleService gs = new GoogleService(settings);
												gs.sendMessageToPhone("Task");
											}

											return Message.obtain();
										}
									};

									WorkerThread workerThread = new WorkerThread(callable, pd, handler);
									workerThread.start();
								}
							});

							builder.setNegativeButton(Task_List.this.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{

								}
							});

							AlertDialog alert = builder.create();
							alert.show();
						}
					});
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		sa.notifyDataSetChanged();
		taskList.setAdapter(sa);
	}

	public void getTaskListOptionsDialog()
	{
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(Task_List.this);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.list_optionsdialog, (ViewGroup) findViewById(R.id.shoppingList_optionsDialogLayout));

		builder.setView(layout);

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner typeSpinner = (Spinner) layout.findViewById(R.id.listTypeSpinner);

		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setSelection(type);
		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				type = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner sortSpinner = (Spinner) layout.findViewById(R.id.shoppingListSortSpinner);

		sortSpinner.setAdapter(sortAdapter);
		sortSpinner.setSelection(sort);
		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				sort = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner directionSpinner = (Spinner) layout.findViewById(R.id.listDirectionSpinner);

		directionSpinner.setAdapter(directionAdapter);
		directionSpinner.setSelection(direction);
		directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				direction = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

		Button okButton = (Button) layout.findViewById(R.id.shoppingListOptionsDialogButton);
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
