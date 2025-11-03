package onelanetraffic;

import java.util.Random;
import exceptionclasses.*;

/**
 * Represents a one-lane road containing vehicles (cars and buses).
 * Manages vehicle movement, collisions, and road state in the traffic simulation.
 * The road is implemented as an array where each index represents a position on the road.
 *
 * @author Lehan Zhang
 */
public class Road {
    private Vehicle[] vehicles; // an array that stores vehicle objects
    private int numVehicles; // The number of vehicles on the road
    private int current; // the array index of the current vehicle to be processed with command
    private ReusePool reusePool = new ReusePool(); // use a linkedQueue to store the crashed vehicles for later reuse

    /**
     * initialize an empty road with a given size
     * set numVehicles, current, and reusePool to intial value
     *
     * @param size the size of the vehicles array
     */
    public Road(int size) {
        numVehicles = 0;
        vehicles = new Vehicle[size];
        current = 0;
        reusePool = new ReusePool();
    }

    /**
     * Constructs a new road of a specific size and populates it with a certain
     * number of vehicles.
     *
     * @param size the size of the vehicles array
     * @param numVehicles number of vehicles on the road
     */
    public Road(int size, int numVehicles) {
        vehicles = new Vehicle[size];
        this.numVehicles = numVehicles;
        populateRoad();
        setCurrent();
    }

    /**
     * Retrieves the array of vehicles currently on the road.
     *
     * @return an array of Vehicle objects representing the vehicles on the road
     */
    public Vehicle[] getVehicles() {
        return vehicles;
    }

    /**
     * Retrieves the number of vehicles currently on the road.
     *
     * @return the total number of vehicles present on the road
     */
    public int getNumVehicles() {
        return numVehicles;
    }

    /**
     * Updates the number of vehicles currently on the road.
     *
     * @param numVehicles the new total number of vehicles to set on the road
     */
    public void setNumVehicles(int numVehicles) {
        this.numVehicles = numVehicles;
    }

    /**
     * Updates the current vehicle's position on the road.
     * Attempts to randomly select a non-null vehicle to set as the current vehicle.
     * If no vehicle can be randomly selected within the specified number of attempts (maxTries),
     * a linear search is performed to find the first non-null vehicle.
     */
    public void setCurrent() {
        // limited loops try to find a random index for movement
        Random rand = new Random();
        int randomIndex = -1;
        int maxTries = vehicles.length;
        for (int i = 0; i < maxTries; i++) {
            randomIndex = rand.nextInt(vehicles.length);
            if (vehicles[randomIndex] != null) {
                current = randomIndex;
                break;
            } else {
                randomIndex = -1;
            }
        }
        // use a linear approach if fail to randomly find one
        if (randomIndex == -1) {
            for (int i = 0; i < vehicles.length; i++) {
                if (vehicles[i] != null) {
                    current = i;
                    break;
                }
            }
        }
    }

    /**
     * Retrieves the reuse pool associated with the road.
     *
     * @return the ReusePool object associated with the road
     */
    public ReusePool getReusePool() {
        return reusePool;
    }

    /**
     * Sets the reuse pool for the road. The reuse pool is used to manage
     *
     * @param reusePool the ReusePool object to be associated with this road
     */
    public void setReusePool(ReusePool reusePool) {
        this.reusePool = reusePool;
    }

    /**
     * Retrieves the size of the vehicles array on the road.
     *
     * @return the size of the vehicles array
     */
    public int getSize() {
        return vehicles.length;
    }

    /**
     * Retrieves 1-based position of the currently selected vehicle on the road.
     *
     * @return the 1-based position of the current vehicle on the road
     */
    public int getPosition() {
        return current + 1;
    }

    /**
     * Moves the current vehicle on the road in the specified direction.
     * The movement is subject to boundary conditions and interactions with other vehicles
     * at the target location, such as collisions.
     *
     * @param direction the direction of movement:
     *                  0 indicates no movement,
     *                  -1 indicates a move to the left,
     *                  1 indicates a move to the right
     */
    public void moveVehicle(int direction) {
        if (direction == 0) {
            System.out.println(vehicles[current] + "remain at current location");
            return;
        }
        // The first and last vehicle on the road should not move if out of bound.
        boolean atLeftEdge = current == 0 && direction == -1;
        boolean atRightEdge = current == vehicles.length - 1 && direction == 1;
        if (atLeftEdge || atRightEdge) {
            System.out.println("\nTarget location out of range, invalid move instruction.");
            return;
        }

        int target = current + direction; // target index after movement

        // move current vehicle to target location
        if (vehicles[target] == null) {
            System.out.println(vehicles[current] + " moved to position " + (target + 1));
            vehicles[target] = vehicles[current];
            vehicles[current] = null;
        } else {
            collision(target);
        }
    }

    /**
     * Adds a new vehicle to the road by reusing an existing vehicle from the reuse pool.
     * The method attempts to retrieve a vehicle from the reuse pool. If the pool is empty,
     * an EmptyQueueException is caught, and an error message is printed. Once a vehicle is retrieved,
     * it is placed in a random empty position on the road.
     */
    private void addVehicle() {
        Vehicle vehicleToAdd = null;
        try {
            vehicleToAdd = reusePool.reuseVehicle();
        } catch (EmptyQueueException ex) {
            System.out.println(ex.getMessage());
        }

        Random rand = new Random();
        int indexToAdd = rand.nextInt(vehicles.length);

        while (vehicles[indexToAdd] != null) {
            indexToAdd = rand.nextInt(vehicles.length);
        }
        vehicles[indexToAdd] = vehicleToAdd;
        numVehicles++;
        System.out.println("\nA repaired vehicle, " + vehicleToAdd + ", added to position " + (indexToAdd + 1));
        System.out.println(reusePool);
    }

    /**
     * Handles collisions between the current vehicle and a target vehicle.
     * Based on the types of the vehicles involved (car or bus), it determines
     * the appropriate collision handling method to invoke and executes it.
     *
     * @param target the index of the target vehicle in the vehicles array
     */
    private void collision(int target) {
        Vehicle currentVehicle = vehicles[current];
        Vehicle otherVehicle = vehicles[target];

        // Determines the collision type and handles accordingly
        if (currentVehicle.isCar() && otherVehicle.isCar()) {
            carVsCar(target);
        }
        else if (currentVehicle.isBus() && otherVehicle.isBus()) {
            busVsBus(target);
        }
        else if (currentVehicle.isCar() && otherVehicle.isBus()) {
            carVsBus(target, true); // Car moves into bus
        }
        else if (currentVehicle.isBus() && otherVehicle.isCar()) {
            carVsBus(target,false); // Bus moves into a car
        }
    }

    /**
     * Handles collisions between two cars based on color and horsepower rules.
     * Rules:
     * - Different colors: Both cars crash and are removed, a new car is added
     * - Same color: Car with higher horsepower survives
     *
     * @param target the index of the target location
     */
    private void carVsCar(int target) {
        // Cast vehicles to Car for access to car-specific methods
        Car currentCar = (Car) vehicles[current];
        Car otherCar = (Car) vehicles[target];

        if (!currentCar.sameColor(otherCar)) {
            System.out.println("\n" + currentCar + " vs " + otherCar
                    + "\nCars of different colors crashed! Removing both.");
            // add crashed vehicles to reuse pool
            reusePool.recycleVehicle(currentCar);
            reusePool.recycleVehicle(otherCar);
            System.out.print(reusePool);

            vehicles[current] = null;
            vehicles[target] = null;
            numVehicles -= 2;
            addVehicle();// Add a vehicle from the reuse pool to the road
        } else {
            if (currentCar.getHorsePower() > otherCar.getHorsePower()) {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        getPosition() + " has greater HP, Car at position " + (target + 1) + " is removed.");
                reusePool.recycleVehicle(otherCar);
                vehicles[target] = null;
            } else {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        (target +1) + " has greater HP, Car at position " + getPosition() + " is removed.");
                reusePool.recycleVehicle(currentCar);
                vehicles[current] = null;
            }
            numVehicles--;
            System.out.print(reusePool);
        }
    }

    /**
     * Handles interactions between a car and a bus.
     *
     * Rules:
     * - Car tries to move into bus space: Car stops (cannot move)
     * - Bus moves into a car space: Bus pushes the car out (the car is removed)
     *
     * @param target the index of the target location
     * @param type true if the car is moving into a bus, false if vise versa
     */
    private void carVsBus(int target, boolean type) {
        if (type) {
            // Car tries to move into Bus's space
            System.out.println("\n" + vehicles[current] + " vs " + vehicles[target] + "\nCar at position "
                    + getPosition() + " stops. Cannot move into Bus's space.");
        } else {
            // Bus moves into Car's space, Bus pushes the Car out
            System.out.println("\n" + vehicles[current] + " vs " + vehicles[target] + "\nBus at position "
                    + getPosition() + " pushes Car at position " + (target + 1) + " out.");

            reusePool.recycleVehicle(vehicles[target]);
            System.out.print(reusePool);

            // Move bus to the car's location, the car is removed
            vehicles[target] = vehicles[current];
            vehicles[current] = null;
            numVehicles--;
        }
    }

    /**
     * Handles collisions between two buses based on weight comparison.
     *
     * Rules:
     * - Different weights: Heavier bus survives, lighter bus is removed
     * - Equal weights: Both buses remain in place
     *
     * @param target the index of the target location
     */
    private void busVsBus(int target) {
        // Cast vehicles to Bus for access to bus-specific methods
        Bus currentBus = (Bus) vehicles[current];
        Bus otherBus = (Bus) vehicles[target];

        if (currentBus.getWeight() > otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + getPosition() + " has more weight, Bus at position " + (target + 1) + " is removed.");
            reusePool.recycleVehicle(otherBus);
            System.out.print(reusePool);

            vehicles[target] = null;
            numVehicles--;
        } else if (currentBus.getWeight() < otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + (target +1) + " has more weight, Bus at position " + getPosition() + " is removed.");
            reusePool.recycleVehicle(currentBus);
            System.out.print(reusePool);

            vehicles[current] = null;
            numVehicles--;
        } else {
            // Equal weight → both buses remain
            System.out.println("\n" + currentBus + " vs " + otherBus
                    + "\nBuses of same weight, both remain in place.");
        }
    }

    /**
     * Populates the road with a specified number of vehicles, assigning each one to a random index
     * in the vehicles array. Each vehicle is created randomly, with 70% chance of being a car, and 30%
     * chance of being a bus.
     */
    private void populateRoad() {
        Random rand = new Random();

        for (int i = 0; i < numVehicles; i++) {
            int index = rand.nextInt(vehicles.length); // generate random index to assign a vehicle

            while (vehicles[index] != null) {
                index = new Random().nextInt(vehicles.length);
            }

            double type = new Random().nextDouble(1.0); // generate 0 or 1 to randomly choose Car (0) or Bus (1)
            if (type < 0.7) {
                vehicles[index] = new Car();
            } else {
                vehicles[index] = new Bus();
            }
        }
    }

    /**
     * Creates a string representation of the current road state.
     * Shows all vehicles and their positions, with empty positions marked.
     *
     * @return a multi-line string showing the road layout
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < vehicles.length; i++) {
            if (vehicles[i] != null) {
                str.append("Position ").append(i + 1).append(": ").
                        append(vehicles[i].toString()).append("\n");
            } else {
                str.append("Position ").append(i + 1).append(": [Empty]\n");
            }
        }
        return str.toString();
    }
}