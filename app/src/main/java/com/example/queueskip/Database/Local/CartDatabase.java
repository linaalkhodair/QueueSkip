package com.example.queueskip.Database.Local;

import android.app.Activity;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.queueskip.Database.ModelDB.Cart;

@Database(entities = {Cart.class},version = 1,exportSchema = false)
public  abstract class CartDatabase extends RoomDatabase {

   // private static Context context;


    public abstract CartDAO cartDAO();

    private static CartDatabase instance;

   // public CartDatabase(Context context){
     //   this.context=context;
    //}

    public static CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CartDatabase.class).allowMainThreadQueries().build();
           // instance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, "Cart")//if we want in memory builder  ithink we can add it here
             //       .allowMainThreadQueries().build();
        }
        return instance;


    }
}