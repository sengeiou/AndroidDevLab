package com.example.technote.TN_File;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class MyChannel {
    @Path("Channel")
    @Attribute(name = "ch")
    private String ch;

    @Path("Channel")
    @Attribute(name = "ip")
    private String ip;

    @Path("Channel")
    @Attribute(name = "port")
    private String port;

    @Path("Channel")
    @Attribute(name = "ch_no")
    private String ch_no;

    @Path("Channel")
    @Attribute(name = "apid")
    private String apid;

    @Path("Channel")
    @Attribute(name = "ppid")
    private String ppid;

    @Path("Channel")
    @Attribute(name = "ast")
    private String ast;

    @Path("Channel")
    @Attribute(name = "opid")
    private String opid;

    @Path("Channel")
    @Attribute(name = "ctype")
    private String ctype;

    @Path("Channel")
    @Attribute(name = "title")
    private String title;

    @Path("Channel")
    @Attribute(name = "image")
    private String image;

    @Path("Channel")
    @Attribute(name = "ca_id")
    private String ca_id;

    @Path("Channel")
    @Attribute(name = "ca_pid")
    private String ca_pid;

    @Path("Channel")
    @Attribute(name = "channelUri")
    private String channelUri;

    public String getCh(){
        return ch;
    }
    public String getIp() {
         return ip;
    }
    public String getPort() {
        return port;
    }
    public String getCh_no() {
        return ch_no;
    }
    public String getApid() {
        return apid;
    }
    public String getPpid() {
        return ppid;
    }
    public String getAst() {
        return ast;
    }
    public String getOpid() {
        return opid;
    }
    public String getCtype() {
        return ctype;
    }
    public String getTitle() {
        return title;
    }
    public String getImage() {
        return image;
    }
    public String getCa_id() {
        return ca_id;
    }
    public String getCa_pid() {
        return ca_pid;
    }
    public String getChannelUri() {
        return channelUri;
    }

    public void setCh(String ch){
        this.ch = ch;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public void setCh_no(String ch_no) {
        this.ch_no = ch_no;
    }
    public void setApid(String apid) {
        this.apid = apid;
    }
    public void setPpid(String ppid) {
        this.ppid = ppid;
    }
    public void setAst(String ast) {
        this.ast = ast;
    }
    public void setOpid(String opid) {
        this.opid = opid;
    }
    public void setCtype(String ctype) {
        this.ctype = ctype;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setCa_id(String ca_id) {
        this.ca_id = ca_id;
    }
    public void setCa_pid(String ca_pid) {
        this.ca_pid = ca_pid;
    }
    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

}
