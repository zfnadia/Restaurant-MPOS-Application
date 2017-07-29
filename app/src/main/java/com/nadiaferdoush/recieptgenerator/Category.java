package com.nadiaFerdoush.recieptgenerator;

public class Category {
    private String name;
    private String time;
    public int id;

    public Category(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
