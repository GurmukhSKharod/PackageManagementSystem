package packagedeliveries.webappserver.model;


import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Perishable.java
 * Gurmukh Kharod
 *
 * This class is a child of Package.
 * This class implements Package.
 * All getters and setters have been overridden in this class.
 * The compareTo method from the parent class has also been overridden.
 *
 * Additionally, the following contents have been added to make this class unique:
 * The Perishable class requires a new input type: LocalDateTime expiryDate;
 * This field has getter/setter methods, modifies the default toString, and modifies the constructor.
 * Using the RuntimeTypeAdapter, the type of the package will also be displayed.
 */
public class Perishable implements Package {

    private String name = "";
    private String notes;
    private double priceInDollar;
    private double weightInKg;
    private boolean delivered;

    private LocalDateTime expectedDeliveryDate;

    private LocalDateTime expiryDate;

    /**
     * constructor - used to create packages during runtime.
     * @param name
     * @param notes
     * @param priceInDollar
     * @param weightInKg
     * @param delivered
     * @param expectedDeliveryDate
     */
    public Perishable(String name, String notes, double priceInDollar, double weightInKg, boolean delivered, LocalDateTime expectedDeliveryDate, LocalDateTime expiryDate) {
        this.name = name;
        this.notes = notes;
        this.priceInDollar = priceInDollar;
        this.weightInKg = weightInKg;
        this.delivered = delivered;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.expiryDate = expiryDate;
    }

    /**
     * empty constructor for an empty object.
     * Note: We can have multiple constructors for a single class, so long as the parameters in the heading are different.
     */
    public Perishable() {

    }


    /**
     * The following getters and setters below are common between all subclasses of Package.java and are overridden here.
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public double getPriceInDollar() {
        return priceInDollar;
    }

    @Override
    public void setPriceInDollar(double priceInDollar) {
        this.priceInDollar = priceInDollar;
    }

    @Override
    public double getWeightInKg() {
        return weightInKg;
    }

    @Override
    public void setWeightInKg(double weightInKg) {
        this.weightInKg = weightInKg;
    }

    @Override
    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    @Override
    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    /**
     * getter and setter for the new field, these are not overridden because they are not in the parent interface.
     * This field is unique to this subclass.
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getType() { return "Perishable";}


    /**
     * The toString Method for this child class has all the common fields, as well as this class' unique field.
     */
    @Override
    public String toString() {

        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = expectedDeliveryDate.format(dateTimeFormatter);
        String formattedExpiryDateTime = expiryDate.format(dateTimeFormatter);

        return   "\nPackage Type: Perishable " +
                "\nPackage: " + this.name +
                "\nNotes: " + this.notes +
                "\nPrice: " + numberFormatter.format(this.priceInDollar) +
                "\nWeight: " + this.weightInKg + "kg" +
                "\nExpected Delivery Date: " + formattedDateTime +
                "\nDelivered? " + ( this.delivered ? "yes" : "no" ) +
                "\nExpiry Date: " + formattedExpiryDateTime;

    }


    /**
     * Used to organize book objects by date, this method is shared between all children of the Package.java interface.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Package o) {
        return this.getExpectedDeliveryDate().compareTo( o.getExpectedDeliveryDate() );
    }
}
