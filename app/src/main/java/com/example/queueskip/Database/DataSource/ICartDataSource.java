package com.example.queueskip.Database.DataSource;

import com.example.queueskip.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItems();
    List<Cart> getCartItemss(); //NEW NEW NEW ADDED
    Flowable<List<Cart>> getCartItemByID(int cartItemId);
    int countCartItems();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}

