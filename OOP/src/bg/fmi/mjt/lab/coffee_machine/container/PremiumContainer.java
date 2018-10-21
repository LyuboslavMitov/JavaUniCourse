package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class PremiumContainer implements Container {
	
	private double water;
	private double coffee;
	private double milk;
	private double cacao;
	
	public PremiumContainer() {
		water=1000;
		coffee=1000;
		milk=1000;
		cacao=300;
	}
	@Override
	public double getCurrentWater() {
		return water;
		
	}

	@Override
	public double getCurrentMilk() {
		
		return milk;
	}

	@Override
	public double getCurrentCoffee() {
	
		return coffee;
	}

	@Override
	public double getCurrentCacao() {
 		return cacao;
	}

	@Override
	public boolean canMakeBeverage(Beverage b, int q) {
		if(water-b.getWater()*q<0 || coffee-b.getCoffee()*q<0 || milk-b.getMilk()*q< 0 || cacao-b.getCacao()*q<0) {
			return false;
		}
		else {
			water-=b.getWater();
			coffee-=b.getCoffee();
			milk-=b.getMilk();
			cacao-=b.getCacao();
			return true;
		}	
	}
	
}
