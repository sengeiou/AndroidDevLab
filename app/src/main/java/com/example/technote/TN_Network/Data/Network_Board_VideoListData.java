package com.example.technote.TN_Network.Data;

public class Network_Board_VideoListData {
    private String id;
    private String video_url;
    private String thumbnail_url;

    private String title;
    private String subject;

    public String getId(){return id;}
    public String getVideo_url() {
        return video_url;
    }
    public String getThumbnail_url(){
        return thumbnail_url;
    }
    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }


    public void setId(String id){this.id=id;}
    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
    public void setThumbnail_url(String thumbnail_url){
        this.thumbnail_url = thumbnail_url;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setSubject(String subject){this.subject = subject;}
}
