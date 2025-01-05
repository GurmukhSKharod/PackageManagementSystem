package packagedeliveries.client.control;

import packagedeliveries.client.gson.extras.RuntimeTypeAdapterFactory;
import packagedeliveries.client.model.*;
import packagedeliveries.client.model.Book;
import packagedeliveries.client.model.Electronic;
import packagedeliveries.client.model.Package;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import packagedeliveries.client.model.Perishable;


import java.io.*;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * PackageDeliveriesTrackerControl.java
 * Gurmukh Kharod
 *
 * This class has the following features added:
 * Using various methods, this class now calls HTTP GET/POST requests to perform the different options.
 * The endpoint methods are found in the server side.
 *
 * The following methods have been added:
 * requestAllPackages(), exitProgramHTTP(), requestOverduePackages(),
 * requestUpcomingPackages(), addPackageToServer(), removePackageFromServer(),
 * setPackageToDeliveredFromServer(), addSamplePackagesToServer()
 *
 * Each of these methods represents the seven options we have for our program.
 * Note that the read/write part of this program is now done on the server side.
 * However, we are still using gson to parse the GET response when receiving items from the server.
 *
 *
 */

public class PackageDeliveriesTrackerControl {

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

    //Array list must be of type Package, to get access to objects
    private static ArrayList<Package> packageListInMenu = new ArrayList<>();

    //menu versions of the fields found in PackageInfo.java
    private String nameInMenu = "";
    private String notesInMenu;
    private double priceInDollarInMenu;
    private double weightInKgInMenu;
    private boolean deliveredInMenu;
    private LocalDateTime expectedDeliveryDate = LocalDateTime.now();

    //package type: 1 - Book, 2 - Perishable, 3 - Electronic
    private int packageType;

    private String authorName; //used for Book
    private LocalDateTime expiryDate; //used for Perishable
    private double environmentalHandlingFee; //used for Electronic





    /**
     * In the case that the POST request does not go through for addPackageToServer(),
     * we can call this method, containing a GET request to add some sample packages to the server.
     * These sample packages have been specifically made so that there are one of each type, and the dates are different.
     * That way we can see them ordered properly, and we can also view the upcoming/overdue properly.
     *
     * Additionally, we can click deliver or the remove package button to see how the server changes.
     */
    public void addSamplePackagesToServer() throws IOException {
        String commandToRequestAllPackages =
                "curl -X GET localhost:8080/addSamplePackages";
        ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToRequestAllPackages.split(" "));
        Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
    }


    /**
     * This method uses a String curl command to get all the packages from the server.
     * Then we will use a Buffer to read the string output and use gson to convert the string output into json.
     * Finally we will print this out to the console, and add the json objects to the array list in menu.
     * This array list will be referred to in the gui to be printed out in the JScrollPane.
     *
     * exceptions are thrown for process,process builder, and http response for GET
     * @throws IOException
     * @throws InterruptedException
     */
    public void requestAllPackages() throws IOException, InterruptedException {
        //input stream read line, string curl command
        //input stream is large strng reresenting array list.
        //take in array list and print to gui
        String commandToRequestAllPackages =
                "curl -X GET localhost:8080/listAll";
        ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToRequestAllPackages.split(" "));
        Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(processToRequestAllPackages.getInputStream()));

        Type type = new TypeToken<ArrayList<Package>>() {
        }.getType();
        RuntimeTypeAdapterFactory<Package> packageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Package.class, "type")
                .registerSubtype(Book.class, "Book")
                .registerSubtype(Perishable.class, "Perishable")
                .registerSubtype(Electronic.class, "Electronic");
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
                }).registerTypeAdapterFactory(packageAdapterFactory).setPrettyPrinting().create();

        ArrayList<Package> list = new ArrayList<Package>();
        list.addAll(gson.fromJson(in, type));
        packageListInMenu.removeAll(packageListInMenu);
        packageListInMenu.addAll(list);
        Collections.sort(packageListInMenu);
        System.out.println(packageListInMenu);
        processToRequestAllPackages.destroy();
    }

    /**
     * This method will simply call a GET request to /exit for the server, which will save the package list to a json file.
     *
     * exception is thrown for process,process builder, and http response for GET
     * @throws IOException
     */
    public void exitProgramHTTP() throws IOException {
        String commandToRequestAllPackages =
                "curl -X GET localhost:8080/exit";
        ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToRequestAllPackages.split(" "));
        Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
        //processToRequestAllPackages.destroy();
    }


    /**
     * This method uses a String curl command to get overdue packages from the server.
     * Then we will use a Buffer to read the string output and use gson to convert the string output into json.
     * Finally we will print this out to the console, and add the json objects to the array list in menu.
     * This array list will be referred to in the gui to be printed out in the JScrollPane.
     *
     * exceptions are thrown for process,process builder, and http response for GET
     * @throws IOException
     * @throws InterruptedException
     */
    public void requestOverduePackages() throws IOException, InterruptedException {
        //input stream read line, string curl command
        //input stream is large strng reresenting array list.
        //take in array list and print to gui
        String commandToRequestAllPackages =
                "curl -X GET localhost:8080/listOverduePackage";
        ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToRequestAllPackages.split(" "));
        Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(processToRequestAllPackages.getInputStream()));

        Type type = new TypeToken<ArrayList<Package>>() {
        }.getType();
        RuntimeTypeAdapterFactory<Package> packageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Package.class, "type")
                .registerSubtype(Book.class, "Book")
                .registerSubtype(Perishable.class, "Perishable")
                .registerSubtype(Electronic.class, "Electronic");
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
                }).registerTypeAdapterFactory(packageAdapterFactory).setPrettyPrinting().create();

        ArrayList<Package> list = new ArrayList<Package>();
        list.addAll(gson.fromJson(in, type));
        packageListInMenu.removeAll(packageListInMenu);
        packageListInMenu.addAll(list);
        Collections.sort(packageListInMenu);
        System.out.println(packageListInMenu);
        processToRequestAllPackages.destroy();
    }

    /**
     * This method uses a String curl command to get upcoming packages from the server.
     * Then we will use a Buffer to read the string output and use gson to convert the string output into json.
     * Finally we will print this out to the console, and add the json objects to the array list in menu.
     * This array list will be referred to in the gui to be printed out in the JScrollPane.
     *
     * exceptions are thrown for process,process builder, and http response for GET
     * @throws IOException
     * @throws InterruptedException
     */
    public void requestUpcomingPackages() throws IOException, InterruptedException {
        //input stream read line, string curl command
        //input stream is large strng reresenting array list.
        //take in array list and print to gui
        String commandToRequestAllPackages =
                "curl -X GET localhost:8080/listUpcomingPackage";
        ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToRequestAllPackages.split(" "));
        Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(processToRequestAllPackages.getInputStream()));

        Type type = new TypeToken<ArrayList<Package>>() {
        }.getType();
        RuntimeTypeAdapterFactory<Package> packageAdapterFactory = RuntimeTypeAdapterFactory
                .of(Package.class, "type")
                .registerSubtype(Book.class, "Book")
                .registerSubtype(Perishable.class, "Perishable")
                .registerSubtype(Electronic.class, "Electronic");
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
                }).registerTypeAdapterFactory(packageAdapterFactory).setPrettyPrinting().create();

        ArrayList<Package> list = new ArrayList<Package>();
        list.addAll(gson.fromJson(in, type));
        packageListInMenu.removeAll(packageListInMenu);
        packageListInMenu.addAll(list);
        Collections.sort(packageListInMenu);
        System.out.println(packageListInMenu);
        processToRequestAllPackages.destroy();
    }

    /**
     * The following method will simply take in a package from the GUI, determine the type,
     * and call the corresponding CURL POST request in the server to add a new package.
     * @param p
     * @throws IOException
     * @throws InterruptedException
     */
    public void addPackageToServer(Package p) throws IOException, InterruptedException {

        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = p.getExpectedDeliveryDate().format(dateTimeFormatter);

       if(p instanceof Book){
            //must format the string like a proper http request for POST
            String commandToAddPackageBook = "curl -H \"Content-Type: application/json\" -X POST -d '{ "
                    + "\"name\":" + " \"" + p.getName() + "\"" + "," +
                    " \"notes\":" + " \"" + p.getNotes() + "\"" + "," +
                    " \"priceInDollar\":" + " " + p.getPriceInDollar() + "" + "," +
                    " \"weightInKg\":" + " " + p.getWeightInKg() + "" + "," +
                    " \"delivered\":" + " " + p.isDelivered() + "" + "," +
                    " \"expectedDeliveryDate\":" + " \"" + p.getExpectedDeliveryDate() + "\"" + "," +
                    " \"authorName\":" + " \"" + ((Book) p).getAuthorName() + "\""
                    + " }' localhost:8080/addBook";

            System.out.println(commandToAddPackageBook); //check what curl command looks like, to print it to the terminal if errors
            ProcessBuilder processBuilderToRequestAllPackagesBook = new ProcessBuilder(commandToAddPackageBook.split(" "));
            Process processToRequestAllPackagesBook = processBuilderToRequestAllPackagesBook.start();

        } else if (p instanceof Perishable){
            String formattedDateTimeExpiry = ((Perishable) p).getExpiryDate().format(dateTimeFormatter);

            String commandToAddPackagePerishable = "curl -H \"Content-Type: application/json\" -X POST -d  '{ "
                    + "\"name\":" + " \"" + p.getName() + "\"" + "," +
                    " \"notes\":" + " \"" + p.getNotes() + "\"" + "," +
                    " \"priceInDollar\":" + " " + p.getPriceInDollar() + "" + "," +
                    " \"weightInKg\":" + " " + p.getWeightInKg() + "" + "," +
                    " \"delivered\":" + " " + p.isDelivered() + "" + "," +
                    " \"expectedDeliveryDate\":" + " \"" + p.getExpectedDeliveryDate() + "\"" + "," +
                    " \"expiryDate\":" + " \"" + ((Perishable) p).getExpiryDate() + "\""
                    + " }' http://localhost:8080/addPerishable";
            System.out.println(commandToAddPackagePerishable); //check curl command for copying to terminal if client gui errors
            System.out.println("Perishable Added");
            ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToAddPackagePerishable.split(" "));
            Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
            processToRequestAllPackages.destroy();
            //Process process = Runtime.getRuntime().exec(commandToAddPackagePerishable);
        } else if (p instanceof Electronic) {
            String commandToAddPackageElectronic = "curl -H \"Content-Type: application/json\" -X POST -d  '{ "
                    + "\"name\":" + " \"" + p.getName() + "\"" + "," +
                    " \"notes\":" + " \"" + p.getNotes() + "\"" + "," +
                    " \"priceInDollar\":" + " " + p.getPriceInDollar() + "" + "," +
                    " \"weightInKg\":" + " " + p.getWeightInKg() + "" + "," +
                    " \"delivered\":" + " " + p.isDelivered() + "" + "," +
                    " \"expectedDeliveryDate\":" + " \"" + p.getExpectedDeliveryDate() + "\"" + "," +
                    " \"environmentalHandlingFee\":" + " " + ((Electronic) p).getEnvironmentalHandlingFee() + ""
                    + " }' http://localhost:8080/addElectronic";
            System.out.println(commandToAddPackageElectronic); //check curl command for copying to terminal if client gui errors
            System.out.println("Electronic Added");
            ProcessBuilder processBuilderToRequestAllPackages = new ProcessBuilder(commandToAddPackageElectronic.split(" "));
            Process processToRequestAllPackages = processBuilderToRequestAllPackages.start();
            //Process process = Runtime.getRuntime().exec(commandToAddPackageElectronic);
            processToRequestAllPackages.destroy();
        }
    }

    /**
     * This method will call a curl post command to remove a package based on index of array list.
     * @param indexToRemove
     * @throws IOException
     */
    public void removePackageFromServer(int indexToRemove) throws IOException {
        String commandToRemove = "curl -H \"Content-Type: application/json\" -X POST http://localhost:8080/removePackage/" + (indexToRemove);
        System.out.println(commandToRemove); //check what curl command looks like, to print it to the terminal if errors
        ProcessBuilder processBuilderToRemovePackage = new ProcessBuilder(commandToRemove.split(" "));
        Process processToRemovePackage = processBuilderToRemovePackage.start();
    }

    /**
     * This method will call a curl post command to set a package to delivered, based on index of array list.
     * @param indexToSetDelivered
     * @throws IOException
     */
    public void setPackageToDeliveredFromServer(int indexToSetDelivered) throws IOException {
        String commandToRemove = "curl -H \"Content-Type: application/json\" -X POST http://localhost:8080/markPackageAsDelivered/" + (indexToSetDelivered);
        System.out.println(commandToRemove); //check what curl command looks like, to print it to the terminal if errors
        ProcessBuilder processBuilderToRemovePackage = new ProcessBuilder(commandToRemove.split(" "));
        Process processToRemovePackage = processBuilderToRemovePackage.start();
    }



    /**
     * get the package made in gui, and store in array list
     * @param packInfo
     */
    public void addPackageFromGUI(Package packInfo){
        packageListInMenu.add(packInfo);
        System.out.println(packInfo);
    }

    /**
     * simply get the array list to be used for other options
     * @return
     */
    public ArrayList<Package> getPackageListFromMenu(){
        return packageListInMenu;
    }



    /**
     * simply sort the array list, refer to child classes for sorting implementation.
     */
    public void sortPackageInMenu(){
        Collections.sort(packageListInMenu);
    }

    /**
     * return a count of overdue packages, when printing the overdue packages to screen.
     * additionally print the output to the console, if gui gives errors.
     */
    public int getOverduePackagesCount() {
        boolean overduePackageExists = false;
        int packageNumber = 1;
        int overduePackagesCount = 0;

        if(packageListInMenu.isEmpty()){
            System.out.println("There are no packages.");
        } else {
            for(Package p : packageListInMenu){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is before .now(), print the package.
                if(!p.isDelivered() && now.isAfter(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    overduePackageExists = true;
                    overduePackagesCount++;
                }
            }
            if(!overduePackageExists){
                System.out.println("There are no overdue packages.");
            }
        }
        return overduePackagesCount;
    }

    /**
     * return a count of upcoming packages, when printing the upcoming packages to screen.
     * additionally print the output to the console, if gui gives errors.
     */
    public ArrayList<Package> getUpcomingPackagesList() {
        boolean upcomingPackageExists = false;
        int packageNumber = 1;
        int upcomingPackagesCount = 0;
        ArrayList<Package> upcomingPackagesList = new ArrayList<>();

        if(packageListInMenu.isEmpty()){
            System.out.println("There are no packages.");
        } else {
            for(Package p : packageListInMenu){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is after .now(), print the package.
                if(!p.isDelivered() && now.isBefore(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    upcomingPackageExists = true;
                    upcomingPackagesCount++;
                    upcomingPackagesList.add(p);
                }
            }
            if(!upcomingPackageExists){
                System.out.println("There are no upcoming packages.");
            }
        }
        return upcomingPackagesList;
    }



}

