package com.example.queueskip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.widget.Button;

import com.example.queueskip.Adapter.CartAdapter;
import com.example.queueskip.Database.ModelDB.Cart;
import com.example.queueskip.utliz.Common;

import java.util.List;


public class cartActivity extends AppCompatActivity  {
RecyclerView recycler_cart;
Button btn_place_order;
    CompositeDisposable compositionDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );

        compositionDisposable = new CompositeDisposable();
        recycler_cart = (RecyclerView) findViewById( R.id.recycler_cart );
        recycler_cart.setLayoutManager( new LinearLayoutManager( this ) );
        recycler_cart.setHasFixedSize( true );


        btn_place_order = (Button) findViewById( R.id.btn_place_order );
        loadCartItems(); //it was a comment!!!!!
    }


    @Override
    protected void onStop() {
        compositionDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositionDisposable.clear();
        super.onDestroy();
    }

    private void loadCartItems() {

        compositionDisposable.add(

              Common.cartRepository.getCartItems().observeOn(AndroidSchedulers.mainThread())
                      .subscribeOn(Schedulers.io() )
                      .subscribe( new Consumer<List<Cart>>() {
                          @Override
                          public void accept(List<Cart> carts) {
                              displayCartItem(carts);
                          }
                      } )



        );


    }
    private void displayCartItem(List<Cart> carts){
        CartAdapter cartAdapter=new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }
    }


