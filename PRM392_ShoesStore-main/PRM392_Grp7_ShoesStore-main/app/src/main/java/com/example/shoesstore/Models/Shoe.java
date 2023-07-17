package com.example.shoesstore.Models;

public class Shoe {
    private int shoeId;
    private String shoeName;
    private String imgName;
    private String shoeDescription;
    private double shoePrimaryPrice;
    private int quantity;
    private boolean isDelete;

    public Shoe(int shoeId, String shoeName, String imgName, String shoeDescription, double shoePrimaryPrice, int quantity, boolean isDelete) {
        this.shoeId = shoeId;
        this.shoeName = shoeName;
        this.imgName = imgName;
        this.shoeDescription = shoeDescription;
        this.shoePrimaryPrice = shoePrimaryPrice;
        this.quantity = quantity;
        this.isDelete = isDelete;
    }

    public Shoe(String shoeName, String imgName, String shoeDescription, double shoePrimaryPrice, int quantity, boolean isDelete) {
        this.shoeName = shoeName;
        this.imgName = imgName;
        this.shoeDescription = shoeDescription;
        this.shoePrimaryPrice = shoePrimaryPrice;
        this.quantity = quantity;
        this.isDelete = isDelete;
    }

    public int getShoeId() {
        return shoeId;
    }

    public void setShoeId(int shoeId) {
        this.shoeId = shoeId;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getShoeDescription() {
        return shoeDescription;
    }

    public void setShoeDescription(String shoeDescription) {
        this.shoeDescription = shoeDescription;
    }

    public double getShoePrimaryPrice() {
        return shoePrimaryPrice;
    }

    public void setShoePrimaryPrice(double shoePrimaryPrice) {
        this.shoePrimaryPrice = shoePrimaryPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}

