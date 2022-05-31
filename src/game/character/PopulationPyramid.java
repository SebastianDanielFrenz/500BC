package game.character;

public class PopulationPyramid {
	protected long[][] data;
	public static final byte MALE = 0;
	public static final byte FEMALE = 1;

	public PopulationPyramid() {
		data = new long[2][];
		data[0] = new long[200];
		data[1] = new long[200];
	}
}
