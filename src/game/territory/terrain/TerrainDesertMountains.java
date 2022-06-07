package game.territory.terrain;

public class TerrainDesertMountains extends Terrain {

	public TerrainDesertMountains() {
		super("Desert Mountains", 0x17, 0x13, 0x26);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.4;
	}

	@Override
	public double getAgricultureLimit() {
		return 750;
	}

	@Override
	public double getDefenderAdvantage() {
		return 12;
	}

	@Override
	public double getMovementSpeed() {
		return 0.2;
	}

}
