package game.trait;

import java.util.ArrayList;
import java.util.List;

public enum TraitGroup {

	BRAVERY("Bravery"), CALM("Calm"), LUST("Lust");

	TraitGroup(String name) {
		this.name = name;
	}

	public String name;
	private List<Trait> traits = new ArrayList<Trait>();

	public List<Trait> getTraits() {
		return traits;
	}

	public void setTraits(List<Trait> traits) {
		this.traits = traits;
	}
}
