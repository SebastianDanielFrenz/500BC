package game.territory.holding;

public class HoldingFeudal extends Holding {
	
	private String name;
	
	public HoldingFeudal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canHaveFields() {
		return true;
	}

	@Override
	public double getTaxIncomePerCapita() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBaseTaxIncome() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
