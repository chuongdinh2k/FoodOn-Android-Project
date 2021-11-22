package com.example.btl_fooddelivery.Model;

public class User {
    private String Fname, EmailID, Lname,Role,Mobile,PostCode,City,Address;
    public User(){
    }



    public User(String fname, String emailID, String lname, String role, String mobile, String postCode, String city,String address) {
        Fname = fname;
        EmailID = emailID;
        Lname = lname;
        Role = role;
        Mobile = mobile;
        PostCode = postCode;
        City=city;
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }
}
