package packagedeliveries.webappserver;

import packagedeliveries.webappserver.control.PackageDeliveriesTrackerControlServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * WebappserverApplication.java
 * Gurmukh Kharod
 *
 * This class will simply start the server and
 * find an existing package and add its contents to the servers array list.
 *
 * The two main classes for the server side are:
 * PackageController.java - containing all spring boot methods will get/post mapping endpoints
 * PackageDeliveriesTrackerControlServer.java - containing helper methods for the above java class, and read/write to file.
 *
 *
 *
 * The following documentations were used to complete this program:
 *  https://attacomsian.com/blog/jackson-read-write-json
 *  https://www.youtube.com/watch?v=rXBsnNCH59o
 *  https://spring.io/guides/gs/rest-service/
 *  https://www.baeldung.com/java-curl
 *  https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/time/LocalDateTime.html
 *  https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
 *  https://docs.oracle.com/javase/tutorial/uiswing/events/windowlistener.html
 *  https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
 *  https://javadoc.io/doc/com.github.lgooddatepicker/LGoodDatePicker/latest/com/github/lgooddatepicker/components/DateTimePicker.html
 */
@SpringBootApplication
public class WebappserverApplication {

	/**
	 * The main method of this class simply starts the server and grabs the list contents from the file, should a file exist.
	 * @param args
	 */
	public static void main(String[] args) {

		//grab the file contents if the file exists
		File f = new File("./packageListInMenu.json");
		if(f.exists() && !f.isDirectory()) {
			PackageDeliveriesTrackerControlServer ps = new PackageDeliveriesTrackerControlServer();
			ps.readFromFile();
		}

		//run the server
		SpringApplication.run(WebappserverApplication.class, args);
	}

}
