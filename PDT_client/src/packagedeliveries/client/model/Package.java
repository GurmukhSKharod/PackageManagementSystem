package packagedeliveries.client.model;

import java.time.LocalDateTime;

/**
 * Package.java
 * Gurmukh Kharod
 *
 * This is the interface which acts as the parent class to 3 subclasses:
 * Book.java, Perishable.java, Electronic.java.
 *
 * This interface has one final String field, used for the RuntimeTypeAdapter for gson.
 *
 * Secondly, all setter and getter methods that are common between each child class are found here.
 * Lastly, this class extends the Comparable Interface for organizing packages based on their dates.
 *
 * Refer to the child classes for further implementation of these methods.
 *
 */
public interface Package extends Comparable<Package> {
    final String type = "Package"; //required field for the RuntimeTypeAdapter
    void setName(String name);
    void setNotes(String notes);
    void setPriceInDollar(double priceInDollar);
    void setWeightInKg(double weightInKg);
    void setDelivered(boolean delivered);
    void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate);
    String getName();
    String getNotes();
    double getPriceInDollar();
    double getWeightInKg();
    boolean isDelivered();
    LocalDateTime getExpectedDeliveryDate();

    String getType();

    int compareTo(Package o);
}
