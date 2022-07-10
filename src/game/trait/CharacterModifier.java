package game.trait;

public class CharacterModifier {

	public final CharacterModifierType type;
	public final double amount;

	public CharacterModifier(CharacterModifierType type, double amount) {
		this.type = type;
		this.amount = amount;
	}

}
