package game.territory.holding;

import java.util.List;

import game.character.PlayableCharacter;
import game.character.PopulationStatistics;
import game.territory.building.Building;
import game.territory.building.BuildingModifier;
import game.territory.building.BuildingModifierType;
import map.Territory;

public abstract class Holding {

	private Territory parent;
	private double cache_tax_income;
	private double cache_development_target;
	private double cache_levy_limit_base;
	private double cache_levy_limit_mult;
	private double cache_fortification;
	private double cache_education_target;
	private double cache_cost;
	private double cache_ruler_piety;
	private double cache_ruler_prestige;
	private double cache_food_production;
	private double cache_food_storage_limit;
	private double cache_development_pace;

	public abstract String getName();

	public abstract boolean canHaveFields();

	private PopulationStatistics populationStatistics;

	private List<Building> buildings;
	private double area_consumed;
	private double slum_share;

	public double getHoldingArea() {
		return area_consumed;
	}

	public double getSlumShare() {
		return slum_share;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void update() {
		calcBuildingModifiers();
	}

	public abstract double getTaxIncomePerCapita();

	public abstract double getBaseTaxIncome();

	public void calcBuildingModifiers() {
		PlayableCharacter ruler = parent.getRealm().getGovernment().getRuler();
		double base = getBaseTaxIncome();
		double mult = parent.getRealm().getGovernment().getTaxMultiplier();
		double cost = 0;
		double developmentTarget = 0;
		double educationTarget = 0;
		double levyLimitBase = 0;
		double levyLimitMult = 1;
		double fortification = 0;
		double rulerPiety = 0;
		double rulerPrestige = 0;
		double foodProduction = 0;
		double foodStorageLimit = 0;
		double developmentPace = 0;
		for (Building b : buildings) {
			for (BuildingModifier mod : b.getModifiers(this)) {
				if (mod.type == BuildingModifierType.ADD_FOOD_PRODUCTION) {
					double tmp = parent.getStatistics().farmer_tax_rate * mod.amount;
					base += tmp;
					foodProduction += tmp;
				} else if (mod.type == BuildingModifierType.ADD_FOOD_STORAGE) {
					cost += mod.amount / 100;

				} else if (mod.type == BuildingModifierType.ADD_TAX_INCOME_BASE) {
					base += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_TAX_INCOME_MULT) {
					mult += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_DEVELOPMENT) {
					developmentTarget += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_LEVY_LIMIT_BASE) {
					levyLimitBase += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_LEVY_LIMIT_MULT) {
					levyLimitMult += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_RULER_PIETY) {
					rulerPiety += mod.amount;
				} else if (mod.type == BuildingModifierType.ADD_RULER_PRESTIGE) {
					rulerPrestige += mod.amount;
				}
			}
		}
		cache_tax_income = base * mult;
		cache_development_target = developmentTarget;
		cache_levy_limit_base = levyLimitBase;
		cache_levy_limit_mult = levyLimitMult;
		cache_fortification = fortification;
		cache_education_target = educationTarget;
		cache_cost = cost;
		cache_food_storage_limit = foodStorageLimit;
		cache_development_pace = developmentPace;
	}

	public double getTaxIncome() {
		return cache_tax_income;
	}
}
