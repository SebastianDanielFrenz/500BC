package game.territory.terrain;

public class TerrainMountains extends Terrain {

	public TerrainMountains() {
		super("mountains", 0x646464);
	}

	@Override
	public double getDevelopmentMult() {
		return 0.75;
	}

	@Override
	public double getAgricultureLimit() {
		return 15000;
	}

	@Override
	public double getDefenderAdvantage() {
		return 12;
	}

	@Override
	public double getMovementSpeed() {
		return 0.5;
	}

	@Override
	public double getSoilFertilaty() {
		return 0.5;
	}

}
