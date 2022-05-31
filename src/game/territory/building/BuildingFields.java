package game.territory.building;

import java.util.ArrayList;
import java.util.List;

import game.territory.holding.Holding;

public class BuildingFields extends Building {

	public BuildingFields(int lvl) {
		this.lvl = lvl;
	}

	private int lvl;

	@Override
	public List<BuildingModifier> getModifiers(Holding h) {
		List<BuildingModifier> out = new ArrayList<BuildingModifier>();
		out.add(new BuildingModifier(BuildingModifierType.ADD_FOOD_PRODUCTION, 12 * 300 * Math.pow(1.5, lvl - 1)));
		return out;
	}

}
