package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.Store;

public class ShoppingItem extends ObjectBase
{
	public ShoppingItem(Store store)
	{
		this.store = store;
		phpPage = "shopping.php";
		arrayName = "Item";
	}	
}
