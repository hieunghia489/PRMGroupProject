package com.example.shoesstore.Adapter;

public class Listdataproduct {
    String name, description,price;
    int size, quantity;
    int image;

    public Listdataproduct(String name, String description, int size, int quantity, String price, int image) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }
}
