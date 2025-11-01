package onelanetraffic;

import java.util.Random;
/**
 * Represents a car in the traffic simulation.
 * Car has a color and horsepower.
 * This class inherits Vehicle.
 * 
 * @author Lehan Zhang
 */
public class Car extends Vehicle {
    private final String color; // color property of cars

    /**
     * Constructs a new Car with random color and horsepower.
     * Color is randomly selected.
     */
    public Car() {
        super(); //initialize horsepower and position in Vehicle
        this.color = randomColor() ; // assign a random color
    }

    /**
     * Generates a random color for the car from predefined options.
     * 
     * @return a randomly selected color string
     */
    public String randomColor() {
        String[] colors = {"red", "blue", "White", "black", "silver"};
        Random rand = new Random();
        return colors[rand.nextInt(colors.length)];
    }

    /**
     * Compares the color of this car with another car.
     * 
     * @param otherCar the car to compare colors with
     * @return true if both cars have the same color, false if not
     */
    public boolean sameColor(Car otherCar) {
        return this.color.equals(otherCar.color);
    }

    /**
     * Returns a string representation of this car including its properties.
     * 
     * @return a string with color and horsepower
     */
    @Override
    public String toString() {
        return "Car (Color: " + color + ", " + getHorsePower() + " hp)";
    }
}