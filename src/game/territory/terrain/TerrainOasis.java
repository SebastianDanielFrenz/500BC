package game.territory.terrain;

public class TerrainOasis extends Terrain {

	public TerrainOasis() {
		super("oasis", 0x9b8fcc);
	}

	@Override
	public double getDevelopmentMult() {
		return 1.1;
	}

	@Override
	public double getAgricultureLimit() {
		return 33000;
	}

	@Override
	public double getDefenderAdvantage() {
		return 0;
	}

	@Override
	public double getMovementSpeed() {
		return 1;
	}

	@Override
	public double getSoilFertilaty() {
		return 1.1;
	}

}
