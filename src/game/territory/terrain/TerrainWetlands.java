package game.territory.terrain;

public class TerrainWetlands extends Terrain {

	public TerrainWetlands() {
		super("wetlands", 0x4C, 0x99, 0x99);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.75;
	}

	@Override
	public double getAgricultureLimit() {
		return 22500;
	}

	@Override
	public double getDefenderAdvantage() {
		return 5;
	}

	@Override
	public double getMovementSpeed() {
		return 0.7;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.75;
	}

}
