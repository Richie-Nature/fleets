package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private PriceClient priceClient;
    private MapsClient mapsClient;

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapsClient) {
        this.repository = repository;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll().stream().map(car -> {
            car.setLocation(mapsClient.getAddress(car.getLocation()));
            return car;
        }).collect(Collectors.toList());
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * Finds the car by ID from the `repository` if it exists.
         *   If it does not exist, throws a CarNotFoundException
         */
        Optional<Car> optionalCar = repository.findById(id);
        Car car =  optionalCar.orElseThrow(CarNotFoundException::new);

        /**
         * Uses the Pricing Web client created in `VehiclesApiApplication`
         * to get the price based on the `id` input' and then sets the
         * price of the car
         */
        car.setPrice(priceClient.getPrice(id));


        /**
         * Uses the Maps Web client created in `VehiclesApiApplication`
         *   to get the address for the vehicle and feeds the location of
         *   the car object to the Maps service.
         */
         car.setLocation(mapsClient.getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setModifiedAt(car.getModifiedAt());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         *  Finds the car by ID from the `repository` if it exists.
         *  If it does not exist, throws a CarNotFoundException else
         *  delete the car from the repository.
         */
        System.out.println("Car id to delete "+ id);
      repository.findById(id).map(car -> {
            repository.delete(car);
            return car;
      }).orElseThrow(CarNotFoundException::new);
    }
}
