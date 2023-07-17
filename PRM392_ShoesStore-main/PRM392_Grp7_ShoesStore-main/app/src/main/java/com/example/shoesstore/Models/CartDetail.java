package com.example.shoesstore.Models;

public class CartDetail {
    private int cartDetailId;
    private int cartId;
    private int shoeId;
    private int quantity;
    private double totalPrice;
    private Shoe Shoe;
    private Cart Cart;

    public CartDetail(int cartDetailId, int cartId, int shoeId, int quantity, double totalPrice) {
        this.cartDetailId = cartDetailId;
        this.cartId = cartId;
        this.shoeId = shoeId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public CartDetail(int cartDetailId, int cartId, int shoeId, int quantity, double totalPrice, com.example.shoesstore.Models.Shoe shoe, com.example.shoesstore.Models.Cart cart) {
        this.cartDetailId = cartDetailId;
        this.cartId = cartId;
        this.shoeId = shoeId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        Shoe = shoe;
        Cart = cart;
    }

    public int getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getShoeId() {
        return shoeId;
    }

    public void setShoeId(int shoeId) {
        this.shoeId = shoeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public com.example.shoesstore.Models.Shoe getShoe() {
        return Shoe;
    }

    public void setShoe(com.example.shoesstore.Models.Shoe shoe) {
        Shoe = shoe;
    }

    public com.example.shoesstore.Models.Cart getCart() {
        return Cart;
    }

    public void setCart(com.example.shoesstore.Models.Cart cart) {
        Cart = cart;
    }
}
