package packagedeliveries.client;

import packagedeliveries.client.view.PackageMenuGUI;

import javax.swing.*;


/**
 * PackageDeliveriesTrackerMain.java
 * Gurmukh Kharod
 *
 *
 * The one and only simple role this main class has is to start the GUI form, PackageMenuGUI.
 * The server side IntelliJ project will handle all the operations the user undergoes during runtime in the PackageMenuGUI.
 * An arraylist holding packages of three types is used and stored on the server.
 *
 *
 *  * The following documentation was used as reference:
 *  * (These documentations contains useful information about methods like .addAll() for ArrayList)
 *  *
 *  * This documentation was used:
 *  * 2.   https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
 *  * 3.   https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
 *  * 4.   https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
 *  * 5.   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
 *  *
 *  * This additional documentation was used :
 *  * 6.   https://github.com/google/gson/blob/master/extras/src/main/java/com/google/gson/typeadapters/RuntimeTypeAdapterFactory.java
 *  *
 *  * These additional documentations were used:
 *  * 7.   https://docs.oracle.com/javase/tutorial/uiswing
 *  * 8.   https://javadoc.io/doc/com.github.lgooddatepicker/LGoodDatePicker/latest/com/github/lgooddatepicker/components/DateTimePicker.html
 *  * These additional documentations was used:
 *  * 9.    https://attacomsian.com/blog/jackson-read-write-json
 *  * 10.   https://www.youtube.com/watch?v=rXBsnNCH59o
 *  * 11.   https://spring.io/guides/gs/rest-service/
 *  * 12.   https://www.baeldung.com/java-curl
 *  * 13.   https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/time/LocalDateTime.html
 *  * 14.   https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
 *  * 15.   https://docs.oracle.com/javase/tutorial/uiswing/events/windowlistener.html
 *  * 16.   https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
 *  * 17.   https://javadoc.io/doc/com.github.lgooddatepicker/LGoodDatePicker/latest/com/github/lgooddatepicker/components/DateTimePicker.html
 */
public class PackageDeliveriesTrackerMain {

    public static void main(String[] args) {
        //create the frame on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PackageMenuGUI();
            }
        });
    }
}

