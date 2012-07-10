package de.htwg.lpn.wgbuddy.utility;

import java.util.SortedSet;
import java.util.TreeMap;

public class RandomUser
{

	private Double totalPoints;
	private TreeMap<String, Double> userlist;

	public RandomUser()
	{
	}

	/**
	 * Berechnet wieviel Prozent von der Gesamtpunktzahl ein User hat
	 * 
	 * @param points
	 * @return
	 */
	private Double getProzent(Double points)
	{
		return points / totalPoints;
	}

	public String getRandomUser2()
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

			points.put(point, element);
			actualHighest += point;
		}

		double zufall = Math.random() * actualHighest;

		return points.ceilingEntry(zufall).getValue();

	}

	/**
	 * Bekommt eine TreeMap mit Usern übergeben und gibt einen zufälligen User
	 * zurück
	 * 
	 * @param userlist
	 * @return
	 */
	public String getRandomUser()
	{
		TreeMap<Double, String> randomTreeMap = createRandomTreeMap(createPreselectionMap(userlist));
		findRandomItem(randomTreeMap);
		return randomTreeMap.firstEntry().getValue();
	}

	/**
	 * erstellt eine TreeMap mit den Points als key. Bei gleicher Punktzahl wird
	 * per Zufall entschieden welcher User in die TreeMap eingetragen wird
	 * 
	 * @param userlist
	 * @return
	 */
	private TreeMap<Double, String> createPreselectionMap(TreeMap<String, Double> userlist)
	{
		TreeMap<Double, String> userMap = new TreeMap<Double, String>();
		for (String key : userlist.keySet())
		{
			Double points = (userlist.get(key) > 0) ? userlist.get(key) : 1;
			if (!userMap.containsKey(points))
			{
				userMap.put(points, key);
			}
			else
			{
				if (Math.random() > 0.5)
				{
					userMap.remove(points);
					userMap.put(points, key);
				}
			}
		}
		return userMap;
	}

	/**
	 * erstellt eine TreeMap in welcher die User in der reihenfolge nach ihren
	 * punkten eingetragen sind
	 * 
	 * @param userlist
	 * @return
	 */
	private TreeMap<Double, String> createRandomTreeMap(TreeMap<Double, String> userlist)
	{
		totalPoints = calcTotalPoints(userlist);
		TreeMap<Double, String> ret = new TreeMap<Double, String>();
		fillMap(userlist, ret);

		return ret;
	}

	/**
	 * berechnet die gesamtanzahl der Punkte aller User
	 * 
	 * @param userlist
	 * @return
	 */
	private Double calcTotalPoints(TreeMap<Double, String> userlist)
	{
		Double ret = 0.00;
		for (Double key : userlist.keySet())
		{
			ret += key;
		}
		return ret;
	}

	/**
	 * befühlt die TreeMap mit Usern als value und den Punkten jeweils
	 * aufaddiert mit den Punkten des vorherigen Users beginnend bei dem User
	 * mit den meißten Punkten
	 * 
	 * @param userlist
	 * @param ret
	 */
	private void fillMap(TreeMap<Double, String> userlist, TreeMap<Double, String> ret)
	{
		SortedSet<Double> sortedKeys = (SortedSet<Double>) userlist.keySet();
		Double prozent = getProzent(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));
		ret.put(prozent, userlist.get(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString())));
		userlist.remove(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));

		for (int i = userlist.keySet().size() - 1; i >= 0; i--)
		{
			ret.put(getProzent(Double.valueOf(sortedKeys.toArray()[i].toString())) + prozent, userlist.get(Double.valueOf(sortedKeys.toArray()[i].toString())));
			prozent += getProzent(Double.valueOf(sortedKeys.toArray()[i].toString()));
			userlist.remove(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));
		}
	}

	/**
	 * wählt zufällig einen user aus und löscht ihn aus der TreeMap bis nur noch
	 * ein User in der Treemap vorhanden ist
	 * 
	 * @param randomTreeMap
	 */
	private void findRandomItem(TreeMap<Double, String> randomTreeMap)
	{
		if (randomTreeMap.size() > 1)
		{
			randomTreeMap.remove(randomTreeMap.ceilingEntry(Math.random()).getKey());
			findRandomItem(randomTreeMap);
		}
		else
		{
			return;
		}
	}

	/**
	 * @return the userlist
	 */
	public TreeMap<String, Double> getUserlist()
	{
		return userlist;
	}

	/**
	 * @param userlist
	 *            the userlist to set
	 */
	public void setUserlist(TreeMap<String, Double> userlist)
	{
		this.userlist = userlist;
	}
}
