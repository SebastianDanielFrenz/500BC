package game.territory;

import game.character.PlayableCharacter;
import game.territory.holding.Holding;

public abstract class Government {
	
	public void passLaw(Law law) {
		
	}
	
	public abstract LawPassResponse canPassLaw(Law law);
	public abstract String getTypeName();
	public abstract boolean isSecular();
	
	public abstract PlayableCharacter getRuler();
	public abstract double getTaxMultiplier();
	public abstract boolean canGovern(Holding holding);
}
