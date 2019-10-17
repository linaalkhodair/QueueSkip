package com.example.queueskip.Database.Local;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.queueskip.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart")
    List<Cart> getCartItemss(); //NEW NEW NEW ADDED

    @Query("SELECT * FROM CART WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemByID(String cartItemId);

    @Query("SELECT Count(*)  FROM CART")
    int countCartItems();
    @Query("SELECT amount FROM Cart WHERE id=:cartItemId")
    int getamountItemByID(String cartItemId);

    @Query( "UPDATE Cart SET amount= :namount WHERE id=:cartID " )
    void updateAmount(int namount,String cartID );

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart cart);



}
