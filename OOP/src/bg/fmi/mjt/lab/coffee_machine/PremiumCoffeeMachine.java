package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.container.PremiumContainer;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class PremiumCoffeeMachine implements CoffeeMachine {
	
	private String [] lucks = {
			"If at first you don't succeed call it version 1.0.",
			"Today you will make magic happen!",
			"Have you tried turning it off and on again?",
			"Life would be much more easier if you had the source code."
	};
	private int luckCounter=0;
	private Container container;
	private boolean autoRefill=false;
	
	public PremiumCoffeeMachine(){
		container = new PremiumContainer();
	}
	public PremiumCoffeeMachine(boolean autoRefill){
		container = new PremiumContainer();
		this.autoRefill=autoRefill;
	}
	
	@Override
	public Product brew(Beverage beverage, int quantity) {
		if(quantity<=0 || quantity>3)
			return null;
		if(!container.canMakeBeverage(beverage,quantity)) {
			if(autoRefill) {
				refill();
				container.canMakeBeverage(beverage, quantity);
				return new Product(beverage.getName(),quantity,lucks[luckCounter++%4]);
			}
			else 
				return null;
		}
		container.canMakeBeverage(beverage,quantity);
		return new Product(beverage.getName(),quantity,lucks[luckCounter++%4]);
	}
	
	@Override
	public void refill() {
		container = new PremiumContainer();
	}

	@Override
	public Product brew(Beverage beverage) {
		if(container.canMakeBeverage(beverage,1))
			return new Product(beverage.getName(),1,lucks[luckCounter++%4]);
		else if(autoRefill) {
			refill();
			container.canMakeBeverage(beverage, 1);
			return new Product(beverage.getName(),1,lucks[luckCounter++%4]);
		}
		else
			return null;
	}

	@Override
	public Container getSupplies() {
		return container;
	}

	
}
