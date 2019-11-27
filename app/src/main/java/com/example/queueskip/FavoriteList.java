package com.example.queueskip;

import java.util.ArrayList;

public class FavoriteList {
    String userId;
    ArrayList<Items> itemsList;

    public FavoriteList(String userId, ArrayList<Items> itemsList) {
        this.userId = userId;
        this.itemsList = itemsList;
    }


    public FavoriteList() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ArrayList<Items> itemsList) {
        this.itemsList = itemsList;
    }
}
