package game.territory.terrain;

public class TerrainFarmlands extends Terrain {

	public TerrainFarmlands() {
		super("farmlands", 0xff, 0x00, 0x00);
	}

	@Override
	public double getDevelopmentMult() {
		return 1.2;
	}

	@Override
	public double getAgricultureLimit() {
		return 45000;
	}

	@Override
	public double getDefenderAdvantage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMovementSpeed() {
		return 1;
	}

	@Override
	public double getSoilFertilaty() {
		return 1.5;
	}

}
