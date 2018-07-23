package com.wujiuye.qqgroupjoin.utils;

public class StringUtils {
	
	public static boolean isEmpty(String str){
		if(str==null)
			return true;
		str.trim();
		if(str.equals(""))
			return true;
		return false;
	}

}
