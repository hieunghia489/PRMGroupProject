package com.example.shoesstore.Models;
public class BuyDetail {
    private int buyDetailId;
    private int buyId;
    private int shoeId;
    private int shoeDetailId;
    private double shoePriceWhenBuy;
    private boolean isDelete;
    private BuyHistory BuyHistory;
    private Shoe Shoe;
    private ShoeDetail ShoeDetail;

    public BuyDetail(int buyDetailId, int buyId, int shoeId, int shoeDetailId, double shoePriceWhenBuy, boolean isDelete) {
        this.buyDetailId = buyDetailId;
        this.buyId = buyId;
        this.shoeId = shoeId;
        this.shoeDetailId = shoeDetailId;
        this.shoePriceWhenBuy = shoePriceWhenBuy;
        this.isDelete = isDelete;
    }

    public BuyDetail(int buyDetailId, int buyId, int shoeId, int shoeDetailId, double shoePriceWhenBuy, boolean isDelete,  Shoe shoe) {
        this.buyDetailId = buyDetailId;
        this.buyId = buyId;
        this.shoeId = shoeId;
        this.shoeDetailId = shoeDetailId;
        this.shoePriceWhenBuy = shoePriceWhenBuy;
        this.isDelete = isDelete;
        Shoe = shoe;
    }

    public int getBuyDetailId() {
        return buyDetailId;
    }

    public void setBuyDetailId(int buyDetailId) {
        this.buyDetailId = buyDetailId;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyId) {
        this.buyId = buyId;
    }

    public int getShoeId() {
        return shoeId;
    }

    public void setShoeId(int shoeId) {
        this.shoeId = shoeId;
    }

    public int getShoeDetailId() {
        return shoeDetailId;
    }

    public void setShoeDetailId(int shoeDetailId) {
        this.shoeDetailId = shoeDetailId;
    }

    public double getShoePriceWhenBuy() {
        return shoePriceWhenBuy;
    }

    public void setShoePriceWhenBuy(double shoePriceWhenBuy) {
        this.shoePriceWhenBuy = shoePriceWhenBuy;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public com.example.shoesstore.Models.BuyHistory getBuyHistory() {
        return BuyHistory;
    }

    public void setBuyHistory(com.example.shoesstore.Models.BuyHistory buyHistory) {
        BuyHistory = buyHistory;
    }

    public com.example.shoesstore.Models.Shoe getShoe() {
        return Shoe;
    }

    public void setShoe(com.example.shoesstore.Models.Shoe shoe) {
        Shoe = shoe;
    }

    public com.example.shoesstore.Models.ShoeDetail getShoeDetail() {
        return ShoeDetail;
    }

    public void setShoeDetail(com.example.shoesstore.Models.ShoeDetail shoeDetail) {
        ShoeDetail = shoeDetail;
    }
}
