package xyz.derkades.minigames.utils.java;

import java.security.SecureRandom;

public class EnumUtils {
	
	public static <T extends Enum<?>> T getRandomEnum(Class<T> clazz){
		final SecureRandom random = new SecureRandom();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}
