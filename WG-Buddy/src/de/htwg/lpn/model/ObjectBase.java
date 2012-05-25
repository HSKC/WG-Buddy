package de.htwg.lpn.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ObjectBase
{
	public ObjectBase(HashMap<String, String> map)
	{
		Class<? extends ObjectBase> c = this.getClass();
				
		for(Map.Entry entry : map.entrySet())
		{
			Field field = null;;
			try 
			{
				field = c.getDeclaredField((String) entry.getKey());
			} 
			catch (SecurityException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(field != null)
			{
				field.setAccessible(true);
			}
			
			try
			{
				field.set(this, entry.getValue());
			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<String, String> getMap()
	{
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		Class<? extends ObjectBase> c = this.getClass();
		Field[] fields = c.getDeclaredFields();
		
		for(Field field : fields)
		{
			field.setAccessible(true);
			try 
			{
				returnMap.put(field.getName(), String.valueOf(field.get(this)));
			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return returnMap;
	}
	
	public List<NameValuePair> getNameValuePairs()
	{
		List<NameValuePair> returnNameValuePairs = new ArrayList<NameValuePair>();
       
		Class<? extends ObjectBase> c = this.getClass();
		Field[] fields = c.getDeclaredFields();
		
		for(Field field : fields)
		{
			field.setAccessible(true);
			try 
			{
				returnNameValuePairs.add(new BasicNameValuePair(field.getName(), String.valueOf(field.get(this))));
			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}        
        
		return returnNameValuePairs;
	}
}
