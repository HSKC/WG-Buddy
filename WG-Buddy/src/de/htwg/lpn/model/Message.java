package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.Store;

public class Message extends ObjectBase
{
	public Message(Store store)
	{
		this.store = store;
		phpPage = "message.php";
		arrayName = "Message";
	}
}
