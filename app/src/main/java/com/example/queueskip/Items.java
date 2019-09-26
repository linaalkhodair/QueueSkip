package com.example.queueskip;

public class Items {
    String ItemId;
    String ItemName;
    String ItemPrice;
    String ItemExpire;
   // Bitmap ItemQR;

    public Items(){

    }

    public Items(String itemId, String ItemName, String ItemPrice, String ItemExpire) {
        ItemId = itemId;
        this.ItemName=ItemName;
        this.ItemPrice=ItemPrice;
        this.ItemExpire=ItemExpire;
       // this.ItemQR=ItemQR;
    }

    public String getItemId() {
        return ItemId;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemExpire() {
        return ItemExpire;
    }

  //  public Bitmap getItemQR() {
    //    return ItemQR;
    //}
}
