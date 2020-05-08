package com.yunjeapark.technote.TN_Network.Data;

public class Network_Board_ImageListData {

    private String id;
    private String photo_url_1;

    private String title;
    private String price;
    private String subject;

    public String getId(){return id;}
    public String getPhoto_url_1() {
        return photo_url_1;
    }

    public String getTitle() {
        return title;
    }
    public String getPrice() {
        return price;
    }
    public String getSubject() {
        return subject;
    }


    public void setId(String id){this.id=id;}
    public void setPhoto_url_1(String photo_url_1) {
        this.photo_url_1 = photo_url_1;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setSubject(String subject){this.subject = subject;}

}