package com.example.queueskip;


public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private int trans1;
    private int trans2;
    private int trans3;
//    private int trans4;
//    private int trans5;

//    private ArrayList<Trans> transList; //?
//    private ArrayList<Double> transList;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public int getTrans1() {
        return trans1;
    }

    public void setTrans1(int trans1) {
        this.trans1 = trans1;
    }

    public int getTrans2() {
        return trans2;
    }

    public void setTrans2(int trans2) {
        this.trans2 = trans2;
    }

    public int getTrans3() {
        return trans3;
    }

    public void setTrans3(int trans3) {
        this.trans3 = trans3;
    }

    public  String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
