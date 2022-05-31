package game.trait;

public class CharacterModifier {

	public final CharacterModifierType type;
	public final double value;

	public CharacterModifier(CharacterModifierType type, double value) {
		this.type = type;
		this.value = value;
	}

}
