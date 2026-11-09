package com.mycompany.snaxstation;

public class KitchenRouter {
    // End-of-Day (EOD) sales tracker
    private static double totalDailySales = 0.0;

    // Method for Instant Kitchen Routing (KOT) with Ticket ID and Order Type
    public static void sendToKitchen(String itemName, int quantity, String orderType) {
        
        // Generates a random 4-digit ticket number between 1000 and 9999
        int ticketNumber = (int) (Math.random() * 9000) + 1000;

        System.out.println("\n[ KITCHEN ORDER TICKET SENT ]");
        System.out.println("-> Ticket ID   : #" + ticketNumber);
        System.out.println("-> Order Type  : " + orderType);
        System.out.println("-> Prep Item   : " + itemName);
        System.out.println("-> Quantity    : " + quantity + " pieces");
        System.out.println("-> Status      : Order preparing!");
    }

    // Method to record sales for EOD summary
    public static void recordSale(double amount) {
        totalDailySales += amount;
    }

    // Method to display End-of-Day Summary
    public static void displayEODSummary() {
        System.out.println("\n=========================================");
        System.out.println("       SNAXSTATION END-OF-DAY SUMMARY    ");
        System.out.println("=========================================");
        System.out.println("Total Revenue Made Today: P" + totalDailySales);
        System.out.println("Status: All transactions logged successfully.");
        System.out.println("=========================================");
    }
}