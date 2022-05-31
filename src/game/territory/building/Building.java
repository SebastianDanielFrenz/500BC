package game.territory.building;

import java.util.List;

import game.territory.holding.Holding;

public abstract class Building {

	public abstract List<BuildingModifier> getModifiers(Holding h);
}
