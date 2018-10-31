
package bg.fmi.mjt.lab.coffee_machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.fmi.mjt.lab.coffee_machine.BasicCoffeeMachine;
import bg.fmi.mjt.lab.coffee_machine.CoffeeMachine;
import bg.fmi.mjt.lab.coffee_machine.PremiumCoffeeMachine;
import bg.fmi.mjt.lab.coffee_machine.container.BasicContainer;
import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.container.PremiumContainer;
import bg.fmi.mjt.lab.coffee_machine.supplies.Cappuccino;
import bg.fmi.mjt.lab.coffee_machine.supplies.Espresso;
import bg.fmi.mjt.lab.coffee_machine.supplies.Mochaccino;

public class SampleCoffeeMachineTest {

	private static final double DELTA = 1e-15;

	private static final double BASIC_CONTAINER_INITIAL_WATER = 600;
	private static final double BASIC_CONTAINER_INITIAL_COFFEE = 600;

	private static final double PREMIUM_CONTAINER_INITIAL_WATER = 1000;
	private static final double PREMIUM_CONTAINER_INITIAL_COFFEE = 1000;

	private static final double ESPRESSO_COFFEE = 10;
	private static final double ESPRESSO_WATER = 30;

	private static List<String> lucks= new ArrayList<>();
	private CoffeeMachine basicMachine;
	private CoffeeMachine premiumMachine;

	@Before
	public void setUp() {
		basicMachine = new BasicCoffeeMachine();
		premiumMachine = new PremiumCoffeeMachine();
		lucks.add("If at first you don't succeed call it version 1.0.");
		lucks.add("Today you will make magic happen!");
		lucks.add("Have you tried turning it off and on again");
		lucks.add("Life would be much more easier if you had the source code.");
	}

	@Test
	public void testBasicBrew_UpdatesCorrectlyContainer() {
		basicMachine.brew(new Espresso());
		Container supplies = basicMachine.getSupplies();

		assertEquals(BASIC_CONTAINER_INITIAL_WATER - ESPRESSO_WATER, supplies.getCurrentWater(), DELTA);
		assertEquals(BASIC_CONTAINER_INITIAL_COFFEE - ESPRESSO_COFFEE, supplies.getCurrentCoffee(), DELTA);
	}

	@Test
	public void testPremiumBrew_UpdatesCorrectlyContainer() {
		premiumMachine.brew(new Espresso());
		Container supplies = premiumMachine.getSupplies();

		assertEquals(PREMIUM_CONTAINER_INITIAL_WATER - ESPRESSO_WATER, supplies.getCurrentWater(), DELTA);
		assertEquals(PREMIUM_CONTAINER_INITIAL_COFFEE - ESPRESSO_COFFEE, supplies.getCurrentCoffee(), DELTA);
	}

	@Test
	public void testPremiumBrew_ReturnsFirstLuck() {
		String actual = premiumMachine.brew(new Espresso()).getLuck();
		assertEquals("If at first you don't succeed call it version 1.0.", actual);
	}
//brew without quantity with autorefill
	@Test
	public void testPremiumAutoRefillWorks() {
		int counter =1;
		CoffeeMachine m2 = new PremiumCoffeeMachine(true);
		CoffeeMachine m3 = new PremiumCoffeeMachine();
		assertNull(m2.brew(new Espresso(),5));
		assertNotNull(m2.brew(new Espresso(),3));
		assertNull(m2.brew(new Espresso(),-1));
		assertNull(m2.brew(new Espresso(),0));
		assertNull(m2.brew(new Espresso(),4));
		while (counter<7) {
			assertNotNull(m2.brew(new Mochaccino()));
			assertNotNull(m3.brew(new Mochaccino()));
			System.out.println(counter++);
		}
		assertNotNull(m2.brew(new Mochaccino(),1));
		
		counter=1;
		while (counter<6) {
			assertNotNull(m2.brew(new Mochaccino()));
			System.out.println(counter++);
		}
		assertNotNull(m2.brew(new Mochaccino()));
		
		assertNull(m3.brew(new Mochaccino()));
		assertNull(m3.brew(new Mochaccino(),1));
	}
	@Test
	public void testPremiumLuckAutorefillWorks() {
		CoffeeMachine m = new PremiumCoffeeMachine();
		CoffeeMachine m2 = new BasicCoffeeMachine();
		
		//m.brew(new Espresso());
		String actual = m.brew(new Espresso()).getLuck();
		String actual2 = m.brew(new Espresso()).getLuck();
		String actual3 = m.brew(new Espresso()).getLuck();
		String actual4 = m.brew(new Espresso()).getLuck();
		String actual5 = m.brew(new Espresso()).getLuck();
		String actual6 = m2.brew(new Espresso()).getLuck();
		assertEquals("If at first you don't succeed call it version 1.0.", actual);
		assertEquals("Today you will make magic happen!", actual2);
		assertEquals("Have you tried turning it off and on again?", actual3);
		assertEquals("Life would be much more easier if you had the source code.", actual4);
		assertEquals("If at first you don't succeed call it version 1.0.", actual5);
		assertNull(actual6);
	}
	@Test
	public void testIfLuckListIsCircular() {
		CoffeeMachine premiumMachine = new PremiumCoffeeMachine(true);
		
		for (int i = 0; i < lucks.size(); i++) {
			Product product = premiumMachine.brew(new Espresso());
			assertTrue(lucks.contains(product.getLuck()));
		}
		String currentLuck = premiumMachine.brew(new Espresso()).getLuck();
		assertEquals(lucks.get(0), currentLuck);
	}
	
	
	@Test
	public void testBasicContainer() {
		Container  c= new BasicContainer();
		int counter =1;
		CoffeeMachine m2 = new BasicCoffeeMachine();
		assertFalse(c.canMakeBeverage(new Mochaccino(), 1));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 1));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 5000));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 1));
		assertNull(m2.brew(new Espresso(),5));
		while (counter<21) {
			assertTrue(c.canMakeBeverage(new Espresso(), 1));
			assertNotNull(m2.brew(new Espresso()));
			System.out.println(counter++);
		}
		assertFalse(c.canMakeBeverage(new Espresso(),1));
		assertNull(m2.brew(new Espresso()));
		assertNull(m2.brew(new Espresso(),1));
	}
	@Test
	public void testPremiumContainer() {
		Container  c= new PremiumContainer();
		int counter =1;
		CoffeeMachine m2 = new PremiumCoffeeMachine();
		/*assertFalse(c.canMakeBeverage(new Mochaccino(), 1));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 1));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 5000));
		assertFalse(c.canMakeBeverage(new Cappuccino(), 1));*/
		assertNull(m2.brew(new Espresso(),5));
		while (counter<7) {
			assertNotNull(m2.brew(new Cappuccino()));
			System.out.println(counter++);
		}
		assertNull(m2.brew(new Cappuccino()));
		assertNotNull(m2.brew(new Espresso()));
		assertNull(m2.brew(new Mochaccino()));
	}
	
}