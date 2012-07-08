package de.htwg.lpn.model;

import android.content.SharedPreferences;

public class ShoppingItem extends ObjectBase
{
	public ShoppingItem(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "shopping.php";
		arrayName = "Item";
	}
}
