package com.laptrinhjavaweb.util;

public class checkInputSearch {
	public static boolean isNullStr(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isNullInt(Integer integer) {
		if (integer == null || integer == 0) {
			return true;
		}
		return false;
	}

}
