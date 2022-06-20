package game.territory.terrain;

public class TerrainSteppe extends Terrain {

	public TerrainSteppe() {
		super("steppe", 0xc8641a);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.5;
	}

	@Override
	public double getAgricultureLimit() {
		return 30000;
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
		return 0.8;
	}

}
