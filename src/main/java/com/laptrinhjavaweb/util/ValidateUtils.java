package com.laptrinhjavaweb.util;

public class ValidateUtils {
	
    public static boolean isValid(Object obj) {
        if (obj == null) {
            return false;
        } else if (!obj.toString().equals("")) {
            return true;
        }
        return false;
    }
    
    public static int parseInteger(String input) {
        if (input != null) {
            return Integer.parseInt(input);
        } else {
            return Integer.valueOf(input);
        }
    }
}
