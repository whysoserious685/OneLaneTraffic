package onelanetraffic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * The MainApp class is where the simulation runs for the one-lane traffic project.
 * This program prompts the user for input of road size and the number
 * of vehicles on the road. It uses commands from an input file to simulate
 * vehicle movements and allows undo operations to restore the road to previous states.
 * It also handles invalid inputs and exceptions along the simulation process.
 */
public class MainApp {
    public static void main (String[] args) {
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
            // reads inputs from the file
            action = scr.next();
            step =scr.nextInt();
            aRoad.setCurrent(); // set a current vehicle to perform movement
            int targetPosition = aRoad.getPosition() + step; // 1-based index of the target position

            if (action.equals("m")) {
                // validate movement command
                if ( !(step == 0 || step == 1 || step == -1) ) {
                    System.out.println("Invalid direction. Proceeding to the next command.");
                    continue;
                }
                System.out.println("Executing command - moving vehicle at position " + aRoad.getPosition()
                        + " to position " + targetPosition + ".");
                roadHistory.addHistory(aRoad); // add a copy of the current road state to history
                aRoad.moveVehicle(step);
            } else if (action.equals("u")) {
                System.out.println("Executing command - restoring the road to its state " + step + " steps ago.");
                // restore the current road to its earlier state, loop ends if no more history
                for (int i = 0; i < step; i++) {
                    Road prev = roadHistory.undo();
                    if (prev == null) break;
                    aRoad = prev; // reassign aRoad to its previous state
                }
            } else {
                System.out.println("Invalid command. Proceeding to the next step.\n");
                scr.nextLine(); // ignores and consumes the invalid step number and move to the next line
                continue;
            }
            System.out.println("\nCurrent state of the road:\n" + aRoad);
        }

        System.out.println("Simulation finished.");
        System.out.println("\nFinal state of the road:\n" + aRoad);
    }
}