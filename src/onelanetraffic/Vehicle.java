package onelanetraffic;

import java.util.Random;

/**
 * A super class representing vehicles
 * All types of vehicles should have horsepower
 * 
 * @author Lehan Zhang
 */
public class Vehicle {
    private final int horsePower; // The horsepower of the vehicle

    /**
     * Constructs a new Vehicle with random horsepower which
     * is randomly generated between 100 and 399.
     */
    public Vehicle() {
        Random rand = new Random();
        this.horsePower = rand.nextInt(300) + 100;  // Random horsepower between 100-399
    }

    /**
     * Gets the horsepower
     * 
     * @return the horsepower value
     */
    public int getHorsePower() {
        return horsePower;
    }

    /**
     * Checks if this vehicle is a car.
     * 
     * @return true if this vehicle is a car object, false if not.
     */
    public boolean isCar() {
        return this instanceof Car;
    }

    /**
     * Checks if this vehicle is a bus.
     * 
     * @return true if this vehicle is a bus object, false if not.
     */
    public boolean isBus() {
        return this instanceof Bus;
    }

    /**
     *
     * @return the type of the vehicle with its horsepower
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + horsePower + " hp)";
    }
}