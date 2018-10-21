package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.BasicContainer;
import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class BasicCoffeeMachine implements CoffeeMachine {
	
	Container container;

	public BasicCoffeeMachine() {
		container=new BasicContainer();
	}
	@Override
	public void refill() {
		container=new BasicContainer();
	}

	@Override
	public Product brew(Beverage beverage) {
		if(container.canMakeBeverage(beverage, 1))
			return new Product(beverage.getName(), 1 , null);
		return null;
	}
	@Override
	public Product brew(Beverage beverage,int q) {
		return null;
	}

	@Override
	public Container getSupplies() {
		return container;
	}

}
