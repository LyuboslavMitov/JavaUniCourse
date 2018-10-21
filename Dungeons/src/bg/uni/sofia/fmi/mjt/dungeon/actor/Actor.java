package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public interface Actor {
	public String getName();
	public int getHealth();
	public int getMana();
	public boolean isAlive();
	public void takeDamage(int damagePoints);
	public Weapon getWeapon();
	public Spell getSpell();
	public int attack();
}
