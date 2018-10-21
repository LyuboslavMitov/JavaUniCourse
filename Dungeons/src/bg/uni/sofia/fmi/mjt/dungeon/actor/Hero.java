package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Hero implements Actor {
	private String name;
	private int health;
	private final int maxHealth;
	private final int maxMana;
	private int mana;
	private Spell spell;
	private Weapon weapon;
	
	public Hero(String name, int health, int mana) {
		this.name=name;
		this.health=health;
		this.mana=mana;
		this.maxHealth=health;
		this.maxMana=mana;
		spell=null;
		weapon=null;
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

	@Override
	public Weapon getWeapon() {
		return weapon;
	}

	@Override
	public Spell getSpell() {
		return spell;
	}
	
	public void takeHealing(int healingPoints) {
		if(isAlive()) {
			if(health+healingPoints>maxHealth)
				health = maxHealth;
			else
			health=health+healingPoints;
		}
	}
	public void takeMana(int manaPoints) {
		if(mana+manaPoints>maxMana)
			mana = maxMana;
		else
			mana+=manaPoints;
	}
	public void equip(Weapon weapon) {
		if(weapon==null)
			return;
		if(this.weapon==null || this.weapon.getDamage()<weapon.getDamage())
			this.weapon=weapon;
		
	}
	public void learn(Spell spell) {
		if(spell==null)
			return;
		if(this.spell==null || this.spell.getDamage()<spell.getDamage())
			this.spell=spell;
	}
	private boolean canCastSpell(int manacost) {
		if(mana-manacost>=0) {
			mana-=manacost;
			return true;
		}
		return false;
	}
}
