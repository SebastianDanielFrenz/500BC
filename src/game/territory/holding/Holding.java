package game.territory.holding;

import java.util.List;

import game.character.PopulationStatistics;
import game.territory.building.Building;
import game.territory.building.BuildingModifier;
import game.trait.CharacterModifier;
import game.trait.Trait;
import map.Territory;

public abstract class Holding {

    private Territory parent;
    private double cache_tax_income_base;
    private double cache_tax_income_mult;
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
    private double cache_popular_opinion;
    private double cache_transport_capacity;
    private double cache_economic_groth_target;
    private double cache_economic_growth_pace;
    private double cache_building_construction_speed_mult;
    private double cache_building_construction_cost_mult;
    private double cache_building_maintenance_speed_mult;
    private double cache_building_maintenance_cost_mult;

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

    public void updateMonth() {
        resetCache();
        calcBuildingModifiers();
        calcGovernorModifiers();
    }

    public void updateYear() {
        populationStatistics.age();
    }

    public abstract double getTaxIncomePerCapita();

    public abstract double getBaseTaxIncome();

    public void calcBuildingModifiers() {
        for (Building b : buildings) {
            for (BuildingModifier mod : b.getModifiers(this)) {
                switch (mod.type) {
                    case ADD_FOOD_PRODUCTION -> {
                        double tmp = parent.getStatistics().farmer_tax_rate * mod.amount;
                        cache_tax_income_mult += tmp;
                        cache_food_production += tmp;
                    }
                    case ADD_FOOD_STORAGE -> cache_cost += mod.amount / 100;
                    case ADD_TAX_INCOME_BASE -> cache_tax_income_base += mod.amount;
                    case ADD_TAX_INCOME_MULT -> cache_tax_income_mult += mod.amount;
                    case ADD_POPULAR_OPINION -> cache_popular_opinion += mod.amount;
                    case ADD_DEVELOPMENT -> cache_development_target += mod.amount;
                    case ADD_LEVY_LIMIT_BASE -> cache_levy_limit_base += mod.amount;
                    case ADD_LEVY_LIMIT_MULT -> cache_levy_limit_mult += mod.amount;
                    case ADD_RULER_PIETY -> cache_ruler_piety += mod.amount;
                    case ADD_RULER_PRESTIGE -> cache_ruler_prestige += mod.amount;
                    case ADD_TRANSPORT_CAPACTIY -> cache_transport_capacity += mod.amount;
                    case ADD_ECONOMY_GROTH_TARGET -> cache_economic_groth_target += mod.amount;
                    case ADD_FORTIFICATION -> cache_fortification += mod.amount;

                }
            }
        }
    }

    public void calcGovernorModifiers() {
        for (Trait trait : parent.getRealm().getRuler().getTraits()) {
            for (CharacterModifier mod : trait.mods) {
                switch (mod.type) {
                    case MULT_TROOP_SIZE -> {
                        cache_levy_limit_mult *= mod.amount;
                    }
                    case MULT_BUILDING_CONSTRUCTION_SPEED -> {
                        cache_building_construction_speed_mult += mod.amount;
                    }
                    case MULT_BUILDING_CONSTRUCTION_COST -> {
                        cache_building_construction_cost_mult += mod.amount;
                    }
                    case MULT_BUILDING_MAINTANANCE_SPEED -> {
                        cache_building_maintenance_speed_mult += mod.amount;
                    }
                    case MULT_BUILDING_MAINTANANCE_COST -> {
                        cache_building_maintenance_cost_mult += mod.amount;
                    }
                }
            }
        }
    }

    public void resetCache() {
        cache_tax_income_base = 0;
        cache_tax_income_mult = 0;
        cache_development_target = 0;
        cache_levy_limit_base = 0;
        cache_levy_limit_mult = 0;
        cache_fortification = 0;
        cache_education_target = 0;
        cache_cost = 0;
        cache_ruler_piety = 0;
        cache_ruler_prestige = 0;
        cache_food_production = 0;
        cache_food_storage_limit = 0;
        cache_development_pace = 0;
        cache_popular_opinion = 0;
        cache_transport_capacity = 0;
        cache_economic_groth_target = 0;
        cache_economic_growth_pace = 0;
        cache_building_construction_speed_mult = 0;
        cache_building_construction_cost_mult = 0;
        cache_building_maintenance_speed_mult = 0;
        cache_building_maintenance_cost_mult = 0;
    }

    public double getTaxIncome() {
        return cache_tax_income_base * cache_tax_income_mult;
    }
}
