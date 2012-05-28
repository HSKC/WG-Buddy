package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.Store;

public class User extends ObjectBase
{
	public User(Store store)
	{
		this.store = store;
		phpPage = "user.php";
		arrayName = "User";
	}
}
