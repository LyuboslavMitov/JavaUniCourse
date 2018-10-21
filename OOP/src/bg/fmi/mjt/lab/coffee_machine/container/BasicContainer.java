package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;
import bg.fmi.mjt.lab.coffee_machine.supplies.Cappuccino;
import bg.fmi.mjt.lab.coffee_machine.supplies.Mochaccino;

public class BasicContainer implements Container {
	
	private double water;
	private double coffee;
	
	public BasicContainer() {
		water=600;
		coffee=600;
	}
	
	@Override
	public double getCurrentWater() {
		return water;
	}

	@Override
	public double getCurrentMilk() {
		return 0;
	}

	@Override
	public double getCurrentCoffee() {
		return coffee;
	}

	@Override
	public double getCurrentCacao() {
		return 0;
	}

	@Override
	public boolean canMakeBeverage(Beverage b,int q) {
		if(b instanceof Cappuccino || b instanceof Mochaccino || q!=1) {
			return false;
		}
		if(water-b.getWater()*q<0 || coffee-b.getCoffee()*q<0) {
			return false;
		}
		water-=b.getWater();
		coffee-=b.getCoffee();
		return true;
	}

}
