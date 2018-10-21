package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class Weapon implements Treasure {
	private String name;
	private int damage;
	public Weapon(String name, int damage) {
		this.name=name;
		this.damage=damage;
	}
	@Override
	public String collect(Hero h) {
		h.equip(this);
		return "Weapon found! Damage points: "+damage;
		
	}
	public int getDamage() {
		return damage;
	}
	public String getName() {
		return name;
	}
}
