package game.character;

public class PopulationStatistics {

	protected long[][] pyramid_data;
	public static final byte MALE = 0;
	public static final byte FEMALE = 1;
	public static final int SIZE = 200;
	public long total_population = 0;

	protected long death_count;
	protected double birth_count;
	protected double hunger;

	public PopulationStatistics() {
		pyramid_data = new long[2][];
		pyramid_data[0] = new long[SIZE];
		pyramid_data[1] = new long[SIZE];
	}

	protected HealthStatistics health;
	protected IncomeStatistics income;

	public double getSlaveShare() {
		return income.slave_count / total_population;
	}

	public void age() {
		for (int i = SIZE - 1; i > 0; i--) {
			pyramid_data[0][i] = pyramid_data[0][i - 1];
			pyramid_data[1][i] = pyramid_data[1][i - 1];
		}
	}
}
