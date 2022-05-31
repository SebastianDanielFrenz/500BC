package game.character;

import java.util.List;

import game.trait.CharacterModifier;
import game.trait.CharacterModifierType;
import game.trait.Trait;
import map.World;
import util.CDate;
import util.RandomUtil;

public class PlayableCharacter {

	public static final boolean FEMALE = true;
	public static final boolean MALE = false;

	private List<Trait> traits;

	private double base_diplomacy;
	private double base_martial;
	private double base_stewardship;
	private double base_intrigue;
	private double base_learning;
	private double base_prowess;

	private Genetics genetics;

	private boolean gender;

	private CDate birth;

	public CDate getBirth() {
		return birth;
	}

	public double getHealth() {
		return 0;
	}

	public static PlayableCharacter generate() {
		PlayableCharacter c = new PlayableCharacter();
		c.traits = Trait.generate();
		c.base_diplomacy = RandomUtil.random(5, 2);
		c.base_intrigue = RandomUtil.random(5, 2);
		c.base_learning = RandomUtil.random(5, 2);
		c.base_martial = RandomUtil.random(5, 2);
		c.base_prowess = RandomUtil.random(5, 2);
		c.base_stewardship = RandomUtil.random(5, 2);
		return c;
	}

	public double getDiplomacy() {
		double out = base_diplomacy;
		out += genetics.intelligence;
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_DIPLOMACY) {
					out += mod.value;
				}
			}
		}
		return out;
	}

	public double getIntrigue() {
		double out = base_intrigue;
		out += genetics.intelligence;
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_INTRIGUE) {
					out += mod.value;
				}
			}
		}
		return out;
	}

	public double getLearning() {
		double out = base_learning;
		out += genetics.intelligence;
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_LEARNING) {
					out += mod.value;
				}
			}
		}
		return out;
	}

	public double getMartial() {
		double out = base_martial;
		out += genetics.intelligence;
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_MARTIAL) {
					out += mod.value;
				}
			}
		}
		return out;
	}

	public double getProwess() {
		double out = base_prowess;
		if (gender == MALE) {
			out += genetics.male_base_strength;
		} else {
			out += genetics.female_base_strength;
		}
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_PROWESS) {
					out += mod.value;
				}
			}
		}
		return out;
	}

	public double getStewardship() {
		double out = base_stewardship;
		out += genetics.intelligence;
		for (Trait t : traits) {
			for (CharacterModifier mod : t.mods) {
				if (mod.type == CharacterModifierType.ADD_STEWARDSHIP) {
					out += mod.value;
				}
			}
		}
		return out;
	}
}
