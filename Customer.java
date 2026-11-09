package com.mycompany.snaxstation;

public class Customer {
    
    String name;
    String password;
    int points;
    String tier;

    // Constructor using unique parameter names 
    public Customer(String cName, String cPassword, int cPoints) {
        
        name = cName;
        password = cPassword;
        points = cPoints;
        evaluateTier();
        
    }

    // Method to calculate and update customer tier based on points
    public void evaluateTier() {
        
        
        
        if (points >= 250) {
            
            tier = "SweetTooth Mania";
            
        } else if (points >= 100) {
            
            tier = "Pastry Connoisseur";
            
        } else {
            
            tier = "Regular";
        }
    }
    
    

    // Method to add points (1 point per P50 spent)
    public void addSpentAmount(double amountSpent) {
        int earnedPoints = (int) (amountSpent / 50);
        points += earnedPoints;
        evaluateTier();
        System.out.println("" + name + " earned " + earnedPoints + " points! Total Points: " + points);
    }

    // Method to get discount based on tier
    public double getDiscountRate() {
        
        if (tier.equals("SweetTooth Mania")) {
            
            return 0.10; // 10% discount
            
        } else if (tier.equals("Pastry Connoisseur")) {
            
            return 0.05; // 5% discount
            
        } else {
            
            return 0.00; // Regular tier
            
        }
    }
}