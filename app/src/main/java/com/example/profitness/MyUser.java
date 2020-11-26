package com.example.profitness;

public class MyUser {
    private String firstName, lastName, email, password,phone,birthDAy;
    private int gsex ;
    public MyUser(String fn, String ln, String email, String pass, String phone, String birthDAy, int sg){
        this.birthDAy =birthDAy;
        this.email = email;
        this.firstName = fn;
        this.lastName = ln;
        this.password = pass;
        this.phone = phone;
        this.gsex= sg; // m = 0 f =1
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public String getBirthDAy() {
        return birthDAy;
    }
    public int getGsex(){
        return gsex;
    }
}
