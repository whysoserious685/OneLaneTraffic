package onelanetraffic;

import java.util.Random;

/**
 * Represents a Bus in the traffic simulation.
 * Constructs a new Bus with random weight and horsepower.
 * This class inherits Vehicle
 *
 * @author Lehan Zhang
 */
public class Bus extends Vehicle {
    private final int weight; // weight of the bus in pounds

    /**
     * Constructs a new Bus with random weight and horsepower.
     * Weight is randomly selected from 15000-40000.
     */
    public Bus() {
        super(); // Initialize horsepower and position in Vehicle
        Random rand = new Random();
        this.weight = rand.nextInt(15000, 40001);
    }

    /**
     * Gets the weight of this bus.
     * 
     * @return the bus weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns a string representation of this bus including its properties.
     * 
     * @return a string with weight and horsepower
     */
    @Override
    public String toString() {
        return "Bus (Weight: " + weight + " kg, " + getHorsePower() + " hp)";
    }


}