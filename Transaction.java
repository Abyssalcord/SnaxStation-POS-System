package com.mycompany.snaxstation;
import java.util.Scanner;

public class Transaction {
    
    // Method to process multi-item cart ordering and checkout
    public void processOrder(Customer customer, MenuCatalog catalog, Scanner scanner) {
        boolean ordering = true;
        
        // Temporary fixed-size arrays to hold the current cart items
        int[] cartItems = new int[10];     
        int[] cartQuantities = new int[10]; 
        int cartCount = 0;                 

        System.out.println("\n--- STARTING NEW ORDER ---");

        // 1. Loop to allow adding multiple items to the cart with cumulative stock check
        while (ordering && cartCount < cartItems.length) {
            catalog.displayMenu();
            System.out.print("Enter product number to add to cart (or 0 to finish ordering): ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break; 
            }

            int itemIndex = choice - 1;

            if (itemIndex >= 0 && itemIndex < catalog.pastryNames.length) {
                System.out.print("Enter quantity for " + catalog.pastryNames[itemIndex] + ": ");
                int qty = scanner.nextInt();

                if (qty <= 0) {
                    System.out.println("Invalid quantity. Must be at least 1.\n");
                    continue;
                }

                // Calculate how much of this specific item is ALREADY sitting in the cart
                int alreadyInCart = 0;
                for (int i = 0; i < cartCount; i++) {
                    if (cartItems[i] == itemIndex) {
                        alreadyInCart += cartQuantities[i];
                    }
                }

                // Verify cumulative total against live stock
                int totalRequested = alreadyInCart + qty;

                if (catalog.stockLevels[itemIndex] >= totalRequested) {
                    
                    boolean foundInCart = false;
                    for (int i = 0; i < cartCount; i++) {
                        if (cartItems[i] == itemIndex) {
                            cartQuantities[i] += qty;
                            foundInCart = true;
                            break;
                        }
                    }

                    if (!foundInCart) {
                        cartItems[cartCount] = itemIndex;
                        cartQuantities[cartCount] = qty;
                        cartCount++;
                    }

                    System.out.println("Added to cart successfully!\n");
                    
                } else {
                    int availableRemaining = catalog.stockLevels[itemIndex] - alreadyInCart;
                    System.out.println("Not enough stock! You can only add up to " + Math.max(0, availableRemaining) + " more of this item.\n");
                }
            } else {
                System.out.println("Invalid product number. Try again.\n");
            }

            System.out.print("Do you want to add another item? (1 for Yes, 0 for Checkout): ");
            int continueChoice = scanner.nextInt();
            if (continueChoice == 0) {
                ordering = false;
            }
        }

        // If cart is empty, cancel transaction
        if (cartCount == 0) {
            System.out.println("No items in the cart. Order cancelled.");
            return;
        }

        // 2. Prompt for Dine-In or Take-Out
        System.out.println("\n--- CHOOSE ORDER TYPE ---");
        System.out.println("1. Dine-In");
        System.out.println("2. Take-Out");
        System.out.print("Select order type (1 or 2): ");
        int orderTypeChoice = scanner.nextInt();
        
        String orderType = "Take-Out"; // default fallback
        if (orderTypeChoice == 1) {
            orderType = "Dine-In";
        }

        // 3. Calculate Subtotal across all cart items
        double subtotal = 0.0;
        for (int i = 0; i < cartCount; i++) {
            int idx = cartItems[i];
            int q = cartQuantities[i];
            subtotal += catalog.pastryPrices[idx] * q;
        }

        // 4. Apply Tier Discount if customer is logged in
        double discountRate = 0.0;
        if (customer != null) {
            discountRate = customer.getDiscountRate();
        }
        double discountAmount = subtotal * discountRate;
        double finalTotal = subtotal - discountAmount;

        // 5. Display Cart Summary & Bill Breakdown
        System.out.println("\n========= SNAXSTATION CART SUMMARY =========");
        System.out.println("Order Type: " + orderType);
        for (int i = 0; i < cartCount; i++) {
            int idx = cartItems[i];
            System.out.println("- " + catalog.pastryNames[idx] + " x " + cartQuantities[i] + " (P" + (catalog.pastryPrices[idx] * cartQuantities[i]) + ")");
        }
        System.out.println("------------------------------------------");
        System.out.println("Subtotal: P" + subtotal);
        if (discountRate > 0) {
            System.out.println("Tier Discount (" + (int)(discountRate * 100) + "%): -P" + discountAmount);
        }
        System.out.println("FINAL TOTAL DUE: P" + finalTotal);
        System.out.println("===========================================");

        // 6. Cash Payment & Change Calculation
        System.out.print("Enter cash given by customer (P): ");
        double cashGiven = scanner.nextDouble();

        if (cashGiven < finalTotal) {
            System.out.println("Insufficient cash! Transaction cancelled.");
            return;
        }

        double change = cashGiven - finalTotal;

        // 7. Deduct permanent stock and send each item to the kitchen with order type
        for (int i = 0; i < cartCount; i++) {
            int idx = cartItems[i];
            int q = cartQuantities[i];
            catalog.stockLevels[idx] -= q; 
            KitchenRouter.sendToKitchen(catalog.pastryNames[idx], q, orderType);
        }

        // 8. Print Official Receipt
        System.out.println("\n========= OFFICIAL RECEIPT =========");
        System.out.println("Order Type : " + orderType);
        System.out.println("Cash Received: P" + cashGiven);
        System.out.println("CHANGE DUE : P" + change);
        System.out.println("====================================\n");

        // 9. Award Points & Update Customer Tier
        if (customer != null) {
            customer.addSpentAmount(finalTotal);
        }

        // 10. Record Sale for End-of-Day Summary
        KitchenRouter.recordSale(finalTotal);
    }
}