package onelanetraffic;

import exceptionclasses.EmptyQueueException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.EmptyStackException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * <p>Title: One-lane Traffic MainApp</p>
 *
 * <> Description: Main simulation class for the one-lane traffic.
 * Reads movement instructions from a file and applies them to vehicles on the road.
 * </>
 *
 * @author Lehan Zhang N00896908
 */
public class MainApp {
    public static void main(String[] args) throws FileNotFoundException {
        int size = 0; // size of the road
        int numVehicles = 0; // num of vehicles to place on the road
        boolean valid = false;

        while (!valid) {
            String length = JOptionPane.showInputDialog("Please enter the size of the road(positive integer): ");
            if (length == null) System.exit(0);
            try {
                size = Integer.parseInt(length);
                if (size <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a positive integer");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }

        valid = false;
        while (!valid) {
            String count = JOptionPane.showInputDialog("Please enter the number of vehicles the road" + "(positive integer <= road size):");
            if (count == null) System.exit(0);
            try {
                numVehicles = Integer.parseInt(count);
                if (numVehicles <= 0 || numVehicles > size) {
                    JOptionPane.showMessageDialog(null, "Total number of vehicles need to be less than road size");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }

        Road aRoad = new Road(size, numVehicles); // create a new road object with valid inputs
        HistoryTracking roadHistory = new HistoryTracking(); // an linkedStack to store history state of the road

        String action; // store the input command
        int step; // store the steps of command
        String fileName = "input.txt";
        Scanner fileScan = new Scanner(new File(fileName)); // scanner to read file inputs
        System.out.println("Initial state of the road:\n" + aRoad);

        while (fileScan.hasNext()) {
            action = fileScan.next();
            step =fileScan.nextInt();

            System.out.println(aRoad.getCurrent() + "\n");

            if (action.equals("m")) {
                System.out.println("Executing command - moving vehicle at position " + aRoad.getPosition()
                        + " to position " + (aRoad.getPosition() + 1) + ".");
                roadHistory.addHistory(aRoad);
                aRoad.moveVehicle(step);
            } else if (action.equals("u")) {
                System.out.println("Executing command - restoring the road to its state " + step + " steps ago.");
                roadHistory.undo(step);
            } else {
                System.out.println("Invalid command.");
            }
            System.out.println("\nCurrent state of the road:\n" + aRoad);
        }
    }
}