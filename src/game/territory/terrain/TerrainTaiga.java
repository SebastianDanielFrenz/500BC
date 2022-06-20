package game.territory.terrain;

public class TerrainTaiga extends Terrain {

	public TerrainTaiga() {
		super("taiga", 0x2e9959);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.95;
	}

	@Override
	public double getAgricultureLimit() {
		return 24000;
	}

	@Override
	public double getDefenderAdvantage() {
		return 4;
	}

	@Override
	public double getMovementSpeed() {
		return 0.8;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.8;
	}

}
