

package cmpt213.assignment1.packagedeliveriestracker;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * PackageInformation.java
 * Gurmukh Kharod
 * 301 383 004
 *
 * The following class allows the program to create a PackageInformation object,
 * which includes the following fields:
 * name, notes, priceInDollar, weightInKg, delivered, expectedDeliveryDate.
 * Additionally, setter methods, getter methods, and a toString() method are used.
 */
public class PackageInformation {
    private String name = "";
    private String notes;
    private double priceInDollar;
    private double weightInKg;
    private boolean delivered;

    private LocalDateTime expectedDeliveryDate;

    /**
     * constructor - used to create packages during runtime.
     * @param name
     * @param notes
     * @param priceInDollar
     * @param weightInKg
     * @param delivered
     * @param expectedDeliveryDate
     */
    public PackageInformation(String name, String notes, double priceInDollar, double weightInKg, boolean delivered, LocalDateTime expectedDeliveryDate) {
        this.name = name;
        this.notes = notes;
        this.priceInDollar = priceInDollar;
        this.weightInKg = weightInKg;
        this.delivered = delivered;
        this.expectedDeliveryDate = expectedDeliveryDate;

    }

    /**
     * empty constructor for an empty object.
     * Note: We can have multiple constructors for a single class, so long as the parameters in the heading are different.
     */
    public PackageInformation() {

    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @param priceInDollar
     */
    public void setPriceInDollar(double priceInDollar) {
        this.priceInDollar = priceInDollar;
    }

    /**
     * @param weightInKg
     */
    public void setWeightInKg(double weightInKg) {
        this.weightInKg = weightInKg;
    }

    /**
     * @param delivered
     */
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    /**
     * @param expectedDeliveryDate
     */
    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }


    /**
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @return priceInDollar
     */
    public double getPriceInDollar() {
        return priceInDollar;
    }

    /**
     * @return weightInKg
     */
    public double getWeightInKg() {
        return weightInKg;
    }

    /**
     * @return delivered
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * @return expectedDeliveryDate
     */
    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * @return A String containing the concatenation of all fields.
     * Note: We are now returning the formattedExpectedDeliveryDate,
     *       after accepting the input of expectedDeliveryDate.
     */
    @Override
    public String toString() {

        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = expectedDeliveryDate.format(dateTimeFormatter);

        return  "\nPackage: " + this.name +
                "\nNotes: " + this.notes +
                "\nPrice: " + numberFormatter.format(this.priceInDollar) +
                "\nWeight: " + this.weightInKg + "kg" +
                "\nExpected Delivery Date: " + formattedDateTime +
                "\nDelivered? " + ( this.delivered ? "yes" : "no" );
    }
}
