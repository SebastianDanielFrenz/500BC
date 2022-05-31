package game.territory;

public abstract class SubGovernmentContract {
	
	public abstract boolean allowArmy();
	public abstract double modifyArmyCost(double original);
	public abstract double modifyArmySize(double original);
	public abstract double calcTax(double state_income);

}
