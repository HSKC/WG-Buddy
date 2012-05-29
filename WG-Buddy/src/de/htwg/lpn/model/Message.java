package de.htwg.lpn.model;

import android.content.SharedPreferences;

public class Message extends ObjectBase
{
	public Message(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "message.php";
		arrayName = "Message";
	}
}
