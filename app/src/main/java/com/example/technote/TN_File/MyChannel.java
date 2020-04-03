package com.example.technote.TN_File;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "music_channel")
public class MyChannel {
    @Attribute(name = "ch")
    String ch;

    @Attribute(name = "ip")
    String ip;

    @Attribute(name = "port")
    String port;

    @Attribute(name = "ch_no")
    String ch_no;

    @Attribute(name = "apid")
    String apid;

    @Attribute(name = "ppid")
    String ppid;

    @Attribute(name = "ast")
    String ast;

    @Attribute(name = "opid")
    String opid;

    @Attribute(name = "ctype")
    String ctype;

    @Attribute(name = "title")
    String title;

    @Attribute(name = "image")
    String image;

    @Attribute(name = "ca_id")
    String ca_id;

    @Attribute(name = "ca_pid")
    String ca_pid;

    @Attribute(name = "channelUri")
    String channelUri;



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
