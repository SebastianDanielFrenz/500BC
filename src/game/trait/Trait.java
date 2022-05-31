package game.trait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import map.World;

public enum Trait {

	BRAVE(new CharacterModifier[] { new CharacterModifier(CharacterModifierType.ADD_MARTIAL, 2),
			new CharacterModifier(CharacterModifierType.ADD_PROWESS, 3) },
			new OpinionModifier[] { new OpinionModifier(OpinionType.VASSAL_OPINION, 5),
					new OpinionModifier(OpinionType.ATTRACTION, 10),
					new OpinionModifier(OpinionType.SAME_TRAIT_OPINION, 10),
					new OpinionModifier(OpinionType.OPPOSITE_TRAIT_OPINION, -10) },
			TraitGroup.BRAVERY),

	CRAVEN(new CharacterModifier[] { new CharacterModifier(CharacterModifierType.ADD_INTRIGUE, 2),
			new CharacterModifier(CharacterModifierType.ADD_MARTIAL, -2),
			new CharacterModifier(CharacterModifierType.ADD_PROWESS, -3) },
			new OpinionModifier[] { new OpinionModifier(OpinionType.VASSAL_OPINION, -5),
					new OpinionModifier(OpinionType.ATTRACTION, -10) },
			TraitGroup.BRAVERY),

	CALM(new CharacterModifier[] { new CharacterModifier(CharacterModifierType.ADD_DIPLOMACY, 1),
			new CharacterModifier(CharacterModifierType.ADD_INTRIGUE, 1) },
			new OpinionModifier[] { new OpinionModifier(OpinionType.SAME_TRAIT_OPINION, 10),
					new OpinionModifier(OpinionType.OPPOSITE_TRAIT_OPINION, -10) },
			TraitGroup.CALM),

	CHASTE(new CharacterModifier[] { new CharacterModifier(CharacterModifierType.ADD_LEARNING, 2) },
			new OpinionModifier[] { new OpinionModifier(OpinionType.SAME_TRAIT_OPINION, 10),
					new OpinionModifier(OpinionType.OPPOSITE_TRAIT_OPINION, -10) },
			TraitGroup.LUST);

	public final CharacterModifier[] mods;
	public final OpinionModifier[] opMods;
	public final TraitGroup group;

	Trait(CharacterModifier[] mods, OpinionModifier[] opMods, TraitGroup group) {
		this.mods = mods;
		this.opMods = opMods;
		group.getTraits().add(this);
		this.group = group;
	}

	Trait(CharacterModifier[] mods, OpinionModifier[] opMods, InheritanceRule rule, TraitGroup group) {
		this.mods = mods;
		this.opMods = opMods;
		group.getTraits().add(this);
		this.group = group;
	}

	public boolean clashes(Trait t) {
		return t == this || t.group == group;
	}

	public boolean isAllowed(Collection<Trait> existing) {
		for (Trait t : existing) {
			if (t == null) {
				continue;
			}
			if (t.clashes(this)) {
				return false;
			}
		}
		return true;
	}

	public static List<Trait> generate() {
		// personality
		int i = 0;
		int limit = World.random.nextInt(2) + 2;
		Trait t;
		List<Trait> out = new ArrayList<Trait>();
		while (i < limit) {
			t = values()[World.random.nextInt(values().length)];
			if (t.isAllowed(out)) {
				out.add(t);
				i++;
			}
		}
		return out;
	}

}
