package com.example.queueskip.Database.Local;

import com.example.queueskip.Database.DataSource.ICartDataSource;
import com.example.queueskip.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO){
        if(instance==null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemByID(String cartItemId) {
        return cartDAO.getCartItemByID(cartItemId);
    }
    @Override
    public int getamountItemByID(String id){
        return  cartDAO.getamountItemByID( id );
    }

@Override
public void updateAmount(int namount,String cartID ){
        cartDAO.updateAmount(namount,cartID  );
}
    @Override
    public List<Cart> getCartItemss() {
        return cartDAO.getCartItemss();
    } //NEW NEW NEW //ADDED



    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);

    }
}