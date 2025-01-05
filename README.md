# PackageManagementSystem
Package Management System created using Java in IntelliJ, with Java Swing App Client and Java Spring Boot Server with API Endpoints using curl commands.

## Skills and Technologies Used

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)


## Functionalities: 
1. Perform CRUD operations on packages, and Save packages in JSON format, using GSON. 
2. Apply OOP Design Patterns to support multiple package types. 
3. Implement the user interactivity in an IntelliJ Swing App GUI. 
4. Use Spring Boot + API Endpoints to store packages onto a web server

## Directory Structure
The project is seperated into 2 folders with their own main .java file:
- Client -> PackageDeliveriesTrackerMain
- WebappServer -> WebappserverApplication

## How to Run App
- Both can be run seperately by setting each of the respective src folders as Source Root in IntelliJ --> right clicking src > Mark dir As > Source Root.
- Run the main files seperately, and run the web server first, so the client can recognize the server and bring in the existing packages from the server.
- The current project SDK version is JDK-17. And the following Maven libraries have been used. You can add Maven libraries with: File > Project Structure > Libraries >  " + " icon  > From Maven...
- You can download JDK-17 from oracle here: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html 


## How the APP Works:

### Client:

First ensure the server is running, and to check the activity of the server using localhost:8080 on your browser (Google Chrome).
Then Start the Client which is a Java Swing App in IntelliJ.

We will go over the User Interface and the several options that are offered for working with packages.

When starting the app, the main panel will be shown.
This main panel contains a title, three buttons for listing packages based on a condition,
the JScrollPane for the list, and an add package button on the bottom.

1. Listing packages can be done by clicking "All" button.

2. To add a package, click "Add new package" at the bottom.  
-a message will show up with the data entry rules  
-The user entry for a new package frame will open, separate from the main frame.  
Start by choosing a package type: eg. Book  
-Then enter in the necessary fields for a book.  
-Note that "notes", "expiry date", and "environmental handling fee" will all not be necessary for a Book package.  
-The add package section shows text entry fields for all possible options, but the user does not need to enter
in the fields if they are not neccessary based on the package type.  
-If for example, the user enters expiry date for a book, the app will not crash.  
-Instead the app will simply ignore that piece of user input and create a book object,
assuming all required other fields are entered.  
-Let's assume we do not enter in a value for a field,
then when clicking create, a JOptionPane error message will be given to the user.  
-The date picker was not implemented.  
the user must enter the date in the correct format: yyyy-mm-dd hh:mm    eg. 2000-12-12 12:30  
-Finally the user can click "create" or "cancel"

3. To remove a package, click the corresponding remove button shown for each package.  
-a remove button will only be shown on the package list,
so if there are no packages, there is no way to remove one, which is expected.  

4. To list overdue packages, simply click "Overdue" button  

5. To list upcoming packages, simply click "Upcoming" button  

6. To mark packages as delivered click the checkbox where delivered is located.  
-this delivered check box is specific to each package  
-if there are no packages, the list will be empty,
and we can't set any packages to delivered, which is expected  

7. Finally, when clicking the "x" to close the frame, 
the app will exit, and all packages will be added to the json file.  


### Server:

First run the server using the steps above, and check the activity of the server using localhost:8080 on your browser (Google Chrome)

Here we can use the following commands to perform one of the main operations seen above in the client.
Additionally, an example of each curl command is also shown, for testing purposes in the terminal,
in the case that we are referring to a POST curl command.

1. To ping the server:
curl -X GET localhost:8080/ping


2. To list all packages in the server:
curl -X GET localhost:8080/listAll


3. To add a new package: (3 separate curl post commands)


a) add book:
curl -H "Content-Type: application/json" -X POST -d
'{ Book JSON Object }' localhost:8080/addBook

ex:
curl -H "Content-Type: application/json" -X POST -d
'{ "name": "b1", "notes": "111", "priceInDollar": 1111.0,
"weightInKg": 11111.0, "delivered": false,
"expectedDeliveryDate": "1111-11-11T11:11", "authorName": "a100000" }' localhost:8080/addBook


b) add perishable:
curl -H "Content-Type: application/json" -X POST -d
'{ Perishable JSON Object }' localhost:8080/addPerishable

ex:
curl -H "Content-Type: application/json" -X POST -d
'{ "name": "p1", "notes": "111", "priceInDollar": 1111.0,
"weightInKg": 11111.0, "delivered": false,
"expectedDeliveryDate": "1111-11-11T11:11", "expiryDate": "2222-11-11T11:11" }' localhost:8080/addPerishable


c) add electronic:
curl -H "Content-Type: application/json" -X POST -d
'{ Electronic JSON Object }' localhost:8080/addElectronic

curl -H "Content-Type: application/json" -X POST -d
'{ "name": "e1", "notes": "111", "priceInDollar": 1111.0,
"weightInKg": 11111.0, "delivered": false,
"expectedDeliveryDate": "1111-11-11T11:11", "environmentalHandlingFee" : 3333.33 }' localhost:8080/addElectronic


4. To remove a package:
curl -H "Content-Type: application/json" -X POST http://localhost:8080/removePackage/{index to remove}

ex:
curl -H "Content-Type: application/json" -X POST http://localhost:8080/removePackage/0
(removes the first package in the array list - index 0)


5. List Overdue Packages:
curl -X GET localhost:8080/listOverduePackage


6. List Upcoming Packages:
curl -X GET localhost:8080/listUpcomingPackage


7. mark Package As Delivered:
curl -H "Content-Type: application/json" -X POST http://localhost:8080/markPackageAsDelivered/{index}

ex:
curl -H "Content-Type: application/json" -X POST http://localhost:8080/markPackageAsDelivered/0
(sets 0 array index to delivered - index 0)


8. exit program:
curl -X GET localhost:8080/exit


Additional command to add sample packages for testing purposes:
curl -X GET localhost:8080/addSamplePackages












