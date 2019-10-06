package com.example.queueskip.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Cart")
public class Cart {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public int id;

    @ColumnInfo (name="name")
    public String name;

    @ColumnInfo (name = "Price")
    public int Price;

    @ColumnInfo (name = "amount")
    public int amount;

    @ColumnInfo (name="link")
    public String link;





}
