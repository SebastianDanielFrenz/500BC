package game.territory.building;

public class BuildingModifier {
	public BuildingModifier(BuildingModifierType type, double amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public BuildingModifierType type;
	public double amount;
}
