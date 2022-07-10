package game.territory.building;

import java.util.List;

import game.territory.holding.Holding;

public abstract class Building {

    public abstract List<BuildingModifier> getModifiers(Holding h);

    protected double devProg(double src, double dst, int years) {
        return (dst - src) / (years * 12);
    }
}
