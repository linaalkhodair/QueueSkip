package com.example.queueskip.Database.DataSource;

import com.example.queueskip.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {
    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }
    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource){
        if(instance==null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public List<Cart> getCartItemss() {
        return iCartDataSource.getCartItemss();
    } //NEW NEW NEW ADDED ADDED

    @Override
    public Flowable<List<Cart>> getCartItemByID(String cartItemId) {
        return iCartDataSource.getCartItemByID(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();

    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);

    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);

    }

    @Override
    public int getamountItemByID(String id) {
        return iCartDataSource.getamountItemByID( id );
    }

    @Override
    public void updateAmount(int namount, String cartID) {
        iCartDataSource.updateAmount(namount, cartID );

    }
}