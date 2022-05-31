package game.character;

import map.World;
import util.RandomUtil;

public class Genetics {
	
	public double intelligence;
	public double inbreeding;
	
	public double male_base_strength;
	public double female_base_strength;
	
	public double male_strength_potential;
	public double female_strength_potential;
	
	public double disease_immunity;
	public double male_life_expectancy;
	public double female_life_expectancy;
	
	public double fertility;
	public double temper;
	public double athletics;
	
	public double base_health;
	
	public static Genetics generate() {
		Genetics g = new Genetics();
		g.inbreeding = 0;
		g.intelligence = RandomUtil.random(5, 2);
		
		g.male_base_strength = RandomUtil.random(10, 4);
		g.female_base_strength = RandomUtil.random(6, 4);
		g.male_strength_potential = RandomUtil.random(15, 5);
		g.female_base_strength = RandomUtil.random(10,5);
		
		g.disease_immunity = RandomUtil.random(10, 3);
		g.male_life_expectancy = RandomUtil.random(53, 5);
		g.female_life_expectancy = RandomUtil.random(56, 5);
		
		g.fertility = RandomUtil.random(5, 2.5);
		g.temper = RandomUtil.random(10, 2);
		
		return g;
	}

}
