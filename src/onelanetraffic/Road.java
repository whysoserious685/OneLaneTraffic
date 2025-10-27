package onelanetraffic;

import java.util.Random;
import exceptionclasses.*;

/**
 * Represents a one-lane road containing vehicles (cars and buses).
 * Manages vehicle movement, collisions, and road state in the traffic simulation.
 * The road is implemented as an array where each index represents a position.
 *
 * @author Lehan Zhang
 */
public class Road {
    private Vehicle[] road; // Array storing vehicles on the road, null represents empty positions
    private int numVehicles; // The number of vehicles on the road
    private int current; // indicate the current location index on the road
    private ReusePool reusePool = new ReusePool();

    /**
     * default constructor to create a road with size 6 and
     * populate it with random vehicles
     */
    public Road() {
        road = new Vehicle[6];
        numVehicles = 0;
        populateRoad();
    }

    /**
     * Constructs a new road of with a specific size and populates it with random vehicles.
     *
     * @param size the size of the vehicle array
     * @param numVehicles number of vehicles on the road by user input
     */
    public Road(int size, int numVehicles) {
        road = new Vehicle[size];
        this.numVehicles = numVehicles;
        populateRoad();
        setCurrent();
    }

    public int getCurrent() {
        return current;
    }

    /**
     *
     * @return the 1-based position of the current vehicle
     */
    public int getPosition() {
        return current + 1;
    }

    private void setCurrent() {
        // set current to the index of the first vehicle on the road
        for (int i = 0; i < road.length; i++) {
            if (road[i] != null) {
                current = i;
                break;
            }
        }
    }

    /**
     * Attempts to move a vehicle from the current position to a
     * new position on the road. Movement is handled by adding the
     * direction to the current index.
     *
     * @param direction the movement direction (-1 backward, 0 stay, +1 forward)
     */
    public void moveVehicle(int direction) {
        setCurrent();
        // The first and last vehicle on the road should not move if out of bound.
        boolean atLeftEdge = current == 0 && direction == -1;
        boolean atRightEdge = current == road.length - 1 && direction == 1;
        if (atLeftEdge || atRightEdge) {
            System.out.println("\nTarget location out of range, invalid move instruction.");
            return;
        }

        int target = current + direction; // the target moving location

        // move current vehicle to target location
        if (road[target] == null) {
            System.out.println(road[current] + " moved to position " + (target + 1));
            road[target] = road[current];
            road[current] = null;
        } else  {
            try {
                collision(target);
            } catch (EmptyQueueException ex) {
                System.out.println(ex.getMessage());
            }
        }
        // update current to the index of the next vehicle on the road
        do {
            current++;
            current = (current + 1) % road.length;
        } while (road[current] == null);
    }

    /**
     * Adds a new vehicle to a random empty position on the road.
     * Used when new vehicles are created after collisions.
     *
     * @param vehicle the vehicle to add to the road
     */
    private void addVehicle(Vehicle vehicle) throws EmptyQueueException {
        Vehicle newVehicle = reusePool.reuseVehicle();

//        Random rand = new Random();
//        int attempts = 0;
//        int maxAttempts = road.length * 3; // Prevent infinite loops if the array is full
//
//        // Try to find an empty spot
//        while (attempts < maxAttempts) {
//            int randomIndex = rand.nextInt(road.length);
//            if (road[randomIndex] == null) {
//                road[randomIndex] = vehicle;
//                System.out.println("New vehicle added at position " + randomIndex);
//                return;
//            }
//            attempts++;
//        }
//
//        // No empty spot found
//        System.out.println("Road is full. Could not add new vehicle.");
    }

    /**
     * Handles collisions between vehicles by determining their types and applying appropriate rules.
     * Calls corresponding methods according to the collision type
     *
     * @param target the position of the vehicle being moved into
     */
    private void collision(int target) throws EmptyQueueException {
        Vehicle currentVehicle = road[current];
        Vehicle otherVehicle = road[target];

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
     *
     * Rules:
     * - Different colors: Both cars crash and are removed, new car is added
     * - Same color: Car with higher horsepower survives
     *
     * @param target position of the second car
     */
    private void carVsCar(int target) throws EmptyQueueException {
        // Cast vehicles to Car for access to car-specific methods
        Car currentCar = (Car)road[current];
        Car otherCar = (Car)road[target];

        if (currentCar.sameColor(otherCar)) {
            // Different colors, both crash and are removed
            System.out.println("\n" + currentCar + " vs " + otherCar
                    + "\nCars of different colors crashed! Removing both.");
            reusePool.recycleVehicle(currentCar); // add crashed vehicles to reuse pool
            reusePool.recycleVehicle(otherCar);
            road[current] = null;
            road[target] = null;

            // Add a replacement car at a random position
            Car newCar = (Car)reusePool.reuseVehicle();
            addVehicle(newCar);
        } else {
            // Same color, car with higher horsepower survives
            if (currentCar.getHorsePower() > otherCar.getHorsePower()) {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        (current +1) + " has greater HP, Car at position " + (target + 1) + " is removed.");
                reusePool.recycleVehicle(otherCar);
                road[target] = null;
            } else {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        (target +1) + "has greater HP, Car at position " + (current + 1) + " is removed.");
                reusePool.recycleVehicle(currentCar);
                road[current] = null;
            }
        }
    }

    /**
     * Handles interactions between a car and a bus.
     *
     * Rules:
     * - Car tries to move into bus space: Car stops (cannot move)
     * - Bus moves into a car space: Bus pushes the car out (the car is removed)
     *
     * @param target position of the bus
     * @param type true if the car is moving into a bus, false if a bus is moving into a car
     */
    private void carVsBus(int target, boolean type) throws EmptyQueueException {
        if (type) {
            // Car tries to move into Bus's space, the car stops
            System.out.println("\n" + road[current] + " vs " + road[target] + "\nCar at position "
                    + (current + 1) + " stops. Cannot move into Bus's space.");
        } else {
            // Bus moves into Car's space, Bus pushes the Car out
            System.out.println("\n" + road[current] + " vs " + road[target] + "\nBus at position "
                    + (current +1) + " pushes Car at position " + (target + 1) + " out.");

            // Move bus to the car's location, car is removed
            road[target] = road[current];
            reusePool.recycleVehicle(road[target]);
            road[current] = null;  // Clear bus's old position
        }
    }

    /**
     * Handles collisions between two buses based on weight comparison.
     *
     * Rules:
     * - Different weights: Heavier bus survives, lighter bus is removed
     * - Equal weights: Both buses remain in place
     *
     * @param target position of the first bus
     */
    private void busVsBus(int target) throws EmptyQueueException {
        // Cast vehicles to Bus for access to bus-specific methods
        Bus currentBus = (Bus) road[current];
        Bus otherBus = (Bus) road[target];

        if (currentBus.getWeight() > otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + (current +1) + " has more weight, Bus at position " + (target + 1) + " is removed.");
            reusePool.recycleVehicle(otherBus);
            road[target] = null;
        } else if (currentBus.getWeight() < otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + (target +1) + " has more weight, Bus at position " + (current + 1) + " is removed.");
            reusePool.recycleVehicle(currentBus);
            road[current] = null;
        } else {
            // Equal weight → both buses remain
            System.out.println("\n" + currentBus + " vs " + otherBus
                    + "\nBuses of same weight, both remain in place.");
        }
    }

    /**
     * Place a certain number of random vehicles(bus, car) on a random
     * location on the road.
     */
    private void populateRoad() {
        Random rand = new Random();
        int type = new Random().nextInt(2); // generate 0 or 1 to randomly choose Car (0) or Bus (1)

        for (int i = 0; i < numVehicles; i++) {
            int index = rand.nextInt(road.length); // generate random index to assign a vehicle

            while (road[index] != null) {
                index = new Random().nextInt(road.length);
            }
            if (type == 0) {
                road[index] = new Car();
            } else {
                road[index] = new Bus();
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
        for (int i = 0; i < road.length; i++) {
            if (road[i] != null) {
                str.append(road[i].toString()).append("\n");
            } else {
                str.append("Position ").append(i + 1).append(": [Empty]\n");
            }
        }
        return str.toString();
    }
}