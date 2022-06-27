package game.territory.terrain;

public class TerrainRiver extends Terrain {

	public TerrainRiver() {
		super("river", 0x0000dd);
	}

	@Override
	public double getDevelopmentMult() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAgricultureLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDefenderAdvantage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMovementSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSoilFertilaty() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

}
