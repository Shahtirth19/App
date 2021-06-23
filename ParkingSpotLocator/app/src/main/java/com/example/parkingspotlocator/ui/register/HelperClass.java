package com.example.parkingspotlocator.ui.register;

public class HelperClass {
    String username,password,Fullname,sex,phno,address,eid;

    public HelperClass() {
    }

    public HelperClass(String username, String password, String fullname, String sex, String phno, String address, String eid) {
        this.username = username;
        this.password = password;
        Fullname = fullname;
        this.sex = sex;
        this.phno = phno;
        this.address = address;
        this.eid = eid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
}
