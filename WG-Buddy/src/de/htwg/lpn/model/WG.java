package de.htwg.lpn.model;

import de.htwg.lpn.wgbuddy.Store;

public class WG extends ObjectBase
{
	public WG(Store store)
	{
		this.store = store;
		phpPage = "wg.php";
		arrayName = "WG";
	}
}