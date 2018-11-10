package bg.sofia.uni.fmi.mjt.carstore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.exception.CarNotFoundException;

public class CarStore {
	private Map<String,Car> cars = new HashMap<>();
	private Set<Car> sorted = new TreeSet<>();
  /**
  * Adds the specified car in the store.
  * @return true if the car was added successfully to the store
  */
  public boolean add(Car car) {
    if(cars.containsKey(car.getRegistrationNumber()))
    	return false;
    cars.put(car.getRegistrationNumber(),car);
    sorted.add(car);
	return true;
  }

  /**
  * Adds all of the elements of the specified collection in the store.
  * @return true if the store cars are changed after the execution (i.e. at least one new car is added to the store
  */
  public boolean addAll(Collection<Car> cars) {
	sorted.addAll(cars);
    return cars.addAll(cars);
    
  }

  /**
  * Removes the specified car from the store.
  * @return true if the car is successfully removed from the store
  */
  public boolean remove(Car car) {
	  if(!cars.containsKey(car.getRegistrationNumber()))
	    	return false;
	    cars.remove(car.getRegistrationNumber());
	    sorted.remove(car);
	    return true;
  }

  /**
  * Returns all cars of a given model.
  */
  public Collection<Car> getCarsByModel(Model model) {
	  List<Car> result = new ArrayList<>();
	  for (Car car : sorted) {
		  if(car.getModel().equals(model)) {
			  result.add(car);
		  }
	}
    return result;
  }

  /**
  * Finds a car from the store by its registration number.
  * @throws CarNotFoundException if a car with this registration number is not found in the store
  **/
  public Car getCarByRegistrationNumber(String registrationNumber) {
    Car result = cars.get(registrationNumber);
    if(result!=null) {
    	return result;
    }
    throw new CarNotFoundException();
  }

   /**
   * Returns all cars sorted by their default order*
   **/
  public Collection<Car> getCars() {
    return sorted;
  }
  /**
  * Returns all cars sorted according to the order induced by the specified comparator.
  */
  public Collection<Car> getCars(Comparator<Car> comparator) {
    Set<Car> sortedByComparator = new TreeSet<>(comparator);
    sortedByComparator.addAll(sorted);
    return sortedByComparator;
  }

  /**
  * Returns all cars sorted according to the given comparator and boolean flag for order.
  * @param isReversed if true the cars should be returned in reversed order
  */
  public Collection<Car> getCars(Comparator<Car> comparator, boolean isReversed) {
	  if(isReversed) {
		  comparator=comparator.reversed();
	  }
	  Set<Car> sortedByComparator = new TreeSet<>(comparator);
	  sortedByComparator.addAll(cars.values());
	  List<Car> result = new ArrayList<>(sortedByComparator);
      return result;
  }

  /**
  * Returns the total number of cars in the store.
  */
  public int getNumberOfCars() {
    return cars.values().size();
  }

  /**
  * Returns the total price of all cars in the store.
  */
  public int getTotalPriceForCars() {
    int result=0;
    for(Car car: cars.values()) {
    	result+=car.getPrice();
    }
    return result;
  }
}