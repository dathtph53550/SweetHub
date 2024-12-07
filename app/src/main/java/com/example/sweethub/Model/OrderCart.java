package com.example.sweethub.Model;

import java.util.ArrayList;

public class OrderCart {
    private ArrayList<String> name;
    private ArrayList<String> price;
    private ArrayList<String> quantity;

    // Constructor
    public OrderCart(ArrayList<String> name, ArrayList<String> price, ArrayList<String> quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }




    // Getters
    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getPrice() {
        return price;
    }

    public ArrayList<String> getQuantity() {
        return quantity;
    }

    // Setters
    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public void setPrice(ArrayList<String> price) {
        this.price = price;
    }

    public void setQuantity(ArrayList<String> quantity) {
        this.quantity = quantity;
    }


}
