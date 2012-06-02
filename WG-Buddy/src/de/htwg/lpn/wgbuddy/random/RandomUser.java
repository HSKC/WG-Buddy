package de.htwg.lpn.wgbuddy.random;



import java.util.SortedSet;
import java.util.TreeMap;



public class RandomUser {
	
	private Double totalPoints;
	
	private Double getProzent(Double points)
	{
		return points / totalPoints;
	}
			
	public String getRandomUser(TreeMap<String, Double> userlist)
	{
		//TODO key und value tauschen und bei gleicher punktzahl vorauswahltreffen
		TreeMap<Double, String> randomTreeMap = createRandomTreeMap(createPreselectionMap(userlist));
		findRandomItem(randomTreeMap);
		return randomTreeMap.firstEntry().getValue();
	}
	
	private TreeMap<Double, String> createPreselectionMap(TreeMap<String, Double> userlist) 
	{
		TreeMap<Double, String> ret = new TreeMap<Double, String>();
		for(String key : userlist.keySet())
		{
			if(!ret.containsKey(userlist.get(key)))
			{
				ret.put(userlist.get(key), key);
			}
			else
			{
				if(Math.random() > 0.5)
				{
					ret.remove(userlist.get(key));
					ret.put(userlist.get(key), key);
				}
			}
		}
		return ret;
	}

	private TreeMap<Double, String> createRandomTreeMap(TreeMap<Double, String> userlist) 
	{
		totalPoints = calcTotalPoints(userlist);
		TreeMap<Double, String> ret = new TreeMap<Double, String>();
		fillMap(userlist, ret);

		return ret;
	}

	private Double calcTotalPoints(TreeMap<Double, String> userlist) 
	{
		Double ret = 0.00;
		for(Double key : userlist.keySet())
		{
			ret += key; 
		}
		return ret;
	}

	private void fillMap(TreeMap<Double, String> userlist, TreeMap<Double, String> ret) 
	{			
		SortedSet<Double> sortedKeys = (SortedSet<Double>) userlist.keySet();
		Double prozent = getProzent(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));
		ret.put(prozent, userlist.get(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString())));
		userlist.remove(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));
		
		for(int i = userlist.keySet().size() - 1; i >= 0; i--)
		{	
			ret.put(getProzent(Double.valueOf(sortedKeys.toArray()[i].toString())) + prozent, userlist.get(Double.valueOf(sortedKeys.toArray()[i].toString())));
			prozent += getProzent(Double.valueOf(sortedKeys.toArray()[i].toString()));
			userlist.remove(Double.valueOf(sortedKeys.toArray()[sortedKeys.size() - 1].toString()));
		}
	}

	private void findRandomItem(TreeMap<Double, String> randomTreeMap)
	{
		randomTreeMap.remove(randomTreeMap.ceilingEntry(Math.random()).getKey());
		if(randomTreeMap.size() > 1)
		{
			findRandomItem(randomTreeMap);
		}
		else
		{
			return;
		}
	}
}
