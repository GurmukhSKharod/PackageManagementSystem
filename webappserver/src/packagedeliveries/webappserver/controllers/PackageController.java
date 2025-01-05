package packagedeliveries.webappserver.controllers;


import packagedeliveries.webappserver.control.PackageDeliveriesTrackerControlServer;
import packagedeliveries.webappserver.model.*;
import packagedeliveries.webappserver.model.Book;
import packagedeliveries.webappserver.model.Electronic;
import packagedeliveries.webappserver.model.Package;
import org.springframework.web.bind.annotation.*;
import packagedeliveries.webappserver.model.Perishable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * PackageController.java
 * Gurmukh Kharod
 *
 * The following class contains all the endpoints for the program.
 * If you would like to test the endpoints in the IntelliJ terminal,
 * please refer to the curl commands text doc for easy pasting and information about each command.
 *
 * This class will interact with PackageDeliveriesTrackerControlServer.java to accomplish its tasks.
 */
@RestController
public class PackageController {

    private static ArrayList<Package> packageListInServerController = new ArrayList<>();
    private String nameInServerController = "";
    private String notesInServerController;
    private double priceInDollarInServerController;
    private double weightInKgInServerController;
    private boolean deliveredInServerController;
    private LocalDateTime expectedDeliveryDate = LocalDateTime.now();
    private LocalDateTime now = LocalDateTime.now();
    PackageDeliveriesTrackerControlServer packageDeliveriesTrackerControlServer =
            new PackageDeliveriesTrackerControlServer();


    //test packages to see if controller sends packages from array list to web server
    //these packages have been kept in for the final assignment 4 submission, as I cannot figure out the error
    //more details will be explained via email.
    Package book1 = new Book("book test name 1", "book test notes 1", 1111, 1111, false, now , "author test 1");
    Package p1 = new Perishable("p test name 1", "p test notes 1", 222.22, 222, false, now.plusHours(12) , now);
    Package e1 = new Electronic("e test name 1", "e test notes 1", 333.33, 333, false, now.plusHours(7) , 3333.33);


    /**
     * This simple get method will add the sample packages above to the array list in the server.
     * We can use this get request to easily test our server.
     * In the client GUI, there will be an addSamplePackages button, used to call this method.
     * The packages have specifically been created:
     * 1. There is one package of each type.
     * 2. Each package has a different date
     * 3. When viewing upcoming/overdue packages, the functionality will be able to show atleast one of these packages.
     */
    @GetMapping("/addSamplePackages")
    public ArrayList<Package> addSamplePackages(){
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(book1);
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(p1);
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(e1);
        System.out.println("packages added");
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }
    /**
    The following method will simply list all packages to the web server by iterating through the array list.
     */
    @GetMapping("/listAll")
    public ArrayList<Package> listAllPackages() {
        //addSamplePackages();
        System.out.println("List all packages");
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }



    /**
     * The following method is used to add a new package from the array list to the  web server. - book
     */
    @PostMapping("/addBook")
    public ArrayList<Package> addBook(@RequestBody Book book) {
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(book);
        System.out.println("Book added");
        System.out.println(packageDeliveriesTrackerControlServer.getPackageListInServerControl());
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }

    /**
     * The following method is used to add a new package from the array list to the  web server. - perishable
     */
    @PostMapping("/addPerishable")
    public ArrayList<Package> addPerishable(@RequestBody Perishable perishable) {
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(perishable);
        System.out.println("Perishable added");
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }

    /**
     * The following method is used to add a new package from the array list to the  web server. - electronic
     */
    @PostMapping("/addElectronic")
    public ArrayList<Package> addElectronic(@RequestBody Electronic electronic) {
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().add(electronic);
        System.out.println("Electronic added");
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }

    /**
     * The following method is used to remove an existing package from the array list to the  web server.
     * Grab i from the POST curl command where i is the index in the array list to remove
     */
    @PostMapping("/removePackage/{i}")
    public ArrayList<Package> removePackage(@PathVariable int i) {
        packageDeliveriesTrackerControlServer.getPackageListInServerControl().remove(i);
        System.out.println("package" + ++i + "removed");
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }

    /**
     * The following method is used to mark packages as delivered and return the array list after change.
     * Grab i from the POST curl command where i is the index in the array list to set to delivered or not delivered
     */
    @PostMapping("/markPackageAsDelivered/{i}")
    public ArrayList<Package> markPackageAsDelivered(@PathVariable int i) {
        if(packageDeliveriesTrackerControlServer.getPackageListInServerControl().get(i).isDelivered() == false){
            packageDeliveriesTrackerControlServer.getPackageListInServerControl().get(i).setDelivered(true);
            System.out.println("package" + ++i + "set to delivered");
        } else if (packageDeliveriesTrackerControlServer.getPackageListInServerControl().get(i).isDelivered() == true){
            packageDeliveriesTrackerControlServer.getPackageListInServerControl().get(i).setDelivered(false);
            System.out.println("package" + ++i + "set to undelivered");
        }
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }

    /**
     The following method will  list all overdue packages to the web server by iterating through the array list,
     and checking if the packages current expected delivery date is less than .now(), and if delivered is false.
     */
    @GetMapping("/listOverduePackage")
    public ArrayList<Package> listOverduePackage() {
        //addSamplePackages();
        int packageNumber = 0;
        boolean overduePackageExists = false;
        int overduePackagesCount = 0;
        ArrayList<Package> overduePackagesListInController = new ArrayList<>();

        if(packageDeliveriesTrackerControlServer.getPackageListInServerControl().isEmpty()){
            System.out.println("There are no packages, and therefore no overdue packages.");

        } else {
            for(Package p : packageDeliveriesTrackerControlServer.getPackageListInServerControl()){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is before .now(), print the package.
                if(!p.isDelivered() && now.isAfter(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    overduePackageExists = true;
                    overduePackagesCount++;
                    overduePackagesListInController.add(p); //keep adding overdue packages
                }
            }
            if(!overduePackageExists){
                System.out.println("There are no overdue packages.");
            }
        }
        System.out.println( "# of overdue:" + overduePackagesCount);
        return overduePackagesListInController; //return the overdue packages to print to server
    }

    /**
     The following method will  list all upcoming packages to the web server by iterating through the array list,
     and checking if the packages current expected delivery date is greater than .now(), and if delivered is false.
     */
    @GetMapping("/listUpcomingPackage")
    public ArrayList<Package> listUpcomingPackage() {
        //addSamplePackages();
        int packageNumber = 0;
        boolean upcomingPackageExists = false;
        int upcomingPackagesCount = 0;

        ArrayList<Package> upcomingPackagesListInController = new ArrayList<>();

        if(packageDeliveriesTrackerControlServer.getPackageListInServerControl().isEmpty()){
            System.out.println("There are no packages, and therefore no upcoming packages.");

        } else {
            for(Package p : packageDeliveriesTrackerControlServer.getPackageListInServerControl()){
                LocalDateTime now = LocalDateTime.now();
                //if the package is not delivered and the packages delivery date is before .now(), print the package.
                if(!p.isDelivered() && now.isBefore(p.getExpectedDeliveryDate())){
                    System.out.print("Package #" + packageNumber++);
                    System.out.println(p);
                    System.out.println();
                    upcomingPackageExists = true;
                    upcomingPackagesCount++;
                    upcomingPackagesListInController.add(p);
                }
            }
            if(!upcomingPackageExists){
                System.out.println("There are no upcoming packages.");
            }
        }
        System.out.println( "# of upcoming:" + upcomingPackagesCount);
        return upcomingPackagesListInController; //return the upcoming packages to print to server
    }

    /**
     The following method will simply call write to file method in the control server class when user exits.
     */
    @GetMapping("/exit")
    public ArrayList<Package> exitProgram() {
        Collections.sort(packageDeliveriesTrackerControlServer.getPackageListInServerControl());
        packageDeliveriesTrackerControlServer
                .writeToFile(packageDeliveriesTrackerControlServer.getPackageListInServerControl());
        return packageDeliveriesTrackerControlServer.getPackageListInServerControl();
    }





}
