//SnaxStation POS
package com.mycompany.snaxstation;
import java.util.Scanner;

public class SnaxStation {
    public static void main(String[] args) {
        
        //Instatinates the classes for it to work
        Scanner scanner = new Scanner(System.in);
        
        MenuCatalog catalog = new MenuCatalog();
        
        Transaction transactionEngine = new Transaction();
        
        // Pre-register a sample customer array to simulate saved VIP accounts
        Customer[] customerDatabase = new Customer[10];
        int customerCount = 0;
        
        // Adding a default test customer
        customerDatabase[customerCount++] = new Customer("Niel", "pass123", 250); // Pastry Connoisseur tier

        Customer loggedInCustomer = null; 
        boolean running = true;
        
        System.out.println("=========================================");
        System.out.println("    WELCOME TO SNAXSTATION POS SYSTEM    ");
        System.out.println("        Fresh Bakes, Fast Checkout       ");
        System.out.println("=========================================");
        System.out.println("");

        while (running) {
            
            System.out.println("=============== Main Menu ===============");
            System.out.println("");
            System.out.println("1. View Menu & Live Stock");
            System.out.println("2. Customer Login / Register");
            System.out.println("3. Place an Order (Cart Checkout)");
            System.out.println("4. View End-of-Day Summary [Admin Only]");
            System.out.println("5. Reward Tiers");
            System.out.println("6. Exit System");
            System.out.print("Select an option (1-5): ");
            int choice = scanner.nextInt();
            System.out.println("");
            System.out.println("=========================================");
            

            scanner.nextLine(); // Removes buffer

            switch (choice) {
                
                case 1:
                    //displays/prints the menu
                    catalog.displayMenu();
                    break;

                case 2:
                    //Login Credentials
                    System.out.print("Enter customer name: ");
                    String nameInput = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String passInput = scanner.nextLine();

                    // Search for existing customer in array
                    Customer activeCustomer = null;
                    
                    for (int i = 0; i < customerCount; i++) {
                        
                        if (customerDatabase[i].name.equalsIgnoreCase(nameInput) && 
                            customerDatabase[i].password.equals(passInput)) {
                            activeCustomer = customerDatabase[i];
                            break;
                        }
                    }

                    if (activeCustomer != null) {
                        
                        loggedInCustomer = activeCustomer;
                        System.out.println("Welcome back, " + loggedInCustomer.name + "!");
                        System.out.println("Current Tier: " + loggedInCustomer.tier + " | Points: " + loggedInCustomer.points);
                        
                    } else {
                        
                        // Register new customer if space permits
                        if (customerCount < customerDatabase.length) {
                            
                            Customer newCust = new Customer(nameInput, passInput, 0);
                            customerDatabase[customerCount++] = newCust;
                            loggedInCustomer = newCust;
                            System.out.println("New account created for " + nameInput);
                            System.out.println("Welcome to SnaxStation! Mr/Ms. " + nameInput);
                            
                        } else {
                            
                            System.out.println("Customer database is full!");
                            System.out.println("Approach an admin or staff.");
                        }
                    }
                    break;

                case 3:
                    
                    // Passes the logged-in customer (or null if guest) and scanner to the transaction engine
                    transactionEngine.processOrder(loggedInCustomer, catalog, scanner);
                    break;

                case 4:
                    
                    // Restricted Admin Check for EOD Summary
                    System.out.print("Enter admin password: ");
                    String adminPass = scanner.nextLine();
                    
                    if (adminPass.equals("admin123")) {
                        KitchenRouter.displayEODSummary();
                        
                    } else {
                        
                        System.out.println("Access denied! Incorrect admin password.");
                    }
                    
                    break;
                    
                case 5:
                    
                    System.out.println("");
                    System.out.println("-----------------------------------------");
                    System.out.println("         CUSTOMER LOYALTY TIERS          ");
                    System.out.println("-----------------------------------------");
                    System.out.println(" * Regular            : 0+ pts  (0% Off)");
                    System.out.println(" * Pastry Connoisseur : 100+ pts (5% Off)");
                    System.out.println(" * SweetTooth Mania   : 250+ pts (10% Off)");
                    System.out.println("=========================================\n");
                    break;

                case 6:
                    
                    //prompts to enter the admin password to exit the program
                    System.out.print("Enter admin password to exit system: ");
                    String exitPass = scanner.nextLine();
                    
                    if (exitPass.equals("snaxstation2026")) {
                        running = false;
                        System.out.println("\nThank you for using Snaxstation POS. Goodbye!");
                    } else {
                        System.out.println("\nIncorrect admin password! Exit cancelled.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
        scanner.close();
    }
}