package game.trait;

public class OpinionModifier {
	
	public final OpinionType type;
	public final double amount;
	
	public OpinionModifier(OpinionType type, double amount) {
		this.amount = amount;
		this.type = type;
	}

}
