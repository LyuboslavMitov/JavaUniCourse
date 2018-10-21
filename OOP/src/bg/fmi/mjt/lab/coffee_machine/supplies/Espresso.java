package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Espresso implements Beverage {

	@Override
	public String getName() {
		return "Espresso";
	}

	@Override
	public double getMilk() {
		return 0;
	}

	@Override
	public double getCoffee() {
		return 10;
	}

	@Override
	public double getWater() {
		return 30;
	}

	@Override
	public double getCacao() {
		return 0;
	}

}
