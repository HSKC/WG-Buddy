package de.htwg.lpn.model;

import android.content.SharedPreferences;

public class User extends ObjectBase
{
	public User(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "user.php";
		arrayName = "User";
	}
}
