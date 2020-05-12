package com.example.finalexam.utils;

/**
 * Created by mshehab on 5/6/18.
 */

public class Gift {
    String name;
    int price;
    String id;

    public Gift() {
    }

    public Gift(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Gift(String name, int price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
