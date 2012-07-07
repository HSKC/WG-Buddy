package de.htwg.lpn.model;

import android.content.SharedPreferences;
import de.htwg.lpn.wgbuddy.utility.JSON;

public class User extends ObjectBase
{
	public User(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "user.php";
		arrayName = "User";
	}	
}
