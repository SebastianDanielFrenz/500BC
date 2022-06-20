package game.territory.terrain;

public class TerrainHills extends Terrain {

	public TerrainHills() {
		super("hills", 0x5a320c);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.9;
	}

	@Override
	public double getAgricultureLimit() {
		return 27000;
	}

	@Override
	public double getDefenderAdvantage() {
		return 5;
	}

	@Override
	public double getMovementSpeed() {
		return 0.8;
	}

	@Override
	public double getSoilFertilaty() {
		return 1;
	}

}
