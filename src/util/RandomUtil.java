package util;

import map.World;

public class RandomUtil {

	public static double random(double mean, double sigma) {
		return mean + sigma * World.random.nextGaussian();
	}

}
