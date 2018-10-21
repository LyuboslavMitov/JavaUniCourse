package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class HealthPotion implements Treasure {
	private int healingPoints;
	
	public HealthPotion(int healingPoints) {
		this.healingPoints=healingPoints;
	}
	public int heal() {
		return healingPoints;
	}
	@Override
	public String collect(Hero h) {
		h.takeHealing(healingPoints);
		return String.format("Health potion found! %s health points added to your hero!", healingPoints);
	}

}
