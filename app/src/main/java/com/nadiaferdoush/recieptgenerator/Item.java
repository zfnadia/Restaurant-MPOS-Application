package com.nadiaferdoush.recieptgenerator;

public class Item {
    private String name;
    private float price;
    private String description;
    public int id;
    public int count;

    public Item(String name, float price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
