package game.territory.terrain;

public class TerrainJungle extends Terrain {

	public TerrainJungle() {
		super("jungle", 0x0a3c23);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.6;
	}

	@Override
	public double getAgricultureLimit() {
		return 22500;
	}

	@Override
	public double getDefenderAdvantage() {
		return 6;
	}

	@Override
	public double getMovementSpeed() {
		return 0.5;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.75;
	}

}
