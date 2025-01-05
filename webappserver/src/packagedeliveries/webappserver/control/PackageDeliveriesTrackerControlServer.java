package packagedeliveries.webappserver.control;

import packagedeliveries.webappserver.gson.extras.RuntimeTypeAdapterFactory;
import packagedeliveries.webappserver.model.Book;
import packagedeliveries.webappserver.model.Electronic;
import packagedeliveries.webappserver.model.Package;
import packagedeliveries.webappserver.model.Perishable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;


/**
 * PackageDeliveriesTrackerControlServer.java
 * Gurmukh Kharod
 *
 * The following class acts as a helper class for our PackageController.java class.
 * Here we can find methods to read/write to the file.
 *
 * This class also contains the array list for the server.
 * This keeps the server side of this program modular,
 * as we separate the array implementation from the endpoints.
 *
 * Note how we still are using the gson.extras file for reading specific package types from the file.
 *
 */
public class PackageDeliveriesTrackerControlServer {

    private static ArrayList<Package> packageListInServerControl = new ArrayList<>();
    private String nameInServerController = "";
    private String notesInServerController;
    private double priceInDollarInServerController;
    private double weightInKgInServerController;
    private boolean deliveredInServerController;
    private LocalDateTime expectedDeliveryDate = LocalDateTime.now();
    private LocalDateTime now = LocalDateTime.now();

    //test packages to see if controller sends packages from array list to web server
    //This may be used when testing, in-case client side does not function as intended.
    Package book1 = new Book("book test name 1", "book test notes 1", 1111, 1111, false, now , "author test 1");
    Package p1 = new Perishable("p test name 1", "p test notes 1", 222.22, 222, false, now.plusHours(12) , now);
    Package e1 = new Electronic("e test name 1", "e test notes 1", 333.33, 333, false, now.plusHours(7) , 3333.33);


    /**
     * get method to return the array list in this class, to be used in the Controller class
     */
    public static ArrayList<Package> getPackageListInServerControl() {
        Collections.sort(packageListInServerControl);
        return packageListInServerControl;
    }

    /**
     * readFromFile() will open the file, if it exists, and put all the values into packages.
     * These packages will be added to the array list, which will be returned to the Main.
     *
     * This method has been modified for assignment 2 to allow a RuntimeTypeAdapterFactory<Package> object,
     * to determine the type of object when reading.
     */
    public ArrayList<Package> readFromFile() {
        //read from file, print content
        try {
            File packageListFile = new File("./packageListInMenu.json");
            FileReader fileReader = new FileReader(packageListFile);
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

            packageListInServerControl = gson.fromJson(fileReader, type); //reads file using file reader of type array list
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.err.println("Error in creating a file reader object.");
        } catch (IOException e) {
            System.err.println("Error in closing the file.");
        }

        return packageListInServerControl;

    }

    /**
     *
     * @param packageListInServerControl
     *
     * writeToFile() will add all the existing packages to the file.
     * All the packages currently in te file will be overwritten.
     * These packages will not be lost however, as they were added to packageListInMain at runtime.
     *
     * This method has been modified for assignment 2 to allow a RuntimeTypeAdapterFactory<Package> object,
     * to determine the type of object when writing.
     */
    public void writeToFile(ArrayList<Package> packageListInServerControl){
        //Take the arrayList from the Main.java and write to File using it.
        //This array contains all the packages already in the file, should they exist.
        try {
            File packageListFile = new File("./packageListInMenu.json");
            FileWriter fileWriter = new FileWriter(packageListFile);
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
            gson.toJson(packageListInServerControl, fileWriter); //writes theList to file
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error in writing the file.");
        }
    }



}
