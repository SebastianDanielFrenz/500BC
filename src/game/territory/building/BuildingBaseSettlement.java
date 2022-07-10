package game.territory.building;

import game.territory.holding.Holding;

import java.util.List;

public class BuildingBaseSettlement extends Building {

    List<BuildingModifier> modifiers = List.of(new BuildingModifier(BuildingModifierType.ADD_FOOD_STORAGE, 25 * 4),
            new BuildingModifier(BuildingModifierType.ADD_FOOD_PRODUCTION, 25 * 4),
            new BuildingModifier(BuildingModifierType.ADD_DEVELOPMENT_TARGET, 10),
            new BuildingModifier(BuildingModifierType.ADD_DEVELOPMENT_PACE, devProg(0, 10, 5000)));

    @Override
    public final List<BuildingModifier> getModifiers(Holding h) {
        return modifiers;
    }
}

