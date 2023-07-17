package com.example.shoesstore.Models;

public class ShoeDetail {
    private int shoeDetailId;
    private int shoeId;
    private boolean isSold;
    private boolean isDelete;
    private Shoe Shoe;

    public ShoeDetail(int shoeDetailId, int shoeId, boolean isSold, boolean isDelete, com.example.shoesstore.Models.Shoe shoe) {
        this.shoeDetailId = shoeDetailId;
        this.shoeId = shoeId;
        this.isSold = isSold;
        this.isDelete = isDelete;
        Shoe = shoe;
    }

    public int getShoeDetailId() {
        return shoeDetailId;
    }

    public void setShoeDetailId(int shoeDetailId) {
        this.shoeDetailId = shoeDetailId;
    }

    public int getShoeId() {
        return shoeId;
    }

    public void setShoeId(int shoeId) {
        this.shoeId = shoeId;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public com.example.shoesstore.Models.Shoe getShoe() {
        return Shoe;
    }

    public void setShoe(com.example.shoesstore.Models.Shoe shoe) {
        Shoe = shoe;
    }
}
