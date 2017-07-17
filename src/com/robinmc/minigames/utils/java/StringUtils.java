package com.robinmc.minigames.utils.java;

public class StringUtils {
	
	public static String getStringFromWords(String[] words){
		String msg = "";
		
		for (String word : words){
			msg = String.join(msg, word + " ");
		}
		
		return msg;
	}

}
