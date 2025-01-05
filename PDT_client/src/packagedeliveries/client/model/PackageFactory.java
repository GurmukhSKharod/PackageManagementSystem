package packagedeliveries.client.model;


/**
 * PackageFactory.java
 * Gurmukh Kharod
 *
 * This class acts as a helper class for the entire program.
 *
 * When we create a package object, we must choose the package type,
 * and the getInstance method allows us to do this.
 */
public class PackageFactory {

    //use getInstance method to get object of child types for package.
    public Package getInstance(String packageType){
        if(packageType == null){
            return null;
        }
        if(packageType.equalsIgnoreCase("BOOK")){
            return new Book();

        } else if(packageType.equalsIgnoreCase("PERISHABLE")){
            return new Perishable();

        } else if(packageType.equalsIgnoreCase("ELECTRONIC")){
            return new Electronic();
        }

        return null;
    }
}
