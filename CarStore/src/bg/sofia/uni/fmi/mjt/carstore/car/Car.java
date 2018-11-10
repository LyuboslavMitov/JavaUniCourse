package bg.sofia.uni.fmi.mjt.carstore.car;

import java.util.Random;

import bg.sofia.uni.fmi.mjt.carstore.enums.EngineType;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.enums.Region;

public abstract class Car implements Comparable<Car> {
	
	  private Model model;
	  private int year;
	  private int price;
	  private EngineType engine;
	  private String registrationNumber;
	  private Region region;
	  private static int REGNUMBER=1000;
	public Car(Model model, int year, int price, EngineType engine,Region region) {
		this.model = model;
		this.year = year;
		this.price = price;
		this.engine = engine;
		this.region=region;
		setRegistrationNumber();
	}

	/**
	  * Returns the model of the car.
	  */
	  public Model getModel() {
		  return model;
	  }

	  /**
	  * Returns the year of manufacture of the car.
	  */
	  public  int getYear() {
		  return year;
	  }

	  /**
	  * Returns the price of the car.
	  */
	  public  int getPrice() {
		  return price;
	  }

	  /**
	  * Returns the engine type of the car.
	  */
	  public EngineType getEngineType() {
		  return engine;
	  }

	  /**
	  * Returns the unique registration number of the car.
	  */
	  public  String getRegistrationNumber() {
		  return registrationNumber;
	  }

		@Override
		public int compareTo(Car o) {
			int m = this.getModel().compareTo(o.getModel());
			if(m!=0) {
				return m;
			}
			return Integer.compare(this.getYear(), o.getYear());
		}
		private void setRegistrationNumber() {
			StringBuilder reg = new StringBuilder();
			reg.append(region.getPrefix())
			.append(REGNUMBER++)
			.append(getRandomAlphabetChar())
			.append(getRandomAlphabetChar());
			registrationNumber=reg.toString();
		}
		
		private char getRandomAlphabetChar() {
			Random r = new Random();
			return Character.toUpperCase((char)(r.nextInt(26) + 'a'));
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((engine == null) ? 0 : engine.hashCode());
			result = prime * result + ((model == null) ? 0 : model.hashCode());
			result = prime * result + price;
			result = prime * result + ((region == null) ? 0 : region.hashCode());
			result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
			result = prime * result + year;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Car other = (Car) obj;
			if (engine != other.engine) {
				return false;
			}
			if (model != other.model) {
				return false;
			}
			if (price != other.price) {
				return false;
			}
			if (region != other.region) {
				return false;
			}
			if (registrationNumber == null) {
				if (other.registrationNumber != null) {
					return false;
				}
			} else if (!registrationNumber.equals(other.registrationNumber)) {
				return false;
			}
			if (year != other.year) {
				return false;
			}
			return true;
		}
}
