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
	public static final Terrain WETLANDS = new TerrainWetlands();
	public static final Terrain DRYLANDS = new TerrainDrylands();
	public static final Terrain FARMLANDS = new TerrainFarmlands();
	public static final Terrain FLOODPLAINS = new TerrainFloodplains();
	public static final Terrain FOREST = new TerrainForest();
	public static final Terrain HILLS = new TerrainHills();
	public static final Terrain JUNGLE = new TerrainJungle();
	public static final Terrain MOUNTAINS = new TerrainMountains();
	public static final Terrain OASIS = new TerrainOasis();
	public static final Terrain STEPPE = new TerrainSteppe();
	public static final Terrain TAIGA = new TerrainTaiga();

	public static final Terrain OCEAN = new TerrainOcean();
	public static final Terrain RIVER = new TerrainRiver();

	public static Terrain getTerrain(String name) {
		for (int i = 0; i < registry.size(); i++) {
			if (registry_names.get(i).equalsIgnoreCase(name)) {
				return registry.get(i);
			}
		}
		return null;
	}

	public static int getIndex(String name) {
		for (int i = 0; i < registry.size(); i++) {
			if (registry_names.get(i).equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	public static int getIndex(Terrain terrain) {
		for (int i = 0; i < registry.size(); i++) {
			if (registry.get(i) == terrain) {
				return i;
			}
		}
		return -1;
	}

	public final int r;
	public final int g;
	public final int b;
	public final int color;
	public final String name;

	public Terrain(String name, int r, int g, int b) {
		registry_names.add(name);
		registry.add(this);
		this.r = r;
		this.g = g;
		this.b = b;
		color = World.generateColorCode(0xff, r, g, b);
		this.name = name;
	}

	public Terrain(String name, int colorcode) {
		this(name, colorcode / 0x10000, (colorcode % 0x10000 / 0x100), (colorcode % 0x100));
	}

	public abstract double getDevelopmentMult();

	public abstract double getAgricultureLimit();

	public abstract double getDefenderAdvantage();

	public abstract double getMovementSpeed();

	public abstract double getSoilFertilaty();

	public boolean isHabitable() {
		return true;
	}

	public String getName() {
		return name;
	}

}
