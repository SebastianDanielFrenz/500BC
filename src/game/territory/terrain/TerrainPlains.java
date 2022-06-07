package game.territory.terrain;

public class TerrainPlains extends Terrain {

	public TerrainPlains() {
		super("Plains", 0xcc, 0xa3, 0x66);
	}

	@Override
	public double getDevelopmentMult() {
		return 1;
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

}
