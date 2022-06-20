package game.territory.terrain;

public class TerrainOcean extends Terrain {

	public TerrainOcean() {
		super("ocean", 0x004a7f);
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
		return 0;
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

}
