package game.territory.terrain;

public class TerrainFloodplains extends Terrain {

	public TerrainFloodplains() {
		super("floodplains", 0x37, 0x1f, 0x99);
	}

	@Override
	public double getDevelopmentMult() {
		return 1.2;
	}

	@Override
	public double getAgricultureLimit() {
		return 40500;
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
		return 1.35;
	}

}
