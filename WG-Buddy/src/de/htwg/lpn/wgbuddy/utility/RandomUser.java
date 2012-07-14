package de.htwg.lpn.wgbuddy.utility;

import java.util.TreeMap;

/**
 * Ermittelt zuf�llig einen Benutzer aus einer Liste.
 */
public class RandomUser
{
	/**
	 * Ermittelt zuf�llig einen Benutzer aus der Liste. Umso geringer die
	 * Punktzahl eines Benutzers ist deso unwahrscheinlicher wird dieser
	 * ausgew�hlt.
	 * 
	 * @param userlist
	 *            Liste der Benutzer
	 * @return Den Benutzernamen des ausgew�hlten Benutzers.
	 */
	public static String getRandomUser(TreeMap<String, Double> userlist)
	{
		double actualHighest = 0;
		TreeMap<Double, String> points = new TreeMap<Double, String>();

		// Alle Benutzer der Liste durchlaufen.
		for (String element : userlist.keySet())
		{
			// Punkte berechnen.
			double point = userlist.get(element);
			if (point > 0)
			{
				point = 1 / point;
			}
			else
			{
				point = 1.5;
			}

			// Summe aller Punkte hochz�hlen.
			actualHighest += point;

			// Benutzer in die Map eintragen.
			points.put(actualHighest, element);
		}

		// Eine zuf�llige Zahl aus dem Bereich erzeugen.
		double zufall = Math.random() * actualHighest;

		// Den Benutzer zur�ckgeben der getroffen wurde.
		return points.ceilingEntry(zufall).getValue();
	}
}