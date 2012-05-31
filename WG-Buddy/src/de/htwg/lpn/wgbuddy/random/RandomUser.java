package de.htwg.lpn.wgbuddy.random;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import de.htwg.lpn.model.User;



public class RandomUser {
	
	private Double totalPoints;
	
	private Double getProzent(Double points)
	{
		return points / totalPoints;
	}
			
	public String getRandomUser(ArrayList<HashMap<String, String>> userlist)
	{
		TreeMap<Double, String> oMap = createTreeMap(userlist);
		findRandomItem(oMap);
		return oMap.firstEntry().getValue();
	}
	
	private TreeMap<Double, String> createTreeMap(ArrayList<HashMap<String, String>> userlist) 
	{
		totalPoints = calcTotalPoints(userlist);
		TreeMap<Double, String> ret = new TreeMap<Double, String>();
		fillMap(userlist, ret);

		return ret;
	}

	private Double calcTotalPoints(ArrayList<HashMap<String, String>> userlist) 
	{
		Double ret = 0.00;
		for(int i = 0; i < userlist.size(); i++)
		{
			ret += Double.valueOf(userlist.get(i).get("points")); 
		}
		return ret;
	}

	private void fillMap(ArrayList<HashMap<String, String>> userlist, TreeMap<Double, String> ret) 
	{	
		TreeMap<Double, String> cache = new TreeMap<Double, String>();
		for(int i = 0; i < userlist.size(); i++)
		{
			cache.put(Double.valueOf(userlist.get(i).get("points")), userlist.get(i).get("id"));
		}
		
		
		SortedSet<Double> sortedKeys = (SortedSet<Double>) cache.keySet();
		Double prozent = getProzent(Double.valueOf(sortedKeys.toArray()[0].toString()));
		ret.put(prozent, cache.get(Double.valueOf(sortedKeys.toArray()[0].toString())));
		for(int i = 1; i < cache.keySet().size(); i++)
		{	
			ret.put(getProzent(Double.valueOf(sortedKeys.toArray()[i].toString())) + prozent, cache.get(Double.valueOf(sortedKeys.toArray()[i].toString())));
			prozent = getProzent(getProzent(Double.valueOf(sortedKeys.toArray()[i].toString())));
			cache.remove(prozent);
		}
	}


	private void findRandomItem(TreeMap<Double, String> oMap)
	{
		oMap.remove(oMap.ceilingEntry(Math.random()).getKey());
		if(oMap.size() > 1)
		{
			findRandomItem(oMap);
		}
		else
		{
			return;
		}
	}
}
