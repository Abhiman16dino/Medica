package com.example.medica.Model;

public class Orders {

    String Products, address, date, name, phone, state, pName, orderID, time;
    int totalAmont;

    public Orders() {
    }

    public Orders(String products, String address, String date, String name, String phone, String state, int totalAmont, String pName, String orderID, String time) {
        Products = products;
        this.address = address;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.totalAmont = totalAmont;
        this.pName = pName;
        this.orderID = orderID;
        this.time = time;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getProducts() {
        return Products;
    }

    public void setProducts(String products) {
        Products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTotalAmont() {
        return totalAmont;
    }

    public void setTotalAmont(int totalAmont) {
        this.totalAmont = totalAmont;
    }
}
