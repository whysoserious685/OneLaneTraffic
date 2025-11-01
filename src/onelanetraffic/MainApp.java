package onelanetraffic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * <p>Title: MainApp</p>
 *
 * <p>Description: Main simulation class for one-lane traffic on a road.
 * Reads movement instructions from a file and applies them to vehicles on the road.
 * As the simulation runs, vehicle movements, collision logic, recycling crashed vehicles and
 * retrieve history road state functions are being tested.<p>
 *
 * Edge cases are being handled by exceptions and conditional statements.</>
 *
 * @author Lehan Zhang N00896908
 */
public class MainApp {
    public static void main(String[] args) {
        int size = 0; // size of the road
        int numVehicles = 0; // num of vehicles to place on the road
        boolean valid = false; // control variable for the validation loop

        // loop the prompt message until a valid input is entered
        while (!valid) {
            String length = JOptionPane.showInputDialog("Please enter the size of the road(positive integer): ");

            // program terminates if user cancels or ends the dialog
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

        Road aRoad = new Road(size, numVehicles); // create a new road object with valid user inputs
        HistoryTracking roadHistory = new HistoryTracking(); // an linkedStack to store history state of the road
        String action; // store string character that indicates a command type
        int step; // store the steps of the specified command
        Scanner scr; // scanner to scan and read input from an input file
        String fileName = "input.txt";  // name of the input file

        System.out.println("Starting one-lane traffic simulation...\n");

        try {
            scr = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            System.out.println("No file was found with the name " + "\"" + fileName + "\"");
            System.out.println("Program terminated");
            return;
        }
        System.out.println("Initial state of the road:\n" + aRoad);

        while (scr.hasNext()) {
            action = scr.next();
            step =scr.nextInt();
            aRoad.setCurrent(); // pick a location to perform movement
            int targetPosition = aRoad.getPosition() + step;

            // System.out.println(aRoad.getCurrent() + "\n");

            if (action.equals("m")) {
                if ( !(step == 0 || step == 1 || step == -1) ) {
                    System.out.println("Invalid direction. Proceeding to the next command.");
                    continue;
                }
                System.out.println("Executing command - moving vehicle at position " + aRoad.getPosition()
                        + " to position " + targetPosition + ".");
                roadHistory.addHistory(aRoad); // add a copy of the current road to history
                aRoad.moveVehicle(step);
            } else if (action.equals("u")) {
                System.out.println("Executing command - restoring the road to its state " + step + " steps ago.");
                for (int i = 0; i < step; i++) {
                    aRoad = roadHistory.undo(); // reassign aRoad to its previous state
                }
            } else {
                System.out.println("Invalid command. Proceeding to the next step.");
                scr.nextLine(); // ignores and consumes the invalid step number and move to the next line
                continue;
            }
            System.out.println("\nCurrent state of the road:\n" + aRoad);
        }
    }
}