package map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import game.territory.Realm;
import game.territory.holding.Holding;
import game.territory.terrain.Terrain;

public class Territory {
	public Territory(int ID, int fileColor) {
		this.ID = ID;
		this.fileColor = 0xff000000+fileColor;
	}

	public final int ID;
	private Realm realm;
	private List<Territory> neighbours = new ArrayList<Territory>();
	public final int fileColor;
	private List<Holding> holdings = new ArrayList<Holding>();
	private Terrain terrain;

	public List<int[]> pixels;

	private TerritoryStatistics statistics;

	private double tax_income;

	public Realm getTopLevelRealm() {
		Realm realm;
		for (realm = this.realm; realm.getParent() != null; realm = realm.getParent()) {
		}
		return realm;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
		World.setTerritoryColor(this, realm.getColor());
	}

	@Override
	public String toString() {
		return String.valueOf(ID);
	}

	public void addNeighbour(Territory t) {
		for (Territory t2 : neighbours) {
			if (t2 == t) {
				return;
			}
		}
		neighbours.add(t);
	}

	public List<Territory> getNeighbours() {
		return neighbours;
	}

	public void update() {
		tax_income = 0;
		for (Holding holding : holdings) {
			holding.update();
			tax_income += holding.getTaxIncome();
		}
	}

	private void calcDevelopmentTarget() {
		for (Holding holding : holdings) {
		}
	}

	public double getTaxIncome() {
		return tax_income;
	}

	public TerritoryStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(TerritoryStatistics statistics) {
		this.statistics = statistics;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Terrain getTerrain() {
		return terrain;
	}
}
