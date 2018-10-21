package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Enemy implements Actor {

	private String name;
	private int health;
	private int mana;
	private Spell spell;
	private Weapon weapon;
	
	
	public Enemy(String name, int health, int mana, Weapon weapon, Spell spell) {
		this.health=health;
		this.name=name;
		this.mana=mana;
		this.weapon=weapon;
		this.spell=spell;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public int getMana() {
		return mana;
	}

	@Override
	public boolean isAlive() {
		if(health>0)
			return true;
		return false;
	}

	@Override
	public void takeDamage(int damagePoints) {
		if(damagePoints<0)
			return;
		if(health-damagePoints<0)
			health=0;
		else
			health-=damagePoints;
	}

	@Override
	public Weapon getWeapon() {
		return weapon;
	}

	@Override
	public Spell getSpell() {
		return spell;
	}

	@Override
	public int attack() {
		int weaponDamage=0;
		int spellDamage=0;
		if(weapon!=null)
			weaponDamage=weapon.getDamage();
		if(spell!=null)
			spellDamage=spell.getDamage();
		
		if(spellDamage>weaponDamage && canCastSpell(spell.getManaCost())) {
			return spellDamage;
		}
		return weaponDamage;
	}
	private boolean canCastSpell(int manacost) {
		if(mana-manacost>=0) {
			mana-=manacost;
			return true;
		}
		return false;
	}
}
