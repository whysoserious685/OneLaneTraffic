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
    private Vehicle[] Vehicles; // an array that stores vehicle objects
    private int numVehicles; // The number of vehicles on the road
    private int current; // the array index of the current vehicle to be processed with command
    private ReusePool reusePool = new ReusePool(); // use a linkedQueue to store the crashed vehicles for later reuse

    /**
     * initialize an empty road with a given size
     * set numVehicles, current, and reusePool to 0
     *
     * @param size the size of the road array
     */
    public Road(int size) {
        numVehicles = 0;
        Vehicles = new Vehicle[size];
        current = 0;
        reusePool = new ReusePool();
    }

    /**
     * Constructs a new road of with a specific size and populates it with random vehicles.
     *
     * @param size the size of the vehicle array
     * @param numVehicles number of vehicles on the road by user input
     */
    public Road(int size, int numVehicles) {
        Vehicles = new Vehicle[size];
        this.numVehicles = numVehicles;
        populateRoad();
        setCurrent();
    }

    public Vehicle[] getVehicles() {
        return Vehicles;
    }

    public int getNumVehicles() {
        return numVehicles;
    }

    public void setNumVehicles(int numVehicles) {
        this.numVehicles = numVehicles;
    }

    /**
     * need fix populate road does not set numVehicles
     */
    public void setCurrent() {
        // if the road becomes empty, fill the road with vehicles
        if (numVehicles == 0) {
            numVehicles = Vehicles.length;
            populateRoad();
        }
        // Try to find a random index for movement(given 10 tries)
        Random rand = new Random();
        int randomIndex = -1;
        int maxTries = 10;
        for (int i = 0; i < maxTries; i++) {
            randomIndex = rand.nextInt(Vehicles.length);
            if (Vehicles[randomIndex] != null) {
                current = randomIndex;
                break;
            } else {
                randomIndex = -1;
            }
        }
        // use a linear approach if fail to randomly find one
        if (randomIndex == -1) {
            for (int i = 0; i < Vehicles.length; i++) {
                if (Vehicles[i] != null) {
                    current = i;
                    break;
                }
            }
        }
    }

    public ReusePool getReusePool() {
        return reusePool;
    }

    public void setReusePool(ReusePool reusePool) {
        this.reusePool = reusePool;
    }

    public int getSize() {
        return Vehicles.length;
    }

    /**
     *
     * @return the 1-based position of the vehicle on the road
     */
    public int getPosition() {
        return current + 1;
    }

    /**
     * Attempts to move a vehicle from the current position to a
     * new position on the road. Movement is handled by adding the
     * direction to the current index.
     *
     * @param direction the movement direction (-1 backward, 0 stay, +1 forward)
     */
    public void moveVehicle(int direction) {
        if (direction == 0) {
            System.out.println(Vehicles[current] + "remain at current location");
            return;
        }
        // The first and last vehicle on the road should not move if out of bound.
        boolean atLeftEdge = current == 0 && direction == -1;
        boolean atRightEdge = current == Vehicles.length - 1 && direction == 1;
        if (atLeftEdge || atRightEdge) {
            System.out.println("\nTarget location out of range, invalid move instruction.");
            return;
        }

        int target = current + direction; // target index after movement

        // move current vehicle to target location
        if (Vehicles[target] == null) {
            System.out.println(Vehicles[current] + " moved to position " + (target + 1));
            Vehicles[target] = Vehicles[current];
            Vehicles[current] = null;
        } else {
            collision(target);
        }
    }

    /**
     * Adds a new vehicle to a random empty position on the road.
     * Used when new vehicles are created after collisions.
     *
     */
    private void addVehicle() {
        Vehicle vehicleToAdd = null;
        try {
            vehicleToAdd = reusePool.reuseVehicle();
        } catch (EmptyQueueException ex) {
            System.out.println(ex.getMessage());
        }

        Random rand = new Random();
        int indexToAdd = rand.nextInt(Vehicles.length);

        while (Vehicles[indexToAdd] != null) {
            indexToAdd = rand.nextInt(Vehicles.length);
        }
        Vehicles[indexToAdd] = vehicleToAdd;
        numVehicles++;
        System.out.println("\nA repaired vehicle " + vehicleToAdd + " added to position " + (indexToAdd + 1));
        System.out.println(reusePool);
    }

    /**
     * Handles collisions between vehicles by determining their types and applying appropriate rules.
     * Calls corresponding methods according to the collision type
     *
     * @param target the target moving location
     */
    private void collision(int target) {
        Vehicle currentVehicle = Vehicles[current];
        Vehicle otherVehicle = Vehicles[target];

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
     * - Different colors: Both cars crash and are removed, a new car is added
     * - Same color: Car with higher horsepower survives
     *
     * @param target the target moving location
     */
    private void carVsCar(int target) {
        // Cast vehicles to Car for access to car-specific methods
        Car currentCar = (Car) Vehicles[current];
        Car otherCar = (Car) Vehicles[target];

        if (!currentCar.sameColor(otherCar)) {
            System.out.println("\n" + currentCar + " vs " + otherCar
                    + "\nCars of different colors crashed! Removing both.");
            // add crashed vehicles to reuse pool
            reusePool.recycleVehicle(currentCar);
            reusePool.recycleVehicle(otherCar);
            System.out.print(reusePool);

            Vehicles[current] = null;
            Vehicles[target] = null;
            numVehicles -= 2;
            addVehicle();// Add a replacement car at a random position
        } else {
            if (currentCar.getHorsePower() > otherCar.getHorsePower()) {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        getPosition() + " has greater HP, Car at position " + (target + 1) + " is removed.");
                reusePool.recycleVehicle(otherCar);
                Vehicles[target] = null;
            } else {
                System.out.println("\n" + currentCar + " vs " + otherCar + "\nCar at position " +
                        (target +1) + " has greater HP, Car at position " + getPosition() + " is removed.");
                reusePool.recycleVehicle(currentCar);
                Vehicles[current] = null;
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
     * @param target position of the bus
     * @param type true if the car is moving into a bus, false if a bus is moving into a car
     */
    private void carVsBus(int target, boolean type) {
        if (type) {
            // Car tries to move into Bus's space, the car stops
            System.out.println("\n" + Vehicles[current] + " vs " + Vehicles[target] + "\nCar at position "
                    + getPosition() + " stops. Cannot move into Bus's space.");
        } else {
            // Bus moves into Car's space, Bus pushes the Car out
            System.out.println("\n" + Vehicles[current] + " vs " + Vehicles[target] + "\nBus at position "
                    + getPosition() + " pushes Car at position " + (target + 1) + " out.");

            reusePool.recycleVehicle(Vehicles[target]);
            System.out.print(reusePool);

            // Move bus to the car's location, the car is removed
            Vehicles[target] = Vehicles[current];
            Vehicles[current] = null;
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
     * @param target position of the first bus
     */
    private void busVsBus(int target) {
        // Cast vehicles to Bus for access to bus-specific methods
        Bus currentBus = (Bus) Vehicles[current];
        Bus otherBus = (Bus) Vehicles[target];

        if (currentBus.getWeight() > otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + getPosition() + " has more weight, Bus at position " + (target + 1) + " is removed.");
            reusePool.recycleVehicle(otherBus);
            System.out.print(reusePool);

            Vehicles[target] = null;
            numVehicles--;
        } else if (currentBus.getWeight() < otherBus.getWeight()) {
            System.out.println("\n" + currentBus + " vs " + otherBus + "\nBus at position "
                    + (target +1) + " has more weight, Bus at position " + getPosition() + " is removed.");
            reusePool.recycleVehicle(currentBus);
            System.out.print(reusePool);

            Vehicles[current] = null;
            numVehicles--;
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

        for (int i = 0; i < numVehicles; i++) {
            int index = rand.nextInt(Vehicles.length); // generate random index to assign a vehicle

            while (Vehicles[index] != null) {
                index = new Random().nextInt(Vehicles.length);
            }

            double type = new Random().nextDouble(1.0); // generate 0 or 1 to randomly choose Car (0) or Bus (1)
            if (type < 0.65) {
                Vehicles[index] = new Car();
            } else {
                Vehicles[index] = new Bus();
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
        for (int i = 0; i < Vehicles.length; i++) {
            if (Vehicles[i] != null) {
                str.append("Position ").append(i + 1).append(": ").
                        append(Vehicles[i].toString()).append("\n");
            } else {
                str.append("Position ").append(i + 1).append(": [Empty]\n");
            }
        }
        return str.toString();
    }
}