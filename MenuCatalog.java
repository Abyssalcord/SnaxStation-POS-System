package com.mycompany.snaxstation;

public class MenuCatalog {
    
    // arrays for SnaxStation products, prices, and stock
    String[] pastryNames = {"Choco Chunk Cookie", "Chompcolate Brownies", "Red Velvet Crinkles", "Brookies", "Banana Cake","Sliced CheeseCake" };
    
    double[] pastryPrices = {65.0, 85.0, 50.0, 75.0, 150.0, 85.0};
    
    int[] stockLevels = {10, 8, 15, 6, 3, 5}; // Live stock tracking

    // Method to display the menu
    public void displayMenu() {
        System.out.println("\n=========================================");
        System.out.println("        SNAXSTATION LIVE MENU            ");
        System.out.println("=========================================");
        
        for (int i = 0; i < pastryNames.length; i++) {
            System.out.printf("[%d] %-20s | P%-6.2f | Stock: %d\n", 
                (i + 1), 
                pastryNames[i], 
                pastryPrices[i], 
                stockLevels[i]
            );
        }
        
        System.out.println("=========================================\n");
        
    }
    
    
    // Method to check and deduct stock when an item is ordered
    public boolean updateStock(int itemIndex, int quantity) {
        
        if (itemIndex >= 0 && itemIndex < stockLevels.length) {
            
            if (stockLevels[itemIndex] >= quantity) {
                
                stockLevels[itemIndex] -= quantity;
                return true; // Successful deduction
                
            } else {
                
                System.out.println("Sorry! " + pastryNames[itemIndex] + " only has " + stockLevels[itemIndex] + " left.");
                return false; // Not enough stock
            }
        }
        return false;
    }
}