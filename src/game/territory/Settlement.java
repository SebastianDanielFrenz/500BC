package game.territory;

import java.util.List;

import game.character.PopulationStatistics;
import game.territory.building.Building;

public abstract class Settlement {
	private boolean needs_recalc = false;
	public final LandedTitle parent;
	private PopulationStatistics population_statistics;
	public List<Building> buildings;

	public Settlement(LandedTitle parent) {
		this.parent = parent;
	}

	public void markRecalc() {
		needs_recalc = true;
	}

	public void unmarkRecalc() {
		needs_recalc = false;
	}

	public boolean isMarked() {
		return needs_recalc;
	}
}
