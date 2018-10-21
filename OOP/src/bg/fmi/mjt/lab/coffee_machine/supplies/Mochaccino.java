package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Mochaccino implements Beverage {

	@Override
	public String getName() {
		return "Mochaccino";
	}

	@Override
	public double getMilk() {
		return 150;
	}

	@Override
	public double getCoffee() {
		return 18;
	}

	@Override
	public double getWater() {
		return 0;
	}

	@Override
	public double getCacao() {
		
		return 10;
	}

}
