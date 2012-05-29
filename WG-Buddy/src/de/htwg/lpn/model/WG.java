package de.htwg.lpn.model;

import android.content.SharedPreferences;

public class WG extends ObjectBase
{
	public WG(SharedPreferences settings)
	{
		this.settings = settings;
		phpPage = "wg.php";
		arrayName = "WG";
	}
}