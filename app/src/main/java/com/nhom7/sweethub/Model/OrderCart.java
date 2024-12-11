package com.nhom7.sweethub.Model;

import java.util.ArrayList;

public class OrderCart {
    private ArrayList<String> name;
    private ArrayList<String> price;
    private ArrayList<String> quantity;
    private String address;

    // Constructor
    public OrderCart(ArrayList<String> name, ArrayList<String> price, ArrayList<String> quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderCart(ArrayList<String> name, ArrayList<String> price, ArrayList<String> quantity, String address) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
