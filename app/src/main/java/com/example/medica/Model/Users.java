package com.example.medica.Model;

public class Users {
    String Name;
    String Email;
    String Password;
    String Phone;
    String Pincode;
    String Address;
    String Image;

    public Users() {
    }

    public Users(String name, String email, String password, String phone, String pincode, String address, String image) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        Pincode = pincode;
        Address = address;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
