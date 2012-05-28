package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.Store;

public class Task extends ObjectBase
{
	public Task(Store store)
	{
		this.store = store;
		phpPage = "task.php";
		arrayName = "Task";
	}	
}
