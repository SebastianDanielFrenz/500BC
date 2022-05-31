package game.territory;

import java.util.List;
import java.util.function.Consumer;

import map.Territory;
import map.World;

public class Realm {
	private Realm parent;
	private List<Realm> children;
	private List<Territory> territories;

	private SubGovernmentContract contract;
	private Government government;
	private int color;

	public Realm(List<Realm> children, List<Territory> territories, Government government) {
		this.children = children;
		this.territories = territories;
		this.government = government;
		if (territories.size() > 0) {
			color = territories.get(0).fileColor;
		}
		else {
			World.random.nextInt();
		}
	}
	
	private double tax_income;
	private double treasury;

	public Realm getParent() {
		return parent;
	}

	public void setParent(Realm parent) {
		this.parent = parent;
	}

	public SubGovernmentContract getContract() {
		return contract;
	}

	public void setContract(SubGovernmentContract contract) {
		this.contract = contract;
	}

	public Government getGovernment() {
		return government;
	}

	public void setGovernment(Government government) {
		this.government = government;
	}

	public List<Realm> getChildren() {
		return children;
	}

	public void setChildren(List<Realm> children) {
		this.children = children;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}

	public String getName() {
		return String.valueOf(hashCode() % 10000);
	}

	public void everyDirectTerritory(Consumer<Territory> con) {
		for (Territory t : territories) {
			con.accept(t);
		}
	}

	public void everyIndirectTerritory(Consumer<Territory> con) {
		everyDirectTerritory(con);
		for (Realm r : children) {
			r.everyIndirectTerritory(con);
		}
	}

	public int getTotalTerritoryCount() {
		int total = territories.size();
		for (Realm r : children) {
			total += r.getTotalTerritoryCount();
		}
		return total;
	}

	public void removeTerritory(Territory t) {
		territories.remove(t);
	}
	
	public double getTaxIncome() {
		return tax_income;
	}
	
	public double getTreasury() {
		return treasury;
	}
	
	public void update() {
		tax_income = 0;
		for (Territory t:territories) {
			tax_income += t.getTaxIncome();
		}
		
	}

	@Override
	public String toString() {
		String out = "Realm(name=" + getName() + ", size=" + getTotalTerritoryCount() + ", parent="
				+ (parent == null ? "null" : parent.getName()) + ", territories=[";
		for (int i = 0; i < territories.size(); i++) {
			out += territories.get(i).toString() + ", ";
		}
		out += "], children=[";
		for (Realm r : children) {
			out += r.getName() + ", ";
		}
		out += "]";
		return out + ")";

	}

	public void addTerritory(Territory t) {
		territories.add(t);
	}
}
