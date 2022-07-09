package game.law;

import game.territory.holding.Holding;

public abstract class TaxLaw extends Law {
	public abstract double getHoldingIncome(Holding holding);
}
