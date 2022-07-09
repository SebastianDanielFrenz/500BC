package game.territory;

import java.util.ArrayList;
import java.util.List;

import game.character.PlayableCharacter;
import game.law.Law;
import game.law.LawPassResponse;
import game.law.TaxLaw;
import game.territory.holding.Holding;

public abstract class Government {

	public void passLaw(Law law) {

	}

	private List<Law> laws = new ArrayList<Law>();

	public abstract LawPassResponse canPassLaw(Law law);

	public abstract String getTypeName();

	public abstract boolean isSecular();

	public abstract PlayableCharacter getRuler();

	public abstract double getTaxMultiplier();

	public abstract boolean canGovern(Holding holding);

	public abstract boolean abdecate();

	public abstract boolean hasGovernmentalCycles();

	public abstract int getGovernmentalCycleLength();

	public List<Law> getLaws() {
		return laws;
	}

	public List<TaxLaw> getTaxLaws() {
		List<TaxLaw> list = new ArrayList<TaxLaw>();
		for (Law law : laws) {
			if (law instanceof TaxLaw) {
				list.add((TaxLaw) law);
			}
		}
		return list;
	}
}
