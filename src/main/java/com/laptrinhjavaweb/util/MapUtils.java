package com.laptrinhjavaweb.util;

import java.util.Map;

public class MapUtils {
	
   public static <T> T getObject(Map<String, Object> maps, String key, Class<T> tClass) { // T : 
	  try {
		  Object object = maps.getOrDefault(key, null);
		  if (object != null) {
			if (tClass.getTypeName().equals("java.lang.Long")) {
				object = Long.valueOf(object.toString());
			} else if (tClass.getTypeName().equals("java.lang.Integer")) {
				object = Integer.valueOf(object.toString());
			} 
			return object != null ? tClass.cast(maps.get(key)) : null; 
		}
		  return null;
	  } catch (Exception e) {
		e.printStackTrace();
		return null;
	}
   }
}


//return maps.containsKey(key) ? (object != null ? tClass.cast(object) : null) : null;