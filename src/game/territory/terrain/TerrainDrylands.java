package game.territory.terrain;

public class TerrainDrylands extends Terrain {

	public TerrainDrylands() {
		super("drylands", 0xdc, 0x2d, 0x78);
	}

	@Override
	public double getDevelopmentMult() {
		return 0;
	}

	@Override
	public double getAgricultureLimit() {
		return 0;
	}

	@Override
	public double getDefenderAdvantage() {
		return 0;
	}

	@Override
	public double getMovementSpeed() {
		return 0;
	}

	@Override
	public double getSoilFertilaty() {
		return 1;
	}

}
