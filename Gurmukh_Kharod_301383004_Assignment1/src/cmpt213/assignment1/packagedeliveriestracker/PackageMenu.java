package cmpt213.assignment1.packagedeliveriestracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  PackageMenu.java
 *  Gurmukh Kharod
 *  301 383 004
 *
 *  This class allows users to choose menu options from 1-7,
 *  with a unique method for each option.
 *  Additional methods have also been added in for greater modularity.
 *
 *  Methods:
 *  printMenu(), userInputReader(),
 *  listAllPackages(), addPackage(), removePackage(),
 *  listOverduePackages(), listUpcomingPackages(),
 *  markPackageAsDelivered(), exitProgram(),
 *  getGetPackageListInMenu()
 *
 * Note that pseudocode has been added in at the top of most method declarations in javadoc comment format.
 *
 */
public class PackageMenu {

    private String title = "Gurmukh's Package Delivery Tracker";
    private String[] menuOptions = {
            "List all packages",
            "Add a package",
            "Remove a package",
            "List overdue packages",
            "List upcoming packages",
            "Mark package as delivered",
            "Exit"
    };

    //used for user input within userInputReader() method
    Scanner console = new Scanner(System.in);

    //Array list must be of type PackageInformation, to get access to objects
    private static ArrayList<PackageInformation> packageListInMenu = new ArrayList<>();

    //menu versions of the fields found in PackageInfo.java
    private String nameInMenu = "";
    private String notesInMenu;
    private double priceInDollarInMenu;
    private double weightInKgInMenu;
    private boolean deliveredInMenu;
    private LocalDateTime expectedDeliveryDate = LocalDateTime.now();

    /**
     * printMenu() method prints the title of the program, followed by the menuOptions array.
     * Various for loops have been used to allow for automatic printing based on array length,
     * rather than hard coding to the console.
     */
    public void printMenu() {

        System.out.println();
        //automatically print out the correct number of #'s based on length of title
        for(int i = 0; i < title.length() + 4; i++){
            System.out.print("#");
        }
        System.out.println();
        System.out.print("# " + title + " #");
        System.out.println();
        for(int i = 0; i < title.length() + 4; i++){
            System.out.print("#");
        }
        System.out.println();

        //print current date
        System.out.println("Today is: " + LocalDate.now());

        //Automatically numbers the possible options, starting from 1
        for (int i = 0; i < menuOptions.length; i++){
            System.out.println( i + 1 + ": " + menuOptions[i] );
        }

    }

    /**
     * Returns all from the file at the start of the app, should a file exist.
     * @return packageListInMenu.
     */
    public ArrayList<PackageInformation> getGetPackageListInMenu() {
        File packageListFile = new File("./packageListInMenu.json");
        if(packageListFile.exists()){
            return readFromFile();
        } else {
            System.out.println("");
        }
        return packageListInMenu;
    }

    /**
     * userInputReader() method contains a while loop to
     * continuously ask the user to enter a number between 1-7.
     * An if statement was added to break out of the while loop upon entering 7.
     * A switch statement was added to separate the different options.
     * Within each case, an int is returned to main.
     * There is a separate method for each option to allow greater modularity within this class.
     *
     */
    public int userInputReader() {

        while(true){

            try {
                int userChoice = Integer.parseInt(console.nextLine());

                if(userChoice == 7){
                    return 7;
                }

                switch(userChoice) {
                    case 1:
                        return 1;

                    case 2:
                        return 2;

                    case 3:
                        return 3;

                    case 4:
                        return 4;

                    case 5:
                        return 5;

                    case 6:
                        return 6;

                    default:
                        System.out.println("Invalid selection. Please enter a number between 1 and 7:");
                        break;

                }
            } catch (NumberFormatException e) {
                System.out.println("This input is not an integer between 1 and 7. Please try again:");
            }
        }
    }


    /**
     * This method will take the packageListInMain, and simply print all the packages.
     * An int packageNumber is used to automatically print the number of the package.
     * @param packageListInMain
     */
    public void listAllPackages(ArrayList<PackageInformation> packageListInMain) {
        if(!packageListInMain.isEmpty()) {
            int packageNumber = 1;
            for (PackageInformation p : packageListInMain) {
                System.out.print("Package #" + packageNumber++);
                System.out.println(p.toString());
                System.out.println();
            }
        } else {
            System.out.println("No packages to show.");
        }

    }

    /**
     *
     * @param packInfo
     * @return packInfo
     *
     * This method performs the following steps:
     *
     *      1. create a new object and just return the object.
     *      2. ask for user entry, data validation via while loops, and if statements
     *      3. create an object, it will be added to the array list in the main method
     *      4. Print that the "(object package name) has been added to the list."
     *      5. return the new object
     *      6. this new object will be sent back to the main method to add to the array list
     *      7. then the array list is sent to the file, via exitProgram() option 7
     *
     */
    public PackageInformation addPackage(PackageInformation packInfo) {

        System.out.println("addPackage");
        while(true){
            System.out.println("Enter the name of the package: ");
            nameInMenu = console.nextLine();
            if (nameInMenu.isBlank()) {
                System.out.println("Incorrect entry. Name is empty.");
            }
            else{
                break;
            }
        }
        System.out.println("Enter the notes of the package: (The notes may be empty)");
        notesInMenu = console.nextLine();
        System.out.println("Enter the price of the package (in dollar): ");
        priceInDollarInMenu =  Double.parseDouble(console.nextLine());
        System.out.println("Enter the weight of the package (in kg): ");
        weightInKgInMenu = Double.parseDouble(console.nextLine());

        String expectedDeliveryDateString = "";

        while(true){
            System.out.println("Enter the year of the expected delivery date (0001-9999): ");
            String yearOfDelivery = console.nextLine();
            if (Integer.parseInt(yearOfDelivery) < 1 || Integer.parseInt(yearOfDelivery) > 9999 || yearOfDelivery.length() != 4) {
                System.out.println("Incorrect entry. Must be between 0001 and 9999. Note that 1 -> 0001, 2 -> 0002, etc.");
            }
            else{
                expectedDeliveryDateString += yearOfDelivery;
                expectedDeliveryDateString += "-";
                break;
            }
        }

        while(true){
            System.out.println("Enter the month of the expected delivery date (01-12): ");
            String monthOfDelivery = console.nextLine();
            if (Integer.parseInt(monthOfDelivery) < 1 || Integer.parseInt(monthOfDelivery) > 12 || monthOfDelivery.length() != 2) {
                System.out.println("Incorrect entry. Must be between 01 and 12. Note that 1 -> 01, 2 -> 02, etc.");
            }
            else{
                expectedDeliveryDateString += monthOfDelivery;
                expectedDeliveryDateString += "-";
                break;
            }
        }

        while(true){
            System.out.println("Enter the day of the expected delivery date (01-28/29/30/31): ");
            String dayOfDelivery = console.nextLine();
            if (Integer.parseInt(dayOfDelivery) < 1 || Integer.parseInt(dayOfDelivery) > 31 || dayOfDelivery.length() != 2) {
                System.out.println("Incorrect entry. Must be between 01 and 28/29/30/31. Note that 1 -> 01, 2 -> 02, etc.");
            }
            else{
                expectedDeliveryDateString += dayOfDelivery;
                expectedDeliveryDateString += " ";
                break;
            }
        }


        while(true){
            System.out.println("Enter the hour of the expected delivery date (00-23): ");
            String hourOfDelivery = console.nextLine();
            if (Integer.parseInt(hourOfDelivery) < 0 || Integer.parseInt(hourOfDelivery) > 23 || hourOfDelivery.length() != 2) {
                System.out.println("Incorrect entry. Must be between (00-23). Note that 0 -> 00, 1 -> 01, 2 -> 02, etc.");
            }
            else{
                expectedDeliveryDateString += hourOfDelivery;
                expectedDeliveryDateString += ":";
                break;
            }
        }

        while(true){
            System.out.println("Enter the minute of the expected delivery date (00-59): ");
            String minuteOfDelivery = console.nextLine();
            if (Integer.parseInt(minuteOfDelivery) < 0 || Integer.parseInt(minuteOfDelivery) > 59 || minuteOfDelivery.length() != 2) {
                System.out.println("Incorrect entry. Must be between (00-59). Note that 0 -> 00, 1 -> 01, 2 -> 02, etc.");
            }
            else{
                expectedDeliveryDateString += minuteOfDelivery;
                break;
            }
        }

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        expectedDeliveryDate = LocalDateTime.parse(expectedDeliveryDateString, aFormatter);

        packInfo = new PackageInformation(nameInMenu, notesInMenu,
                priceInDollarInMenu, weightInKgInMenu, false, expectedDeliveryDate);

        System.out.println(nameInMenu + " has been added to the list.");

        return packInfo;

    }

    /**
     *
     * @param packageListInMain
     * @return String representing user choice
     *
     * This method performs the following steps:
     *
     *         1. get the packageListInMain
     *         2. list all the packages, sending over packageListInMain as an argument
     *         3. If packageListInMain is not empty:
     *         4.      While True -> ask to "Enter the item number you want to remove (0 to cancel):"
     *         5.           If enters 0, cancel (break) and give message.
     *         6.           Else If the array has packages:
     *         7.                  If user enters number that does not exist in array ( < 1 or > array.length) ask for re-entry
     *         8.                  Else break;
     *         7.           Else correct input, then return the input. Note that we return the array index + 1, as that is how the packages are numbered.
     *         9. Else return 0 and exit method
     *
     *  When in Main.java, we can decrement the user input to get to the right array index in packageListInMain
     */
    public String removePackage(ArrayList<PackageInformation> packageListInMain) {

        String userInputToRemovePackage = "";
        listAllPackages(packageListInMain);

        if (packageListInMain.size() > 0) {
            while(true){
                System.out.println("Enter the item number you want to remove (0 to cancel):");
                userInputToRemovePackage = console.nextLine();
                if (Integer.parseInt(userInputToRemovePackage) == 0) {
                    System.out.println("0 has been inputted, and therefore removing a package has been cancelled.");
                    break;
                } else if (packageListInMain.size() > 0){
                    if(Integer.parseInt(userInputToRemovePackage) < 1 || Integer.parseInt(userInputToRemovePackage) > packageListInMain.size()) {
                        System.out.println("Incorrect entry. Must be between 1 and " + packageListInMain.size() + ".");
                    } else {
                        break;
                    }
                } else {
                    return userInputToRemovePackage;
                }
            }
        } else {
            return "0";
        }

        return userInputToRemovePackage;

    }


    /**
     *
     * @param packageListInMain
     *
     * This method performs the following steps:
     *
     * 1. get the package list in main
     * 2. store a bool overduePackageExists in case of no packages that satisfy the method conditions
     * 3. keep an int for listing the packages automatically
     * 4. If the packageListInMain is empty:
     * 5.     Print "There are no packages."
     * 6. Else :
     * 7.     for each package in packageListInMain:
     * 8.           if the package is not delivered and the package's delivery date is before .now():
     * 9.               print the package.
     * 10.    If no packages satisfy the conditions, print "There are no overdue packages."
     */
    public void listOverduePackages(ArrayList<PackageInformation> packageListInMain) {
        boolean overduePackageExists = false;
        int packageNumber = 1;

        if(packageListInMain.isEmpty()){
            System.out.println("There are no packages.");
        } else {
            for(PackageInformation p : packageListInMain){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is before .now(), print the package.
                if(!p.isDelivered() && now.isAfter(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    overduePackageExists = true;
                }
            }
            if(!overduePackageExists){
                System.out.println("There are no overdue packages.");
            }
        }
    }


    /**
     *
     * @param packageListInMain
     *
     * This method performs the following steps:
     *
     * 1. get the package list in main
     * 2. store a bool upcomingPackageExists in case of no packages that satisfy the method conditions
     * 3. keep an int for listing the packages automatically
     * 4. If the packageListInMain is empty:
     * 5.     Print "There are no packages."
     * 6. Else :
     * 7.     for each package in packageListInMain:
     * 8.           if the package is not delivered and the package's delivery date is after .now():
     * 9.               print the package.
     * 10.    If no packages satisfy the conditions, print "There are no upcoming packages."
     */
    public void listUpcomingPackages(ArrayList<PackageInformation> packageListInMain) {
        boolean upcomingPackageExists = false;
        int packageNumber = 1;

        if(packageListInMain.isEmpty()){
            System.out.println("There are no packages.");
        } else {
            for(PackageInformation p : packageListInMain){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is after .now(), print the package.
                if(!p.isDelivered() && now.isBefore(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    upcomingPackageExists = true;
                }
            }
            if(!upcomingPackageExists){
                System.out.println("There are no upcoming packages.");
            }
        }
    }

    /**
     *
     * @param packageListInMain
     * @return String representing user choice
     *
     * This method performs the following steps:
     *
     *         1. get the packageListInMain
     *         2. If no packages, print "There are no packages."
     *         3. Else: list all the packages if they have not been delivered, and automatically number them.
     *         3.   If a delivered package exists:
     *         4.      While True -> ask to "Enter the item number you want to mark as Delivered (0 to cancel): "
     *         5.           If enters 0, cancel (break) and give message.
     *         6.           Else If the array has packages:
     *         7.                  If user enters number that does not exist in array ( < 1 or > array.length) ask for re-entry
     *         8.                  Else break;
     *         9.           Else correct input, then return the input. Note that we return the array index + 1, as that is how the packages are numbered.
     *        10.   Else: print "No undelivered packages to show."
     *
     */
    public String markPackageAsDelivered(ArrayList<PackageInformation> packageListInMain) {

        String userInputToSetDeliveredPackage = "0";
        boolean deliveredPackageExists = false;
        int packageNumber = 1;

        if(packageListInMain.isEmpty()){
            System.out.println("There are no packages.");
            return "0";
        } else {
            for(PackageInformation p : packageListInMain){
                if(!p.isDelivered()){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    deliveredPackageExists = true;
                }
            }

            if(deliveredPackageExists){
                while (true) {
                    System.out.println("Enter the item number you want to mark as Delivered (0 to cancel): ");
                    userInputToSetDeliveredPackage = console.nextLine();
                    if (Integer.parseInt(userInputToSetDeliveredPackage) == 0) {
                        System.out.println("0 has been inputted, and therefore setting to delivered has been cancelled.");
                        break;
                    } else if (packageListInMain.size() > 0) {
                        if (Integer.parseInt(userInputToSetDeliveredPackage) < 1 || Integer.parseInt(userInputToSetDeliveredPackage) > packageListInMain.size()) {
                            System.out.println("Incorrect entry. Must be between 1 and " + packageListInMain.size() + ".");
                        } else {
                            break;
                        }
                    } else {
                        return userInputToSetDeliveredPackage;
                    }
                }
            } else {
                System.out.println("No undelivered packages to show.");
            }
        }

        return userInputToSetDeliveredPackage;
    }

    /**
     * The exitProgram() method will simply take the packageListInMain, and write it to the file.
     * Note that the packageListInMain also contains all the packages from the file, should they exist.
     * Using this way, will overwrite all the existing packages in the file,
     * but we do not lose any packages already created, as they were already added at the beginning of the file.
     * @param packageListInMain
     */
    public void exitProgram(ArrayList<PackageInformation> packageListInMain){
        System.out.println("Thank you for using Gurmukh's Package Delivery Tracker.");
        writeToFile(packageListInMain);

    }

    /**
     * readFromFile() will open the file, if it exists, and put all the values into packages.
     * These packages will be added to the array list, which will be returned to the Main.
     */
    public ArrayList<PackageInformation> readFromFile() {
        //read from file, print content
        try {
            File packageListFile = new File("./packageListInMenu.json");
            FileReader fileReader = new FileReader(packageListFile);
            Type type = new TypeToken<ArrayList<PackageInformation>>() {
            }.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    new TypeAdapter<LocalDateTime>() {
                        @Override
                        public void write(JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
                            jsonWriter.value(localDate.toString());
                        }

                        @Override
                        public LocalDateTime read(JsonReader jsonReader) throws IOException {
                            return LocalDateTime.parse(jsonReader.nextString());
                        }
                    }).setPrettyPrinting().create();

            packageListInMenu = gson.fromJson(fileReader, type); //reads file using file reader of type array list
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.err.println("Error in creating a file reader object.");
        } catch (IOException e) {
            System.err.println("Error in closing the file.");
        }

        return packageListInMenu;

    }

    /**
     *
     * @param packageListInMain
     *
     * writeToFile() will add all the existing packages to the file.
     * All the packages currently in te file will be overwritten.
     * These packages will not be lost however, as they were added to packageListInMain at runtime.
     */
    public void writeToFile(ArrayList<PackageInformation> packageListInMain){
        //Take the arrayList from the Main.java and write to File using it.
        //This array contains all the packages already in the file, should they exist.
        try {
            File packageListFile = new File("./packageListInMenu.json");
            FileWriter fileWriter = new FileWriter(packageListFile);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    new TypeAdapter<LocalDateTime>() {
                        @Override
                        public void write(JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
                            jsonWriter.value(localDate.toString());
                        }
                        @Override
                        public LocalDateTime read(JsonReader jsonReader) throws IOException {
                            return LocalDateTime.parse(jsonReader.nextString());
                        }
                    }).setPrettyPrinting().create();
            gson.toJson(packageListInMain, fileWriter); //writes theList to file
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error in writing the file.");
        }
    }


}
