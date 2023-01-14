# PackageManagementSystem
This project uses Java in IntelliJ to manage packages of 3 different types - Book, Perishable, Electronic. 

FRONT-END:
It uses IntelliJ's built-in GUI functionality on the front end to allow users to create new packages to be added to a list. When creating a package, users can select information such as a package name, price, weight, etc. There are also unique fields that a user can enter based on the package type they have selected, such as the selection of an expiry date for a perishable package type. 

BACK-END:
All packages are stored both locally via json, and on a web server. The backend holds the web server, created via the Java Spring Boot framework, where a custom API holds different calls to the web server to perform CRUD operations on the existing packages. 


Lastly, this application was created in four separate iterations, where each new iteration builds upon all existing features of the previous iterations, such as improvements to user interfaces or additional entry fields.  

Note that when running the application: You must first run the web server, then secondly run the GUI front end separately to allow all new additions to be added to the server. 
