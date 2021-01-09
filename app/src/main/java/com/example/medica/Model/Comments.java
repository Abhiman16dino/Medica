package com.example.medica.Model;

public class Comments {

    String Com , Name, Phone , Img, Cid;

    public Comments() {
    }

    public Comments(String com, String name, String phone, String img, String cid) {
        Com = com;
        Name = name;
        Phone = phone;
        Img  = img;
        Cid = cid;

    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getCom() {
        return Com;
    }

    public void setCom(String com) {
        Com = com;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
