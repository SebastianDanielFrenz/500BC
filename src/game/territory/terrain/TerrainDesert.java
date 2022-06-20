package game.territory.terrain;

public class TerrainDesert extends Terrain {

	public TerrainDesert() {
		super("Desert", 0xff, 0xe6, 0x00);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.5;
	}

	@Override
	public double getAgricultureLimit() {
		return 1500;
	}

	@Override
	public double getDefenderAdvantage() {
		return 0;
	}

	@Override
	public double getMovementSpeed() {
		return 0.7;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.7;
	}

}
