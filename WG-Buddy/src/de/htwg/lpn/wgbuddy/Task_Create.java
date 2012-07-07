package de.htwg.lpn.wgbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import de.htwg.lpn.model.GoogleService;
import de.htwg.lpn.model.Mail;
import de.htwg.lpn.model.Task;
import de.htwg.lpn.model.User;
import de.htwg.lpn.wgbuddy.utility.Dialogs;
import de.htwg.lpn.wgbuddy.utility.RandomUser;
import de.htwg.lpn.wgbuddy.utility.Utilities;
import de.htwg.lpn.wgbuddy.utility.WorkerThread;

public class Task_Create extends Activity
{
	private SharedPreferences settings;

	private TextView name_TextView;
	private TextView comment_TextView;
	private RatingBar points_RatingBar;
	private Button start_Button;

	private Button userList_Button;
	private ListView user_ListView;

	private Task task;
	private User user;

	private ArrayList<HashMap<String, String>> users;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_create);

		settings = getSharedPreferences(Main.PREFS_NAME, 0);

		name_TextView = (TextView) findViewById(R.id.taskNameText);
		comment_TextView = (TextView) findViewById(R.id.userTaskComment);
		points_RatingBar = (RatingBar) findViewById(R.id.taskRatingBar);
		start_Button = (Button) findViewById(R.id.usertaskgoButton);

		userList_Button = (Button) findViewById(R.id.task_userListButton);
		user_ListView = new ListView(this);

		task = new Task(settings);
		user = new User(settings);

		users = user.get("?wgId=" + settings.getString("wg_id", ""));
		SimpleAdapter sa = new SimpleAdapter(this, users, R.layout.task_user_entry, new String[] { "username" }, new int[] { R.id.task_userlistcheckbox });

		ViewBinder vb = new ViewBinder()
		{

			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (view.getId() == R.id.task_userlistcheckbox)
				{
					CheckBox cb = (CheckBox) view;
					cb.setText((CharSequence) data);
					cb.setChecked(true);
					return true;
				}
				else
				{
					return false;
				}
			}
		};
		sa.setViewBinder(vb);
		user_ListView.setAdapter(sa);

		userList_Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getTaskUserDialog();
			}
		});

		clickOnStartButton();
	}

	private void clickOnStartButton()
	{
		start_Button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String name = name_TextView.getText().toString().trim();
				String comment = comment_TextView.getText().toString().trim();

				if (name.compareTo("") != 0 && comment.compareTo("") != 0)
				{
					ProgressDialog pd = ProgressDialog.show(Task_Create.this, "", getString(R.string.utilities_pleaseWait));
					Handler handler = new Handler()
					{
						@Override
						public void handleMessage(Message msg)
						{
							super.handleMessage(msg);

							String name = (String) msg.obj;

							Utilities.toastMessage(Task_Create.this, getString(R.string.task_created) + name);

							Intent intent = new Intent(Task_Create.this, Task_List.class);
							startActivity(intent);
						}
					};

					Callable<Message> callable = new Callable<Message>()
					{
						@Override
						public Message call() throws Exception
						{
							Message message = new Message();

							ListAdapter adapter = user_ListView.getAdapter();
							Integer count = user_ListView.getChildCount();
							TreeMap<String, Double> checkedUser = new TreeMap<String, Double>();

							if (count == 0)
							{
								for (HashMap<String, String> map : users)
								{
									checkedUser.put(map.get("username"), Double.valueOf(map.get("points")));
								}
							}
							else
							{
								for (int i = 0; i < count; i++)
								{
									View entry = user_ListView.getChildAt(i);
									if (entry != null)
									{
										CheckBox checkbox = (CheckBox) entry.findViewById(R.id.task_userlistcheckbox);
										if (checkbox.isChecked())
										{
											@SuppressWarnings("unchecked")
											HashMap<String, String> userData = (HashMap<String, String>) adapter.getItem(i);
											checkedUser.put(userData.get("username"), Double.valueOf(userData.get("points")));
										}
									}
								}
							}

							RandomUser randomUser = RandomUser.getInstance();
							randomUser.setUserlist(checkedUser);

							String chosenUserName = randomUser.getRandomUser();

							message.obj = chosenUserName;

							createNewTask(chosenUserName);

							if (Main.usepush)
							{
								GoogleService gs = new GoogleService(settings);
								gs.sendMessageToPhone("Task");
							}

							return message;
						}

					};

					WorkerThread workerThread = new WorkerThread(callable, pd, handler);
					workerThread.start();
				}
				else
				{
					Utilities.toastMessage(Task_Create.this, getString(R.string.utilities_allFields));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.basic_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
			case R.id.about:
				Dialogs.getAboutDialog(Task_Create.this, settings);
				return true;

			case R.id.menu:
				intent = new Intent(Task_Create.this, Main.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void createNewTask(String chosenUserName)
	{
		String userId = findUserId(chosenUserName);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wgId", settings.getString("wg_id", "")));
		nameValuePairs.add(new BasicNameValuePair("userId", userId));
		nameValuePairs.add(new BasicNameValuePair("name", name_TextView.getText().toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("comment", comment_TextView.getText().toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("points", new Double(points_RatingBar.getRating()).toString()));
		nameValuePairs.add(new BasicNameValuePair("status", "0"));

		task.insert(nameValuePairs);

		if (Main.usepush)
		{
			GoogleService gs = new GoogleService(settings);
			gs.sendMessageToPhone("Task");
		}

		ArrayList<HashMap<String, String>> tasks = task.get("?wgId=" + settings.getString("wg_id", "") + "&userId=" + userId);
		sendMail(tasks);
	}

	private void sendMail(ArrayList<HashMap<String, String>> tasks)
	{
		Mail mail = new Mail(settings);
		mail.sendTask(tasks.get(0).get("id"));
	}

	protected String findUserId(String chosenUserName)
	{
		ArrayList<HashMap<String, String>> chosenUser = user.get("?wgId=" + settings.getString("wg_id", "") + "&username=" + chosenUserName);
		return chosenUser.get(0).get("id");
	}

	protected String findUserEmail(String chosenUserName)
	{
		ArrayList<HashMap<String, String>> chosenUser = user.get("?wgId=" + settings.getString("wg_id", "") + "&username=" + chosenUserName);
		return chosenUser.get(0).get("email");
	}

	public void getTaskUserDialog()
	{
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(this);

		builder.setView(user_ListView);
		builder.setPositiveButton(getString(R.string.utilities_ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
