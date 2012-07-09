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
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.ShoppingItem;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

public class Shopping_List<V> extends Activity
{
	private SharedPreferences settings = null;

	private ListView shoppingList;
	private RadioGroup filterRadioGroup;

	private Integer type = 0;
	private Integer sort = 3;
	private Integer direction = 0;
	private Integer filter = 0;

	ArrayAdapter<CharSequence> typeAdapter = null;
	ArrayAdapter<CharSequence> sortAdapter = null;
	ArrayAdapter<CharSequence> directionAdapter = null;

	private ShoppingItem shoppingItem;
	private HashMap<Integer, HashMap<String, String>> shoppingMap = new HashMap<Integer, HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_list);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);
		Utilities.checkByPass(this, settings);

		shoppingItem = new ShoppingItem(settings);

		shoppingList = (ListView) findViewById(R.id.shoppinglist);
		filterRadioGroup = (RadioGroup) findViewById(R.id.shoppingList_radio_filter);

		typeAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListType_array, android.R.layout.simple_spinner_item);
		sortAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListSort_array, android.R.layout.simple_spinner_item);
		directionAdapter = ArrayAdapter.createFromResource(this, R.array.shoppingListDirection_array, android.R.layout.simple_spinner_item);

		filterRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.shoppingList_radio_filterAll)
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
		inflater.inflate(R.menu.shoppinglist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.add:
				intent = new Intent(Shopping_List.this, Shopping_Create.class);
				startActivity(intent);
				return true;

			case R.id.refresh:
				getList();
				return true;

			case R.id.settings:
				getShoppingListOptionsDialog();
				return true;

			case R.id.about:
				Dialogs.getAboutDialog(Shopping_List.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Shopping_List.this, Main.class);
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

		User user = new User(settings);
		ArrayList<HashMap<String, String>> list = shoppingItem.get(parameter);
		shoppingMap.clear();

		for (HashMap<String, String> map : list)
		{
			shoppingMap.put(Integer.valueOf(map.get("id")), map);

			if (map.get("userId").equals("0"))
			{
				map.put("username", "Keiner");
			}
			else
			{
				ArrayList<HashMap<String, String>> userList = user.get("?id=" + map.get("userId"));
				if (userList.size() > 0)
				{
					map.put("username", userList.get(0).get("username"));
				}
				else
				{
					map.put("username", "Keiner");
				}
			}
		}

		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.list_entry, new String[] { "id", "id", "name", "comment", "rating", "createdDate", "username", "status" }, new int[] {
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

					if (textRepresentation.compareTo("0") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_red);

					}
					else if (textRepresentation.compareTo("1") == 0)
					{
						ll.setBackgroundResource(R.drawable.border_green);
					}

					return true;
				}
				else if (view.getId() == R.id.list_completedButton)
				{
					ImageButton button = (ImageButton) view;
					final Integer id = Integer.valueOf(data.toString());
					button.setTag(id);
					button.setVisibility(View.VISIBLE);
					
					if (!shoppingMap.containsKey(id))
					{
						button.setVisibility(View.INVISIBLE);
						return true;
					}

					Integer status = Integer.valueOf(shoppingMap.get(id).get("status"));

					if (status == 1)
					{
						button.setVisibility(View.INVISIBLE);
					}
					

					button.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							ProgressDialog pd = ProgressDialog.show(Shopping_List.this, "", getString(R.string.utilities_pleaseWait));
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
											Utilities.toastMessage(Shopping_List.this, getString(R.string.shopping_alreadyDeleted));
											break;
										case 1:
											Utilities.toastMessage(Shopping_List.this, getString(R.string.shopping_alreadyPurchased));
											break;
										case 2:
											getList();
											Utilities.toastMessage(Shopping_List.this, getString(R.string.shopping_completedItem));
											break;
									}
								}
							};

							Callable<Message> callable = new Callable<Message>()
							{
								@Override
								public Message call() throws Exception
								{
									Message message = new Message();

									ArrayList<HashMap<String, String>> selectedItem = shoppingItem.get("?id=" + id.toString());

									if (selectedItem.size() == 0)
									{
										message.arg1 = 0;
										return message;
									}

									if (selectedItem.get(0).get("status").compareTo("1") == 0)
									{
										message.arg1 = 1;
										return message;
									}

									List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("userId", settings.getString("user_id", "")));
									nameValuePairs.add(new BasicNameValuePair("status", "1"));

									shoppingItem.update(id, nameValuePairs);

									if (Main.usepush)
									{
										GoogleService gs = new GoogleService(settings);
										gs.sendMessageToPhone("Shopping");
									}

									message.arg1 = 2;
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
							AlertDialog.Builder builder = new AlertDialog.Builder(Shopping_List.this);
							builder.setMessage(Shopping_List.this.getString(R.string.utilities_deleteQuestion));

							builder.setPositiveButton(Shopping_List.this.getString(R.string.utilities_delete), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int index)
								{
									ProgressDialog pd = ProgressDialog.show(Shopping_List.this, "", getString(R.string.utilities_pleaseWait));
									Handler handler = new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											super.handleMessage(msg);
											getList();
											Utilities.toastMessage(Shopping_List.this, getString(R.string.shopping_deletedItem));
										}
									};

									Callable<Message> callable = new Callable<Message>()
									{
										@Override
										public Message call()
										{
											shoppingItem.delete(id, Shopping_List.this);

											if (Main.usepush)
											{
												GoogleService gs = new GoogleService(settings);
												gs.sendMessageToPhone("Shopping");
											}

											return new Message();
										}
									};

									WorkerThread workerThread = new WorkerThread(callable, pd, handler);
									workerThread.start();
								}
							});

							builder.setNegativeButton(Shopping_List.this.getString(R.string.utilities_cancel), new DialogInterface.OnClickListener()
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
		shoppingList.setAdapter(sa);
	}

	public void getShoppingListOptionsDialog()
	{
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(Shopping_List.this);

		LayoutInflater inflater = (LayoutInflater) Shopping_List.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.list_optionsdialog, (ViewGroup) findViewById(R.id.shoppingList_optionsDialogLayout));

		builder.setView(layout);

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		alertDialog.getWindow().setLayout(300, 600);

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
