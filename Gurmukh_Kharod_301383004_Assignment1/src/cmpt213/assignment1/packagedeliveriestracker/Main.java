package cmpt213.assignment1.packagedeliveriestracker;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Main.java
 * Gurmukh Kharod
 * 301 383 004
 *
 * This is the driver class for this program,
 * At the start of runtime, an arrayList, a PackageInformation object, and a PackageMenu object are created.
 * All the existing packages from the file are added to the arrayList should the file exist.
 * An infinite while loop shows up displaying the menu continuously, until userChoice == 7.
 * At program termination, the final arrayList containing all packages will be written to the file.
 * Any existing packages in the file will be overwritten, but the original packages will already be saved, so they are not lost.
 *
 * The following documentation was used as reference when completing this program:
 * (These documentations contains useful information about methods like .addAll() for ArrayList)
 *
 * 1.   class lecture slides from Week 1 - 3, and Gson practice Application video from class
 * 2.   https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
 * 3.   https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
 * 4.   https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
 * 5.   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
 *
 */
public class Main {

    /**
     * The main method contains all the driver code for this program.
     * For more details about each option and their operations,
     * refer to the pseudocode found at the top of each method in PackageMenu.java.
     * @param args
     */
    public static void main(String[] args) {

        //our main package list used throughout runtime, and added to the file at the end of runtime.
        ArrayList<PackageInformation> packageListInMain = new ArrayList<>();;

        //newPackage is used to add new packages to the array list
        PackageInformation newPackage = new PackageInformation();

        //package driver is used to run the methods of PackageMenu, based on user choice.
        PackageMenu packageDriver = new PackageMenu();

        //immediately when the app starts, open the file and get all the packages from it, to add to the start of the list.
        packageListInMain.addAll(packageDriver.getGetPackageListInMenu());

        //we are getting the choice from userInputReader() continuously.
        //at the end, the array list of objects is added to the file, userChoice == 7
        while(true){

            packageDriver.printMenu();
            int userChoice = packageDriver.userInputReader();

            //exit the program, this will break out of the infinite while loop
            if(userChoice == 7) {
                packageDriver.exitProgram(packageListInMain);
                break;
            }

            switch(userChoice) {
                case 1:
                    //list all packages in the file
                    packageDriver.listAllPackages(packageListInMain);
                    break;
                case 2:
                    //add a new package
                    newPackage = packageDriver.addPackage(newPackage);
                    packageListInMain.add(newPackage);
                    break;
                case 3:
                    //remove an existing package. Get the user choice from PackageMenu, then handle deletion here.
                    int userChoiceToRemove = Integer.parseInt(packageDriver.removePackage(packageListInMain));
                    if (userChoiceToRemove >= 1 && userChoiceToRemove <= (packageListInMain.size())) {
                        String nameOfPackageToRemove; //save the name, for the end of this operation
                        userChoiceToRemove -= 1; //to match with array list index
                        nameOfPackageToRemove = packageListInMain.get(userChoiceToRemove).getName();
                        packageListInMain.remove(userChoiceToRemove);
                        System.out.println(nameOfPackageToRemove + " has been removed from the list.");
                    }
                    break;
                case 4:
                    //list all undelivered packages - with expected delivery date before the current date and time.
                    packageDriver.listOverduePackages(packageListInMain);
                    //Collections.sort(packageListInMain, Comparator.comparing(PackageInformation::getExpectedDeliveryDate);
                    break;
                case 5:
                    //List all undelivered packages - with expected delivery date on or after the current date & time
                    packageDriver.listUpcomingPackages(packageListInMain);
                    break;
                case 6:
                    //deliver - similar to remove a package, Get user choice and perform handling here.
                    int userInputToSetDelivered = Integer.parseInt(packageDriver.markPackageAsDelivered(packageListInMain));
                    if (userInputToSetDelivered >= 1 && userInputToSetDelivered <= (packageListInMain.size() + 1)) {
                        String nameOfPackageToSetDelivered; //save the name, for the end of this operation
                        userInputToSetDelivered -= 1; //to match with array list index
                        nameOfPackageToSetDelivered = packageListInMain.get(userInputToSetDelivered).getName();
                        packageListInMain.get(userInputToSetDelivered).setDelivered(true);
                        System.out.println(nameOfPackageToSetDelivered + " has been delivered.");
                    }
                    break;
                default:
                    System.out.println("Invalid selection. Enter a number between 1 and 7.");
                    break;

            }
        }
    }
}