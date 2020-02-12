package com.example.technote.Data;

public class GoogleMapGPSData {
    private byte[] value;
    private byte hemisphere1;
    private byte hemisphere2;
    private byte degree1;
    private byte degree2;

    private byte a;
    private byte b;
    private byte c;
    private byte d;

    private byte e;
    private byte f;
    private byte g;
    private byte h;

    private boolean dataSend;

    public byte getHemisphere1(){return hemisphere1;}
    public byte getHemisphere2(){ return hemisphere2; }
    public byte getDegree1() { return degree1; }
    public byte getDegree2(){return degree2;}
    public byte getA(){return a;}
    public byte getB(){return b;}
    public byte getC(){return c;}
    public byte getD(){return d;}
    public byte getE(){return e;}
    public byte getF(){return f;}
    public byte getG(){return g;}
    public byte getH(){return h;}
    public byte[] getValue(){return value;}
    public boolean getDataSend(){return dataSend;}

    public void setHemisphere1(byte hemisphere1){this.hemisphere1=hemisphere1;}
    public void setHemisphere2(byte hemisphere2){ this.hemisphere2 = hemisphere2; }
    public void setDegree1(byte degree1) { this.degree1 = degree1; }
    public void setDegree2(byte degree2){this.degree2 = degree2; }
    public void setA(byte a){this.a = a; }
    public void setB(byte b){this.b = b; }
    public void setC(byte c){this.c = c; }
    public void setD(byte d){this.d = d; }
    public void setE(byte e){this.e = e; }
    public void setF(byte f){this.f = f; }
    public void setG(byte g){this.g = g; }
    public void setH(byte h){this.h = h; }
    public void setValue(byte[] value){this.value = value; }
    public void setDataSend(boolean dataSend){this.dataSend = dataSend;}

}
