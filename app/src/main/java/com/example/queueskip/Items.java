package com.example.queueskip;

import java.io.Serializable;

public class Items implements Serializable {
    private String name;
    private String price;
    private String expire;
    private String photo;

    public Items() {
    }

    public Items(String name, String price, String expire,String photo) {
        this.name = name;
        this.price = price;
        this.expire = expire;
        this.photo = photo;
    }

    //getters


    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getExpire() {
        return expire;
    }

    public String getPhoto() {
        return photo;
    }

    //setters

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}