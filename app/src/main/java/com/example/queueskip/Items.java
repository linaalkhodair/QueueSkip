package com.example.queueskip;

import java.io.Serializable;

public class Items implements Serializable {
    private String name;
    private String price;
    private String expire;
    private String photo;
    private String id;


    public Items() {
    }

    public Items(String id,String name, String price, String expire, String photo) {
        this.name = name;
        this.price = price;
        this.expire = expire;
        this.photo = photo;

        this.id = id;
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

    public String getId(){return id;}

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

    public void swtId(String id){this.id=id;}

}