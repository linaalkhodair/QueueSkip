package com.example.queueskip.Database.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.queueskip.Database.ModelDB.Cart;

@Database(entities = {Cart.class},version = 1,exportSchema = false)
public  abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();

    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, "Cart")//if we want in memory builder  ithink we can add it here
                    .allowMainThreadQueries().build();
        return instance;


    }
}