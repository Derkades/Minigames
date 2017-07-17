package xyz.derkades.minigames.utils.java;

import java.util.concurrent.ThreadLocalRandom;

public class NumberUtils {
	
	public static int randomInteger(int min, int max){
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}
