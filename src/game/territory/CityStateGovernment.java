package game.territory;

import game.character.PlayableCharacter;
import game.territory.holding.Holding;

public class CityStateGovernment extends Government {
	
	public CityStateGovernment() {
		mayor = PlayableCharacter.generate();
	}
	
	private PlayableCharacter mayor;

	@Override
	public LawPassResponse canPassLaw(Law law) {
		return new LawPassResponse();
	}

	@Override
	public String getTypeName() {
		return "City State";
	}

	@Override
	public boolean isSecular() {
		return true;
	}

	@Override
	public PlayableCharacter getRuler() {
		return mayor;
	}

	@Override
	public double getTaxMultiplier() {
		return skillValueToEffectiveness(mayor.getStewardship());
	}

	public static double skillValueToEffectiveness(double value) {
		return (value - 10) / 10;
	}

	@Override
	public boolean canGovern(Holding holding) {
		return true;
	}

}
