package com.yunjeapark.technote.tn_database.Data;

public class CallLogData {

    private String type;
    private String name;
    private String date;
    private String phoneNumber;

    public String getName(){return name;}
    public String getPhoneNumber(){ return phoneNumber; }
    public String getDate() {
        return date;
    }
    public String getType(){return  type;}

    public void setName(String name){this.name=name;}
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
    public void setDate(String date) {
        this.date = date;
    }
    public void setType(String type){this.type = type; }

}