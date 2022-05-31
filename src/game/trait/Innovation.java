package game.trait;

public enum Innovation {

	FIRE("Fire", new Innovation[] {}),;

	public final String name;
	public final Innovation[] first;

	Innovation(String name, Innovation[] first) {
		this.name = name;
		this.first = first;
	}

}
