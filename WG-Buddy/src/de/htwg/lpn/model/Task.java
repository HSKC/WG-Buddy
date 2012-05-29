package de.htwg.lpn.model;

import android.content.SharedPreferences;

public class Task extends ObjectBase
{
	public Task(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "task.php";
		arrayName = "Task";
	}	
}
