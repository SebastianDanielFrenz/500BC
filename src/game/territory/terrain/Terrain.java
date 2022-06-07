package game.territory.terrain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import map.World;

public abstract class Terrain {

	public static List<String> registry_names = new LinkedList<String>();
	public static List<Terrain> registry = new ArrayList<Terrain>();
	
	public static final Terrain DESERT = new TerrainDesert();
	public static final Terrain DESERT_MOUNTAINS = new TerrainDesertMountains();
	public static final Terrain PLAINS = new TerrainPlains();
	
	public static Terrain getTerrain(String name) {
		for (int i = 0; i < registry.size(); i++) {
			if (registry_names.get(i).equalsIgnoreCase(name)) {
				return registry.get(i);
			}
		}
		return null;
	}

	public final int r;
	public final int g;
	public final int b;
	public final int color;

	public Terrain(String name, int r, int g, int b) {
		registry_names.add(name);
		registry.add(this);
		this.r = r;
		this.g = g;
		this.b = b;
		color = World.generateColorCode(0xff, r, g, b);
	}

	public abstract double getDevelopmentMult();

	public abstract double getAgricultureLimit();

	public abstract double getDefenderAdvantage();

	public abstract double getMovementSpeed();

}
