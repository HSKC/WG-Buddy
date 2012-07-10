package de.htwg.lpn.wgbuddy.utility;

import java.util.TreeMap;

public class RandomUser
{
	public RandomUser()
	{
	}

	public String getRandomUser(TreeMap<String, Double> userlist)
	{
		double actualHighest = 0;
		TreeMap<Double, String> points = new TreeMap<Double, String>();

		for (String element : userlist.keySet())
		{
			double point = userlist.get(element);
			if (point > 0)
			{
				point = 1 / point;
			}
			else
			{
				point = 1.5;
			}

			actualHighest += point;

			points.put(actualHighest, element);
		}

		double zufall = Math.random() * actualHighest;

		return points.ceilingEntry(zufall).getValue();
	}
}