package com.laptrinhjavaweb.util;

public class NumberUtils {
	public static boolean isInteger(String value) {
	    if (value == null) {
	        return false;
	    }
	    try {
	        Integer integer = Integer.parseInt(value);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	} 
}
