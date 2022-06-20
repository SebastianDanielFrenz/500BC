package game.territory.terrain;

public class TerrainForest extends Terrain {

	public TerrainForest() {
		super("forest", 0x47, 0xb2, 0x2d);
	}

	@Override
	public double getDevelopmentMult() {
		return 1;
	}

	@Override
	public double getAgricultureLimit() {
		return 27000;
	}

	@Override
	public double getDefenderAdvantage() {
		return 3;
	}

	@Override
	public double getMovementSpeed() {
		return 0.8;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.9;
	}

}
