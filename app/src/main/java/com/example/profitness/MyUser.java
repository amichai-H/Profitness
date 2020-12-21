package com.example.profitness;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyUser {
    DBshort mydb = new DBshort();
    FirebaseAuth mAuth;
    FirebaseUser user;
    private String firstName="", lastName="", email="", password="",phone="",birthDAy="",coach="";
    private int gsex=0, training=0;
    public MyUser(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
    public MyUser(String fn, String ln, String email, String pass, String phone, String birthDAy, int sg,int isTraining,String coach){
        this.coach = coach;
        this.birthDAy =birthDAy;
        this.email = email;
        this.firstName = fn;
        this.lastName = ln;
        this.password = pass;
        this.phone = phone;
        this.gsex= sg; // m = 0 f =1
        this.training = isTraining;
    }
    public MyUser(String fn, String ln, String phone, String birthDAy, int sg,int isTraining,String coach){
        this.coach = coach;
        this.birthDAy =birthDAy;
        this.firstName = fn;
        this.lastName = ln;
        this.phone = phone;
        this.gsex= sg; // m = 0 f =1
        this.training = isTraining;
    }
    public void init(DocumentSnapshot doc){
        setFirstName((String) doc.get("first"));
        setLastName((String) doc.get("last"));
        setPhone((String) doc.get("phone"));
        setBirthDAy((String) doc.get("born"));
        setEmail((String) doc.get("email"));
        setTraining(Math.toIntExact(doc.getLong("trainer")));
        setCoach((String) doc.get("coach"));
        setGsex(Math.toIntExact(doc.getLong("sex")));
    }
    public void init(){
        mydb.getUser(user.getUid(),(doc)->{
            setFirstName((String) doc.get("first"));
            setLastName((String) doc.get("last"));
            setPhone((String) doc.get("phone"));
            setBirthDAy((String) doc.get("born"));
            setEmail((String) doc.get("email"));
            setTraining(Math.toIntExact(doc.getLong("trainer")));
            setCoach((String) doc.get("coach"));
            setGsex(Math.toIntExact(doc.getLong("sex")));
        });

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

    public int getTraining() {
        return training;
    }

    public String getCoach() {
        return coach;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDAy(String birthDAy) {
        this.birthDAy = birthDAy;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public void setGsex(int gsex) {
        this.gsex = gsex;
    }

    public void setTraining(int training) {
        this.training = training;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isTrainee(){
        return training == 0;
    }
}
