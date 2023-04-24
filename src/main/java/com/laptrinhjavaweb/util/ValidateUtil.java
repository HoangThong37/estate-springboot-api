package com.laptrinhjavaweb.util;

import com.laptrinhjavaweb.constant.SystemConstant;

public class ValidateUtil { 
	// String and Interger
	public static Object validateSearch(Object object) {
		String regex = "^[0-9]+"; // start int (0-9)
		if (object == null || object.toString().equals(SystemConstant.STRING_EMPTY) ) {
			return null;
		} else if (object.toString().matches(regex)) {
			return Integer.parseInt(object.toString());
		} 
		return object.toString();
	}
	
    public static boolean isValid(Object obj) {
        if (obj == null) {
            return false;
        } else if (!obj.toString().equals(SystemConstant.STRING_EMPTY)) {
            return true;
        }
         return false;
     }
 

}
