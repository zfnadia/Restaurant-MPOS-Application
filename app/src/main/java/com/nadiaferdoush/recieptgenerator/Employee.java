package com.nadiaferdoush.recieptgenerator;

public class Employee {
    public int id;
    private String name;
    private String birthDate;
    private String address;
    private String email;
    private String password;
    private String phone;
    private int salary;
    private int type;

    public Employee(String name, String birthDate, String address, String email, String password, String phone, int salary, int type) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getSalary() {
        return salary;
    }

    public int getType() {
        return type;
    }
}
