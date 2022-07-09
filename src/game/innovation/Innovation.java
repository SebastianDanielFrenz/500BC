package game.innovation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import game.territory.terrain.Terrain;
import map.Territory;
import util.CDate;

public enum Innovation {

	FIRE(none(), t -> new CDate(-250000, 1, 1), getProgressMult(200000)),
	TRIBES(FIRE, t -> new CDate(-50000, 1, 1), getProgressMult(200000)),
	SETTLEMENTS(FIRE, t -> t.getTerrain() == Terrain.FLOODPLAINS ? new CDate(-4000, 1, 1) : null,
			getProgressMult(46000)),
	TAMING(none(), t -> new CDate(-20000, 1, 1), getProgressMult(100000)),
	ANIMAL_FARMING(TAMING, t -> new CDate(-7000, 1, 1), getProgressMult(13000)),
	CIVILIZATION(SETTLEMENTS, t -> new CDate(-3700, 1, 1), getProgressMult(500)),
	HORSE(new Innovation[] { SETTLEMENTS, ANIMAL_FARMING }, null, getProgressMult(500)),
	WRITING(CIVILIZATION, null, getProgressMult(200)), CURRENCY(CIVILIZATION, null, getProgressMult(100)),
	ORGANIZED_FORCES(TRIBES, t -> new CDate(-20000, 1, 1), getProgressMult(0)),
	STANDING_ARMY(ORGANIZED_FORCES, null, getProgressMult(1000)),
	SUPPLY_MANAGEMENT(ORGANIZED_FORCES, null, getProgressMult(200), true);

	public static final Innovation[] NONE = new Innovation[] {};

	private static Innovation[][] levels = null;

	public static Innovation[][] getLevels() {
		if (levels == null) {
			List<List<Innovation>> list2D = new ArrayList<List<Innovation>>();
			Map<Innovation, Integer> map = new TreeMap<Innovation, Integer>();

			int done = 0;
			for (Innovation inno : values()) {
				if (inno.getPrevious().length == 0) {
					map.put(inno, 0);
					done++;
				}
			}

			int level = 1;
			while (done < values().length) {
				for (Innovation inno : values()) {
					Integer value = map.get(inno);
					if (value == null) {
						boolean legal = true;
						for (Innovation prev : inno.getPrevious()) {
							if (map.get(prev) == null || map.get(prev) == level) {
								legal = false;
								break;
							}
						}
						if (legal) {
							map.put(inno, level);
							done++;
						}
					}
				}
				level++;
			}

			// level is one too high
			List<Innovation> tmp;
			for (int i = 0; i < level; i++) {
				tmp = new ArrayList<Innovation>();
				for (Innovation inno : map.keySet()) {
					if (map.get(inno) == i) {
						tmp.add(inno);
					}
				}
				list2D.add(tmp);
			}

			levels = new Innovation[list2D.size()][];
			for (int i = 0; i < list2D.size(); i++) {
				levels[i] = new Innovation[list2D.get(i).size()];
				for (int j = 0; j < list2D.get(i).size(); j++) {
					levels[i][j] = list2D.get(i).get(j);
				}
			}
		}
		return levels;
	}

	Innovation(Innovation[] previous, ConditionalDate<Territory> default_unlock, double progress_mult) {
		this(previous, default_unlock, progress_mult, false);
	}

	Innovation(Innovation previous, ConditionalDate<Territory> default_unlock, double progress_mult) {
		this(previous == null ? new Innovation[] {} : new Innovation[] { previous }, default_unlock, progress_mult);
	}

	Innovation(Innovation[] previous, ConditionalDate<Territory> default_unlock, double progress_mult,
			boolean chance_based) {
		this.previous = previous;
		this.default_unlock = default_unlock;
		this.progress_mult = progress_mult / 12;
		this.chance_based = false;
	}

	Innovation(Innovation previous, ConditionalDate<Territory> default_unlock, double progress_mult,
			boolean chance_based) {
		this(new Innovation[] { previous }, default_unlock == null ? t -> null : default_unlock, progress_mult,
				chance_based);
	}

	private final Innovation[] previous;
	private final ConditionalDate<Territory> default_unlock;
	private final double progress_mult;
	private final boolean chance_based;

	public Innovation[] getPrevious() {
		return previous;
	}

	public ConditionalDate<Territory> getDefaultUnlock() {
		return default_unlock;
	}

	public CDate getUnlock(Territory t) {
		return default_unlock.check(t);
	}

	public double getProgressMult() {
		return progress_mult;
	}

	public boolean isChanceBased() {
		return chance_based;
	}

	public static double getProgressMult(int years) {
		return 1 / years;
	}

	public static Innovation[] none() {
		return new Innovation[0];
	}
}
