package utils;

import java.util.Random;

public class KeyGenerator {
	
	public static synchronized String genUniqueKey() {
		Random random = new Random();
		Integer number = random.nextInt(90000) + 10000;
		return String.valueOf(number);
	}
}
