package packagedeliveries.client.view;
import packagedeliveries.client.control.PackageDeliveriesTrackerControl;
import packagedeliveries.client.model.Book;
import packagedeliveries.client.model.Electronic;
import packagedeliveries.client.model.Package;
import packagedeliveries.client.model.Perishable;
import packagedeliveries.client.model.PackageFactory;

import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 *  * PackageMenuGUI.java
 *  * Gurmukh Kharod
 *  * 301 383 004
 *
 *  This class represents the GUI for this application.
 *  The responsibility of this class is to generate the main frame, and user entry frame for a new package.
 *  This class will also directly work with the control class.
 *  The control class creates and sends an array over to the gui.
 *  The gui will simply take the objects in the array list and print based on parameters, add new packages, etc.
 *
 *
 * Updates for Assignment 4:
 *
 * For Assignment 3, The upcoming packages list printing to
 * jscrollpane and the setting package to delivered/not delivered
 * were both not functioning properly.
 *
 * In Assignment 4, both features have been properly implemented.
 * For listing upcoming packages, the GUI simply gets the proper list from the control class to be printed.
 * For setting to delivered, a simple action command has been used.
 *
 * Specifically in this GUI class for Assignment 4,
 * new code has only been added to the actionPerformed() method,
 * where we now call HTTP GET/POST requests in methods found in the PackageDeliveriesTrackerControl class.
 *
 *
 */
public class PackageMenuGUI implements ActionListener {


    //the following GUI component variables are for our main class and will be assigned in constructor
    JFrame applicationFrame;
    JPanel mainPanel;
    JLabel titleLabel;
    JPanel topButtonsPanel;
    JButton allPackagesButton;
    JButton overduePackagesButton;
    JButton upcomingPackagesButton;
    JPanel middleJScrollPanel;
    JLabel noPackages;

    JLabel packagesName = new JLabel("Packages:");
    JPanel accumulatedPackagePanel = new JPanel();
    JPanel bottomButtonPanel;
    JButton addNewPackageButton;




    //The following GUI components are for our create new package section
    JFrame newPackageMenuFrame;
    JPanel mainPanelInMenu;
    private JPanel topTitleInMenuPanel;
    private JPanel userEntryPanel;
    private JPanel bottomButtonsInMenuPanel;
    private JLabel newPackageMenuTitleLabel;

    //private JLabel instructionsLabel;

    String instructions = "\n" +
            "Instructions: \n" +
            "\n" +
            "Please select a package type, and enter values in for:\n" +
            "1. package name\n" +
            "2. package price\n" +
            "3. package weight\n" +
            "4. package expected delivery date\n" +
            "\n" +
            "Based on the package type, please also enter in an additional field:\n" +
            "1. Book - requires package author name\n" +
            "2. Perishable - requires package expiry date\n" +
            "3. Electronic - requires package environmental handling fee\n" +
            "For the package types, there is no need to enter in values for the extra fields.\n" +
            "Eg. If the package type is book, expiry date and env. handling fee are not required.\n" +
            "\n" +
            "Note the following:\n" +
            "1. package notes can be empty\n" +
            "2. when entering date fields, please use the following format: \n" +
            "'yyyy-dd-mm hh:mm'  \n" +
            "eg. 2000-12-12 05:10\n" +
            "Notice how when entering hour 5, we use 05.\n" +
            "\n" +
            "Thank you. \n";
    String[] packageTypeOptions = {"Book", "Perishable", "Electronic"};

    JLabel packages;
    private JLabel packageTypeLabel;
    private JLabel packageNameLabel;
    private JLabel packageNotesLabel;
    private JLabel packagePriceLabel;
    private JLabel packageWeightLabel;
    private JLabel packageExpectedDeliveryDateLabel;
    private JLabel packageAuthorLabel;
    private JLabel packageExpiryDateLabel;
    private JLabel packageEnvironmentalHandlingFeeLabel;
    private JComboBox packageTypeComboBox;
    private JTextField packageNameInMenu;
    private JTextField packageNotesInMenu;
    private JTextField packagePriceInMenu;
    private JTextField packageWeightInMenu;
    private JTextField packageExpectedDeliveryDateInMenu;
    private JTextField packageAuthorInMenu;
    private JTextField packageExpiryDateInMenu;
    private JTextField packageEnvironmentalHandlingFeeInMenu;
    JButton createPackageInMenuBtn;
    JButton cancelPackageInMenuBtn;

    //used for displaying gui in jPanels on jScrollPane
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel notesLabel;
    private JLabel priceLabel;
    private JLabel weightLabel;
    private JLabel expectedDeliveryDateLabel;
    private JLabel authorLabel;
    private JLabel expiryDateLabel;
    private JLabel environmentalHandlingFeeLabel;

    private JCheckBox deliveredCB;

    private JButton removePackageBtn;


    Box box = new Box(BoxLayout.Y_AXIS);
    JScrollPane packagesScrollPane = new JScrollPane(box);

    JPanel emptyPanel = new JPanel();






    //the following variables are for the package contents
    //Array list must be of type Package, to get access to objects
    private static ArrayList<Package> packageListInGUI = new ArrayList<>();

    //menu versions of the fields found in PackageInfo.java
    private String nameInMenu = "";
    private String notesInMenu;
    private double priceInDollarInMenu;
    private double weightInKgInMenu;
    private boolean deliveredInMenu;
    private LocalDateTime expectedDeliveryDateInMenu = LocalDateTime.now();

    //in assignment 2 and onwards, we want to now create a package type: 1 - Book, 2 - Perishable, 3 - Electronic
    private String packageTypeInMenu = "";

    private String authorNameInMenu; //used for Book
    private LocalDateTime expiryDateInMenu; //used for Perishable
    private double environmentalHandlingFeeInMenu; //used for Electronic

    //objects used for when user enters all valid info
    Package packInfo;
    PackageFactory packageFactory = new PackageFactory();
    PackageDeliveriesTrackerControl packageDeliveriesTrackerControl = new PackageDeliveriesTrackerControl();
    JButton addSamplePackagesPackageButton; //used for testing with server

    //used for printing to JScrollPane
    //ArrayList<JPanel> panelsForScrollPane = new ArrayList<>();

    /**
     * Constructor to make the main menu panel
     */
    public PackageMenuGUI () {
        JOptionPane.showMessageDialog(null,
                "Welcome to the Package Delivery Tracker GUI client.\n" +
                        "If you would like to add some sample packages, please click the " +
                        "\"Add Sample Packages\" Button below. \n" +
                        "If you would like to view updates to the list, please click the \"All\" button.\n" +
                        "Thank you" );

        createMainMenu();
    }

    /**
     * implement the actionPerformed method for the ActionListener interface for all app buttons.
     * generateJScrollPackages() is used to keep the app updating automatically.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("All")){
            //request list from server - GET, then sort, then generate.
            try {
                packageDeliveriesTrackerControl.requestAllPackages();
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            packageDeliveriesTrackerControl.sortPackageInMenu();
            generateJScrollPackages();
        } else if (e.getActionCommand().equals("Overdue")) {
            System.out.println("Overdue packages:\n");
            try {
                packageDeliveriesTrackerControl.requestOverduePackages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            generateJScrollPackagesOverdue();
        } else if (e.getActionCommand().equals("Upcoming")) {
            System.out.println("Upcoming packages:\n");
            try {
                packageDeliveriesTrackerControl.requestUpcomingPackages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            generateJScrollPackagesUpcoming();
        } else if (e.getActionCommand().equals("Add New Package")) {
            packageDeliveriesTrackerControl.sortPackageInMenu();
            createNewPackageMenu();
        } else if(e.getActionCommand().equals("Add Sample Packages")){
            try {
                packageDeliveriesTrackerControl.addSamplePackagesToServer();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                packageDeliveriesTrackerControl.requestAllPackages();
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(null, "Sample Packages Added. \n " +
                    "One of each package type has been added with different expected delivery dates.\n" +
                    "Click on upcoming/overdue buttons above to see how these packages are listed.\n" +
                    "Click on -All- button to view the packages that have just been added to the list");

        } else if (e.getActionCommand().equals("Create")){
            packInfo = inputVerification();
            try {
                packageDeliveriesTrackerControl.addPackageToServer(packInfo);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            try {
                packageDeliveriesTrackerControl.requestAllPackages();
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            generateJScrollPackages();
        } else if (e.getActionCommand().equals("Cancel")){
            newPackageMenuFrame.dispose();
            packageDeliveriesTrackerControl.sortPackageInMenu();
            generateJScrollPackages();
        }else if (e.getActionCommand().contains("remove")){
            //grab getactioncommand name (there is a method), substring get 1-remove
            Character userChoiceToRemove = e.getActionCommand().charAt((0));
            int choiceToRemove = Integer.parseInt(String.valueOf(userChoiceToRemove));
            try {
                packageDeliveriesTrackerControl.removePackageFromServer(choiceToRemove);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(null, "Package number: " + (choiceToRemove+1) + " removed");
            middleJScrollPanel.removeAll();
            middleJScrollPanel.revalidate();
            middleJScrollPanel.repaint();
            packageDeliveriesTrackerControl.sortPackageInMenu();
            try {
                packageDeliveriesTrackerControl.requestAllPackages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            generateJScrollPackages();
        } else if(e.getActionCommand().contains("deliver")){
            Character userChoiceToDeliver = e.getActionCommand().charAt((0));
            System.out.println(userChoiceToDeliver);
            int choiceToDeliver = Integer.parseInt(String.valueOf(userChoiceToDeliver));
            if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(choiceToDeliver).isDelivered() == false){
                //packageDeliveriesTrackerControl.getPackageListFromMenu().get(choiceToDeliver).setDelivered(true); //or whatever the index is
                try {
                    packageDeliveriesTrackerControl.setPackageToDeliveredFromServer(choiceToDeliver);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Package number: " + (choiceToDeliver+1) + " set to delivered");
                middleJScrollPanel.removeAll();
                middleJScrollPanel.revalidate();
                middleJScrollPanel.repaint();
                packageDeliveriesTrackerControl.sortPackageInMenu();
                try {
                    packageDeliveriesTrackerControl.requestAllPackages();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                generateJScrollPackages();
            } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(choiceToDeliver).isDelivered() == true){
                try {
                    packageDeliveriesTrackerControl.setPackageToDeliveredFromServer(choiceToDeliver);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Package number: " + (choiceToDeliver+1) + " set to not delivered");
                middleJScrollPanel.removeAll();
                middleJScrollPanel.revalidate();
                middleJScrollPanel.repaint();
                packageDeliveriesTrackerControl.sortPackageInMenu();
                try {
                    packageDeliveriesTrackerControl.requestAllPackages();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                generateJScrollPackages();
            }

        }
    }

    /**
     * use the component fields found at the top of the class to generate the main menu.
     * The main menu contains the title, buttons at top, scroll pane, and add package button at bottom
     */
    public void createMainMenu(){
        //customize top level frame
        applicationFrame = new JFrame("Gurmukh's Package Deliveries Tracker");
        applicationFrame.setSize(950, 1000);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //packageDeliveriesTrackerControl.startAndGetPackageListInMenu();

        applicationFrame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                //packageDeliveriesTrackerControl.exitProgramFromGUI();
                try {
                    packageDeliveriesTrackerControl.exitProgramHTTP();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        //create main panel, sub panels will be added to main panel
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(800, 1000));

        titleLabel = new JLabel("Gurmukh's Package Deliveries Tracker");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        mainPanel.add(titleLabel);
        mainPanel.add(new JSeparator());


        //sub panel for top buttons (All, Overdue, Upcoming)
        topButtonsPanel = new JPanel();
        topButtonsPanel.setPreferredSize(new Dimension(800, 100));

        allPackagesButton = new JButton("All");
        allPackagesButton.setPreferredSize(new Dimension(200, 50));
        allPackagesButton.addActionListener(this);
        overduePackagesButton = new JButton("Overdue");
        overduePackagesButton.setPreferredSize(new Dimension(200, 50));
        overduePackagesButton.addActionListener(this);
        upcomingPackagesButton = new JButton("Upcoming");
        upcomingPackagesButton.setPreferredSize(new Dimension(200, 50));
        upcomingPackagesButton.addActionListener(this);

        topButtonsPanel.add(allPackagesButton);
        topButtonsPanel.add(upcomingPackagesButton);
        topButtonsPanel.add(overduePackagesButton);



        mainPanel.add(Box.createRigidArea(new Dimension(10, 30))); //padding

        //sub panel for middle JScroll Pane which dynamically shows packages, based on top buttons
        middleJScrollPanel = new JPanel();
        middleJScrollPanel.setPreferredSize(new Dimension(700, 700));



        //packagesScrollPane.setLayout(new FlowLayout());
        packagesScrollPane.setPreferredSize(new Dimension(700, 500));
        packagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

//        try {
//            if(packageDeliveriesTrackerControl.requestAllPackages().isEmpty()){
//                noPackages = new JLabel("No Packages for this section.");
//                accumulatedPackagePanel.add(noPackages);
//                packagesScrollPane.setViewportView(accumulatedPackagePanel);
//            } else {
//                generateJScrollPackages();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        //packagesScrollPane.setViewportView(singlePackagePanel);
        middleJScrollPanel.add(packagesScrollPane);


        mainPanel.add(Box.createRigidArea(new Dimension(10, 40))); //padding

        //sub panel for bottom button to add a new package
        bottomButtonPanel = new JPanel();
        bottomButtonPanel.setPreferredSize(new Dimension(800, 100));

        addNewPackageButton = new JButton("Add New Package");
        addNewPackageButton.setPreferredSize(new Dimension(300, 50));
        addNewPackageButton.addActionListener(this);

        addSamplePackagesPackageButton = new JButton("Add Sample Packages");
        addSamplePackagesPackageButton.setPreferredSize(new Dimension(300, 50));
        addSamplePackagesPackageButton.addActionListener(this);

        bottomButtonPanel.add(addNewPackageButton);
        bottomButtonPanel.add(addSamplePackagesPackageButton);

        // Set the BoxLayout to be Y_AXIS: from top to bottom
        BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

        mainPanel.add(topButtonsPanel);
        mainPanel.add(middleJScrollPanel);
        mainPanel.add(bottomButtonPanel);
        mainPanel.setLayout(boxlayout);

        applicationFrame.add(mainPanel);

        applicationFrame.setVisible(true);
    }

    /**
     * The following method will create the menu for adding a new package.
     * The proper local date time picker has not been implemented.
     *
     * Instead, we can use the proper format: yyyy-mm-dd hh:mm
     *
     * eg. 2000-12-12 12:12
     *
     *
     */
    public void createNewPackageMenu(){
        //set up JFrame for menu
        newPackageMenuFrame = new JFrame("Add A New Package");
        newPackageMenuFrame.setSize(750, 400);
        newPackageMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //set up JPanels for menu
        mainPanelInMenu = new JPanel();
        topTitleInMenuPanel = new JPanel();
        userEntryPanel = new JPanel();
        bottomButtonsInMenuPanel = new JPanel();


        //set up simple title and instructions in menu panel
        newPackageMenuTitleLabel = new JLabel("Add A New Package");
        newPackageMenuTitleLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
        topTitleInMenuPanel.add(newPackageMenuTitleLabel);
        topTitleInMenuPanel.add(new JSeparator());
        JOptionPane.showMessageDialog(null, instructions); //display instructions before user gets to enter


        //set up user entry panel section
        GridLayout gridForUserEntry = new GridLayout(0,2);
        userEntryPanel.setLayout(gridForUserEntry);
        userEntryPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        //labels
        packageTypeLabel = new JLabel("Type: ");
        packageNameLabel = new JLabel("Name: ");
        packageNotesLabel = new JLabel("Notes: ");
        packagePriceLabel = new JLabel("Price: ");
        packageWeightLabel = new JLabel("Weight: ");
        packageExpectedDeliveryDateLabel = new JLabel("Expected Delivery Date: ");
        packageAuthorLabel = new JLabel("Author: ");
        packageExpiryDateLabel = new JLabel("Expiry Delivery Date: ");
        packageEnvironmentalHandlingFeeLabel = new JLabel("Environ. Handling Fee: ");
        //input boxes/combo boxes/date picker
        packageTypeComboBox = new JComboBox(packageTypeOptions); //construct with a string array
        packageTypeComboBox.setEditable(true); //make it editable, default is not
        packageTypeComboBox.setSelectedIndex(0); //set to book at start
        packageTypeComboBox.setMaximumSize(new Dimension(300, 30));
        packageTypeComboBox.addActionListener(this);
        packageNameInMenu = new JTextField( 30);
        packageNotesInMenu = new JTextField(30);
        packagePriceInMenu = new JTextField(30);
        packageWeightInMenu = new JTextField(30);
        packageExpectedDeliveryDateInMenu = new JTextField(30);
        packageAuthorInMenu = new JTextField(30);
        packageExpiryDateInMenu = new JTextField(30);
        packageEnvironmentalHandlingFeeInMenu = new JTextField(30);
        //adding to userEntryPanel
        userEntryPanel.add(packageTypeLabel);
        userEntryPanel.add(packageTypeComboBox);
        userEntryPanel.add(packageNameLabel);
        userEntryPanel.add(packageNameInMenu);
        userEntryPanel.add(packageNotesLabel);
        userEntryPanel.add(packageNotesInMenu);
        userEntryPanel.add(packagePriceLabel);
        userEntryPanel.add(packagePriceInMenu);
        userEntryPanel.add(packageWeightLabel);
        userEntryPanel.add(packageWeightInMenu);
        userEntryPanel.add(packageExpectedDeliveryDateLabel);
        userEntryPanel.add(packageExpectedDeliveryDateInMenu);
        userEntryPanel.add(packageAuthorLabel);
        userEntryPanel.add(packageAuthorInMenu);
        userEntryPanel.add(packageExpiryDateLabel);
        userEntryPanel.add(packageExpiryDateInMenu);
        userEntryPanel.add(packageEnvironmentalHandlingFeeLabel);
        userEntryPanel.add(packageEnvironmentalHandlingFeeInMenu);

        //set up bottom buttons in menu panel section
        createPackageInMenuBtn = new JButton("Create");
        createPackageInMenuBtn.setPreferredSize(new Dimension(300, 50));
        createPackageInMenuBtn.addActionListener(this);
        cancelPackageInMenuBtn = new JButton("Cancel");
        cancelPackageInMenuBtn.setPreferredSize(new Dimension(300, 50));
        cancelPackageInMenuBtn.addActionListener(this);
        bottomButtonsInMenuPanel.add(createPackageInMenuBtn);
        bottomButtonsInMenuPanel.add(cancelPackageInMenuBtn);

        //finalize by adding sections together in box layout
        BoxLayout boxlayout = new BoxLayout(mainPanelInMenu, BoxLayout.Y_AXIS);
        mainPanelInMenu.add(topTitleInMenuPanel);
        mainPanelInMenu.add(userEntryPanel);
        mainPanelInMenu.add(bottomButtonsInMenuPanel);
        mainPanelInMenu.setLayout(boxlayout);
        newPackageMenuFrame.add(mainPanelInMenu);
        newPackageMenuFrame.setVisible(true);
    }



    /**
     * //when create button is clicked we do the checking...
     * //assuming user chooses a package type, use this selection to limit checking.
     * //eg. if package type == book, then dont check for expiry date or environmental handling fee, only author name
     * @return
     */
    public Package inputVerification(){
        //if all is successful return true and create an object and add to array, if any are false, send message with what is false.
        String inputVerificationMessage = "";

        //check if the following values have been entered and if they are the correct type :
        // notes can be empty:
        notesInMenu = packageNotesInMenu.getText().trim();

        if (packageTypeComboBox.getSelectedItem().toString() == "Book" ||
                packageTypeComboBox.getSelectedItem().toString() == "Perishable" ||
                packageTypeComboBox.getSelectedItem().toString() == "Electronic") {
            inputVerificationMessage += "";
            packageTypeInMenu = packageTypeComboBox.getSelectedItem().toString();
        } else {
            inputVerificationMessage += "Incorrect entry for package type. \n";
        }
        if (packageNameInMenu.getText().trim().isBlank() ||
                packageNameInMenu.getText().trim() == null) {
            inputVerificationMessage += "Incorrect entry. Name is empty. \n";
        } else {
            nameInMenu = packageNameInMenu.getText().trim();
        }
        if (packagePriceInMenu.getText().trim().isEmpty() ||
                Double.parseDouble(packagePriceInMenu.getText().trim()) < 0 ||
                packagePriceInMenu.getText().trim() == null) {
            inputVerificationMessage += "Incorrect entry. price is less than 0. \n";
        } else {
            priceInDollarInMenu = Double.parseDouble(packagePriceInMenu.getText().trim());
        }
        if (packageWeightInMenu.getText().trim().isEmpty() ||
                Double.parseDouble(packageWeightInMenu.getText().trim()) < 0 ||
                packageWeightInMenu.getText().trim() == null) {
            inputVerificationMessage += "Incorrect entry. weight is less than 0. \n";
        } else {
            weightInKgInMenu = Double.parseDouble(packageWeightInMenu.getText().trim());
        }
        if(packageExpectedDeliveryDateInMenu.getText().trim().isEmpty()) {
            inputVerificationMessage += "Incorrect entry. Expected delivery date is empty. \n";
        } else {
            DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            expectedDeliveryDateInMenu = LocalDateTime.parse(packageExpectedDeliveryDateInMenu.getText().trim(), aFormatter);
        }
        if(packageTypeComboBox.getSelectedItem().toString() == "Book"){
            if (packageAuthorInMenu.getText().trim().isBlank()||
                    packageAuthorInMenu.getText().trim() == null) {
                inputVerificationMessage += "Incorrect entry. Author name is empty. \n";
            } else {
                authorNameInMenu = packageAuthorInMenu.getText().trim();
            }
        }
        if(packageTypeComboBox.getSelectedItem().toString() == "Perishable"){
            if(packageExpiryDateInMenu.getText().trim().isEmpty()) {
                inputVerificationMessage += "Incorrect entry. Expiry date is empty. \n";
            } else {
                DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                expiryDateInMenu = LocalDateTime.parse(packageExpiryDateInMenu.getText().trim(), aFormatter);
            }
        }
        if(packageTypeComboBox.getSelectedItem().toString() == "Electronic"){
            if (packageEnvironmentalHandlingFeeInMenu.getText().trim().isEmpty() ||
                    Double.parseDouble(packageEnvironmentalHandlingFeeInMenu.getText().trim()) < 0 ||
                    packageEnvironmentalHandlingFeeInMenu.getText().trim() == null) {
                inputVerificationMessage += "Incorrect entry. Environmental Handling Fee is less than 0. \n";
            } else {
                environmentalHandlingFeeInMenu = Double.parseDouble(packageEnvironmentalHandlingFeeInMenu.getText().trim());
            }
        }
        if(inputVerificationMessage.isEmpty()){
            JOptionPane.showMessageDialog(null, "All correct entries, a package named " + nameInMenu + " has been created.");
            if (packageTypeComboBox.getSelectedItem().toString() == "Book" ||
                    packageTypeComboBox.getSelectedItem().toString() == "Perishable" ||
                    packageTypeComboBox.getSelectedItem().toString() == "Electronic") {
                packInfo = packageFactory.getInstance(packageTypeComboBox.getSelectedItem().toString());
            }
            if(packageTypeInMenu == "Book"){
                packInfo = new Book(nameInMenu, notesInMenu,
                        priceInDollarInMenu, weightInKgInMenu, false, expectedDeliveryDateInMenu, authorNameInMenu);
                return packInfo;
            } else if(packageTypeInMenu == "Perishable"){
                packInfo = new Perishable(nameInMenu, notesInMenu,
                        priceInDollarInMenu, weightInKgInMenu, false, expectedDeliveryDateInMenu, expiryDateInMenu);
                return packInfo;
            } else if(packageTypeInMenu == "Electronic") {
                packInfo = new Electronic(nameInMenu, notesInMenu,
                        priceInDollarInMenu, weightInKgInMenu, false, expectedDeliveryDateInMenu, environmentalHandlingFeeInMenu);
                return packInfo;
            }

        } else {
            inputVerificationMessage += "Note that 'notes' can be empty" ;
            JOptionPane.showMessageDialog(null, inputVerificationMessage);
        }

        return null;

    }


    /**
     * // The following method will grab the packageListInGUI,
     * // and create an array list of panels representing each object in the list.
     * // then these panels will be added to the JScrollPane
     */
    public void generateJScrollPackages(){

        //remove all contents from old version of JScrollPane. the updated contents will be shown after this.
        accumulatedPackagePanel.removeAll();
        boolean packagesExist = false;

        //System.out.println(packageDeliveriesTrackerControl.getGetPackageListInMenu().size());
        //iterate through and add in packages - erase old contents
        JPanel[] jPanelsTopSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] jPanelsMidSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] jPanelsBottomSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] combinedSections = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JLabel topSection; //contains package number and type
        JLabel midSection; //contains name, notes, price, weight, expected delivery date, and unique field
        JLabel bottomSection; //contained delivered? and remove button.
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        deliveredCB = new JCheckBox("Delivered (Check the box to set package to delivered):");

        for(int i=0; i < packageDeliveriesTrackerControl.getPackageListFromMenu().size(); i++){
            topSection = new JLabel("Package : #" + (i+1) + " - " + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getType()));
            topSection.setFont(new Font("Times New Roman", Font.BOLD, 20));
            jPanelsTopSection[i] = new JPanel();
            jPanelsTopSection[i].setLayout(new BoxLayout(jPanelsTopSection[i], BoxLayout.Y_AXIS));
            jPanelsTopSection[i].add( topSection);
            jPanelsTopSection[i].add(new JSeparator());
            jPanelsTopSection[i].setBorder(new EmptyBorder(10, 10, -10, 0));

            //NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
            //double formattedPrice = Double.parseDouble(numberFormatter.format((packageDeliveriesTrackerControl.getGetPackageListInMenu().get(i).getPriceInDollar())));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getExpectedDeliveryDate()).format(dateTimeFormatter);
            nameLabel = new JLabel("Name: " + packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getName());
            notesLabel = new JLabel("Notes: " + packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getNotes());
            priceLabel = new JLabel("Price: $" + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getPriceInDollar()));
            weightLabel = new JLabel("Weight: " + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getWeightInKg()));
            expectedDeliveryDateLabel = new JLabel("Expected Delivery Date: " + formattedDateTime);
            //adding unique field based on package type
            if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                authorLabel = new JLabel("Author: " + ((Book) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getAuthorName());
            } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                expiryDateLabel = new JLabel("Expiry Date: " + ((Perishable) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getExpiryDate().format(dateTimeFormatter));
            } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                environmentalHandlingFeeLabel = new JLabel("Env. Handling Fee: " + ((Electronic) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getEnvironmentalHandlingFee());
            }

            //midSection.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            jPanelsMidSection[i] = new JPanel();
            jPanelsMidSection[i].setLayout(new BoxLayout(jPanelsMidSection[i], BoxLayout.Y_AXIS));
            jPanelsMidSection[i].setBorder(new EmptyBorder(0, 10, 10, 0));
            jPanelsMidSection[i].add(nameLabel);
            jPanelsMidSection[i].add( notesLabel);
            jPanelsMidSection[i].add( priceLabel);
            jPanelsMidSection[i].add( weightLabel);
            jPanelsMidSection[i].add( expectedDeliveryDateLabel);
            //only add the label to the panel based on package type. eg: if book, then add author.
            if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                jPanelsMidSection[i].add(authorLabel);
            } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                jPanelsMidSection[i].add(expiryDateLabel);
            } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                jPanelsMidSection[i].add(environmentalHandlingFeeLabel);
            }

            deliveredCB = new JCheckBox("Delivered (Check to set package to delivered. Checked means delivered):");
            if((packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).isDelivered()) == true){
                deliveredCB.setSelected(true);
            } else {
                deliveredCB.setSelected(false);
            }
            deliveredCB.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            deliveredCB.addActionListener(this);
            deliveredCB.setActionCommand(i + " deliver");

            removePackageBtn = new JButton("Remove this Package");
            removePackageBtn.setPreferredSize(new Dimension(100, 50));
            removePackageBtn.addActionListener(this);
            //action command
            removePackageBtn.setActionCommand(i + " remove");


            jPanelsBottomSection[i] = new JPanel();
            jPanelsBottomSection[i].setLayout(new BoxLayout(jPanelsBottomSection[i], BoxLayout.Y_AXIS));
            jPanelsBottomSection[i].add( deliveredCB);
            jPanelsBottomSection[i].add( removePackageBtn);

            combinedSections[i] = new JPanel(new GridLayout(3,0));
            combinedSections[i].add(jPanelsTopSection[i]);
            combinedSections[i].add(jPanelsMidSection[i]);
            combinedSections[i].add(jPanelsBottomSection[i]);
            combinedSections[i].setBorder(blackLine);

            packagesExist = true;

        }

        //System.out.println(combinedSections.length);
        for(int i=0; i < combinedSections.length; i++){

            accumulatedPackagePanel.setLayout(new BoxLayout(accumulatedPackagePanel, BoxLayout.Y_AXIS));
            accumulatedPackagePanel.add(combinedSections[i]);

            accumulatedPackagePanel.add(new JSeparator());

        }

        //if there are no packages packages....
        if(packagesExist == false){
            System.out.println("No packages in the list in console");
            JLabel noPackagesLabel = new JLabel("No packages in the list");
            JPanel noPackagesPanel = new JPanel();
            noPackagesPanel.add(noPackagesLabel);
            accumulatedPackagePanel.add(noPackagesPanel);
        }
        packagesScrollPane.setViewportView(accumulatedPackagePanel);

        middleJScrollPanel.add(packagesScrollPane);

    }

    /**
     * // The following method will grab the packageListInGUI,
     * // and create an array list of panels representing each object in the list.
     * // then these panels will be added to the JScrollPane
     * This method does it for overdue packages and interacts with the control in order to do so.
     */
    public void generateJScrollPackagesOverdue(){

        //remove all contents from old version of JScrollPane. the updated contents will be shown after this.
        accumulatedPackagePanel.removeAll();

        //see console version of output
        //packageDeliveriesTrackerControl.listOverduePackages(packageDeliveriesTrackerControl.getPackageListFromMenu());

        LocalDateTime now = LocalDateTime.now();

        int overduePackageCount = packageDeliveriesTrackerControl.getOverduePackagesCount(/*packageDeliveriesTrackerControl.getPackageListFromMenu()*/);
        System.out.println(overduePackageCount);
        boolean overduePackageExists = false;

        //System.out.println(packageDeliveriesTrackerControl.getGetPackageListInMenu().size());
        //iterate through and add in packages - erase old contents
        JPanel[] jPanelsTopSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] jPanelsMidSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] jPanelsBottomSection = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JPanel[] combinedSections = new JPanel[packageDeliveriesTrackerControl.getPackageListFromMenu().size()];
        JLabel topSection; //contains package number and type
        JLabel midSection; //contains name, notes, price, weight, expected delivery date, and unique field
        JLabel bottomSection; //contained delivered? and remove button.
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        deliveredCB = new JCheckBox("Delivered (Check the box to set package to delivered):");

        for(int i=0; i < packageDeliveriesTrackerControl.getPackageListFromMenu().size(); i++){

            //if this particular package is not delivered and if the packages delivery date is before .now(), add to JScrollBar.
            if(!packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).isDelivered()
                    && now.isAfter(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getExpectedDeliveryDate())){
                topSection = new JLabel("Package : #" + (i+1) + " - " + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getType()));
                topSection.setFont(new Font("Times New Roman", Font.BOLD, 20));
                jPanelsTopSection[i] = new JPanel();
                jPanelsTopSection[i].setLayout(new BoxLayout(jPanelsTopSection[i], BoxLayout.Y_AXIS));
                jPanelsTopSection[i].add( topSection);
                jPanelsTopSection[i].add(new JSeparator());
                jPanelsTopSection[i].setBorder(new EmptyBorder(10, 10, -10, 0));

                //NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
                //double formattedPrice = Double.parseDouble(numberFormatter.format((packageDeliveriesTrackerControl.getGetPackageListInMenu().get(i).getPriceInDollar())));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDateTime = (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getExpectedDeliveryDate()).format(dateTimeFormatter);
                nameLabel = new JLabel("Name: " + packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getName());
                notesLabel = new JLabel("Notes: " + packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getNotes());
                priceLabel = new JLabel("Price: $" + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getPriceInDollar()));
                weightLabel = new JLabel("Weight: " + (packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).getWeightInKg()));
                expectedDeliveryDateLabel = new JLabel("Expected Delivery Date: " + formattedDateTime);
                //adding unique field based on package type
                if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                    authorLabel = new JLabel("Author: " + ((Book) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getAuthorName());
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                    expiryDateLabel = new JLabel("Expiry Date: " + ((Perishable) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getExpiryDate().format(dateTimeFormatter));
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                    environmentalHandlingFeeLabel = new JLabel("Env. Handling Fee: " + ((Electronic) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getEnvironmentalHandlingFee());
                }

                //midSection.setFont(new Font("Times New Roman", Font.PLAIN, 14));
                jPanelsMidSection[i] = new JPanel();
                jPanelsMidSection[i].setLayout(new BoxLayout(jPanelsMidSection[i], BoxLayout.Y_AXIS));
                jPanelsMidSection[i].setBorder(new EmptyBorder(0, 10, 10, 0));
                jPanelsMidSection[i].add(nameLabel);
                jPanelsMidSection[i].add( notesLabel);
                jPanelsMidSection[i].add( priceLabel);
                jPanelsMidSection[i].add( weightLabel);
                jPanelsMidSection[i].add( expectedDeliveryDateLabel);
                //only add the label to the panel based on package type. eg: if book, then add author.
                if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                    jPanelsMidSection[i].add(authorLabel);
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                    jPanelsMidSection[i].add(expiryDateLabel);
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                    jPanelsMidSection[i].add(environmentalHandlingFeeLabel);
                }


                deliveredCB = new JCheckBox("Delivered (Check to set package to delivered. Checked means delivered):");
                if((packageDeliveriesTrackerControl.getPackageListFromMenu().get(i).isDelivered()) == true){
                    deliveredCB.setSelected(true);
                } else {
                    deliveredCB.setSelected(false);
                }

                deliveredCB.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                deliveredCB.setActionCommand(i + " deliver");

                removePackageBtn = new JButton("Remove this Package");
                removePackageBtn.setPreferredSize(new Dimension(100, 50));
                removePackageBtn.addActionListener(this);
                //action command
                removePackageBtn.setActionCommand(i + " remove");


                jPanelsBottomSection[i] = new JPanel();
                jPanelsBottomSection[i].setLayout(new BoxLayout(jPanelsBottomSection[i], BoxLayout.Y_AXIS));
                jPanelsBottomSection[i].add( deliveredCB);
                jPanelsBottomSection[i].add( removePackageBtn);

                combinedSections[i] = new JPanel(new GridLayout(3,0));
                combinedSections[i].add(jPanelsTopSection[i]);
                combinedSections[i].add(jPanelsMidSection[i]);
                combinedSections[i].add(jPanelsBottomSection[i]);
                combinedSections[i].setBorder(blackLine);

                overduePackageExists = true;

            }


        }

        //iterate thorugh each package that is overdue/upcoming and add to list
        for(int i=0; i < overduePackageCount; i++){

            accumulatedPackagePanel.setLayout(new BoxLayout(accumulatedPackagePanel, BoxLayout.Y_AXIS));
            accumulatedPackagePanel.add(combinedSections[i]);

            accumulatedPackagePanel.add(new JSeparator());

        }

        //if there are no overdue packages....
        if(overduePackageExists == false){
            System.out.println("No overdue packages in console.");
            JLabel noOverduePackagesLabel = new JLabel("No overdue packages");
            JPanel noOverduePackagesPanel = new JPanel();
            noOverduePackagesPanel.add(noOverduePackagesLabel);
            accumulatedPackagePanel.add(noOverduePackagesPanel);
        }


        packagesScrollPane.setViewportView(accumulatedPackagePanel);

        middleJScrollPanel.add(packagesScrollPane);

    }


    /**
     * // The following method will grab the packageListInGUI,
     * // and create an array list of panels representing each object in the list.
     * // then these panels will be added to the JScrollPane
     * This method does it for upcoming packages and interacts with the control in order to do so.
     *
     * In Assignment 3, this method was not functioning accordingly, and was giving a null exception error.
     *
     * In Assignement 4, this method functions accordingly and now uses an array, upcomingPackageListInGUI,
     * and no longer compares dates in this method. The date comparison is done in the control class,
     * and the resulting array sent from control is printed to the jscrollpane.
     */
    public void generateJScrollPackagesUpcoming(){

        //remove all contents from old version of JScrollPane. the updated contents will be shown after this.
        accumulatedPackagePanel.removeAll();

        ArrayList<Package> upcomingPackageListInGUI = packageDeliveriesTrackerControl.getUpcomingPackagesList();
        boolean upcomingPackageExists = false;

        //System.out.println(packageDeliveriesTrackerControl.getGetPackageListInMenu().size());
        //iterate through and add in packages - erase old contents
        JPanel[] jPanelsTopSection = new JPanel[upcomingPackageListInGUI.size()];
        JPanel[] jPanelsMidSection = new JPanel[upcomingPackageListInGUI.size()];
        JPanel[] jPanelsBottomSection = new JPanel[upcomingPackageListInGUI.size()];
        JPanel[] combinedSections = new JPanel[upcomingPackageListInGUI.size()];
        JLabel topSection; //contains package number and type
        JLabel midSection; //contains name, notes, price, weight, expected delivery date, and unique field
        JLabel bottomSection; //contained delivered? and remove button.
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        deliveredCB = new JCheckBox("Delivered (Check the box to set package to delivered):");

        for(int i=0; i < upcomingPackageListInGUI.size(); i++){

                topSection = new JLabel("Package : #" + (i+1) + " - " + (upcomingPackageListInGUI.get(i).getType()));
                topSection.setFont(new Font("Times New Roman", Font.BOLD, 20));
                jPanelsTopSection[i] = new JPanel();
                jPanelsTopSection[i].setLayout(new BoxLayout(jPanelsTopSection[i], BoxLayout.Y_AXIS));
                jPanelsTopSection[i].add( topSection);
                jPanelsTopSection[i].add(new JSeparator());
                jPanelsTopSection[i].setBorder(new EmptyBorder(10, 10, -10, 0));

                //NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
                //double formattedPrice = Double.parseDouble(numberFormatter.format((packageDeliveriesTrackerControl.getGetPackageListInMenu().get(i).getPriceInDollar())));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDateTime = (upcomingPackageListInGUI.get(i).getExpectedDeliveryDate()).format(dateTimeFormatter);
                nameLabel = new JLabel("Name: " + upcomingPackageListInGUI.get(i).getName());
                notesLabel = new JLabel("Notes: " + upcomingPackageListInGUI.get(i).getNotes());
                priceLabel = new JLabel("Price: $" + (upcomingPackageListInGUI.get(i).getPriceInDollar()));
                weightLabel = new JLabel("Weight: " + (upcomingPackageListInGUI.get(i).getWeightInKg()));
                expectedDeliveryDateLabel = new JLabel("Expected Delivery Date: " + formattedDateTime);
                //adding unique field based on package type
                if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                    authorLabel = new JLabel("Author: " + ((Book) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getAuthorName());
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                    expiryDateLabel = new JLabel("Expiry Date: " + ((Perishable) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getExpiryDate().format(dateTimeFormatter));
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                    environmentalHandlingFeeLabel = new JLabel("Env. Handling Fee: " + ((Electronic) packageDeliveriesTrackerControl.getPackageListFromMenu().get(i)).getEnvironmentalHandlingFee());
                }

                //midSection.setFont(new Font("Times New Roman", Font.PLAIN, 14));
                jPanelsMidSection[i] = new JPanel();
                jPanelsMidSection[i].setLayout(new BoxLayout(jPanelsMidSection[i], BoxLayout.Y_AXIS));
                jPanelsMidSection[i].setBorder(new EmptyBorder(0, 10, 10, 0));
                jPanelsMidSection[i].add(nameLabel);
                jPanelsMidSection[i].add( notesLabel);
                jPanelsMidSection[i].add( priceLabel);
                jPanelsMidSection[i].add( weightLabel);
                jPanelsMidSection[i].add( expectedDeliveryDateLabel);
                //only add the label to the panel based on package type. eg: if book, then add author.
                if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Book){
                    jPanelsMidSection[i].add(authorLabel);
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Perishable){
                    jPanelsMidSection[i].add(expiryDateLabel);
                } else if(packageDeliveriesTrackerControl.getPackageListFromMenu().get(i) instanceof Electronic){
                    jPanelsMidSection[i].add(environmentalHandlingFeeLabel);
                }

                deliveredCB = new JCheckBox("Delivered (Check to set package to delivered. Checked means delivered):");
                if((upcomingPackageListInGUI.get(i).isDelivered()) == true){
                    deliveredCB.setSelected(true);
                } else {
                    deliveredCB.setSelected(false);
                }

                deliveredCB.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                deliveredCB.setActionCommand(i + " deliver");

                removePackageBtn = new JButton("Remove this Package");
                removePackageBtn.setPreferredSize(new Dimension(100, 50));
                removePackageBtn.addActionListener(this);
                //action command
                removePackageBtn.setActionCommand(i + " remove");


                jPanelsBottomSection[i] = new JPanel();
                jPanelsBottomSection[i].setLayout(new BoxLayout(jPanelsBottomSection[i], BoxLayout.Y_AXIS));
                jPanelsBottomSection[i].add( deliveredCB);
                jPanelsBottomSection[i].add( removePackageBtn);

                combinedSections[i] = new JPanel(new GridLayout(3,0));
                combinedSections[i].add(jPanelsTopSection[i]);
                combinedSections[i].add(jPanelsMidSection[i]);
                combinedSections[i].add(jPanelsBottomSection[i]);
                combinedSections[i].setBorder(blackLine);

                upcomingPackageExists = true;

        }

        //if there are no overdue packages....
        if(upcomingPackageExists == false){
            System.out.println("No upcoming packages in console.");
            JLabel noUpcomingPackagesLabel = new JLabel("No upcoming packages");
            JPanel noUpcomingPackagesPanel = new JPanel();
            noUpcomingPackagesPanel.add(noUpcomingPackagesLabel);
            accumulatedPackagePanel.add(noUpcomingPackagesPanel);
        }

        if(upcomingPackageExists == true && combinedSections != null && accumulatedPackagePanel != null){
            //iterate thorugh each package that is overdue/upcoming and add to list
            for(int i=0; i < upcomingPackageListInGUI.size(); i++){

                accumulatedPackagePanel.setLayout(new BoxLayout(accumulatedPackagePanel, BoxLayout.Y_AXIS));
                accumulatedPackagePanel.add(combinedSections[i]);

                accumulatedPackagePanel.add(new JSeparator());

            }
        }

        packagesScrollPane.setViewportView(accumulatedPackagePanel);

        middleJScrollPanel.add(packagesScrollPane);

    }



}
