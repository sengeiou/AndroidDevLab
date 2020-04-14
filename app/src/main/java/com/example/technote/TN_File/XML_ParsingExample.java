package com.example.technote.TN_File;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.example.technote.R;
import com.tickaroo.tikxml.TikXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okio.Buffer;

public class XML_ParsingExample extends AppCompatActivity implements View.OnClickListener {
    private MyChannel xml_musicChannel_data;
    private Music_Channel music_channel;
    private MyHandler myHandler = new MyHandler();
    private AQuery aq;
    private ArrayList<MyChannel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_xml_parsing_example);
        initView();
        /*
        try {
            InputStream is = getResources().getAssets().open("music_channel.xml");
            //파서 인스턴스화
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element=doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("MyChannel");
            Log.d("getNode", "nList.toString : " + String.valueOf(nList.getLength()));
            Log.d("getNode","nList.item(0).getNodeValue() : " +nList.item(0).getAttributes().getNamedItem("title").getNodeValue());
            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Element element2 = (Element) node;
                    Log.d("getNode", "node.getAttributes().getNamedItem(title).getNodeValue() : " + String.valueOf(i) + " " +  node.getAttributes().getNamedItem("title").getNodeValue());
                    xml_musicChannel_data = new MyChannel();
                    xml_musicChannel_data.setCh(node.getAttributes().getNamedItem("ch").getNodeValue());
                    xml_musicChannel_data.setIp(node.getAttributes().getNamedItem("ip").getNodeValue());
                    xml_musicChannel_data.setPort(node.getAttributes().getNamedItem("port").getNodeValue());
                    xml_musicChannel_data.setCh_no(node.getAttributes().getNamedItem("ch_no").getNodeValue());
                    xml_musicChannel_data.setApid(node.getAttributes().getNamedItem("apid").getNodeValue());
                    xml_musicChannel_data.setPpid(node.getAttributes().getNamedItem("ppid").getNodeValue());
                    xml_musicChannel_data.setAst(node.getAttributes().getNamedItem("ast").getNodeValue());
                    xml_musicChannel_data.setOpid(node.getAttributes().getNamedItem("opid").getNodeValue());
                    xml_musicChannel_data.setCtype(node.getAttributes().getNamedItem("ctype").getNodeValue());
                    xml_musicChannel_data.setTitle(node.getAttributes().getNamedItem("title").getNodeValue());
                    xml_musicChannel_data.setImage(node.getAttributes().getNamedItem("image").getNodeValue());
                    xml_musicChannel_data.setCa_id(node.getAttributes().getNamedItem("ca_id").getNodeValue());
                    xml_musicChannel_data.setChannelUri(node.getAttributes().getNamedItem("channelUri").getNodeValue());
                    arrayList.add(xml_musicChannel_data);
                    Log.d("InsertData","i : " + String.valueOf(i));
                }
            }
            Log.d("InsertData","textView.setText");
        } catch (Exception e) {
            Log.d("getNode","Exception e : " + e.getMessage());
        }
         */
        try {
            Log.d("XMLParsing", "Before xml parsing");

            TikXml tikXml = new TikXml.Builder()
                    .exceptionOnUnreadXml(true)
                    .writeDefaultXmlDeclaration(true)
                    .build();

            InputStream is = getResources().getAssets().open("music_channel.xml");
            music_channel = tikXml.read(new Buffer().readFrom(is),Music_Channel.class);

            Log.d("XMLParsing", "After xml parsing");

        } catch (IOException e) {
            Log.d("XMLParsing","IOException : " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == aq.id(R.id.bt_ch_1).getView()){
            myHandler.sendEmptyMessage(0);
        }else if(v == aq.id(R.id.bt_ch_2).getView()){
            myHandler.sendEmptyMessage(1);
        }else if(v == aq.id(R.id.bt_ch_3).getView()){
            myHandler.sendEmptyMessage(2);
        }else if(v == aq.id(R.id.bt_ch_4).getView()){
            myHandler.sendEmptyMessage(3);
        }else if(v == aq.id(R.id.bt_ch_5).getView()){
            myHandler.sendEmptyMessage(4);
        }else if(v == aq.id(R.id.bt_ch_6).getView()){
            myHandler.sendEmptyMessage(5);
        }else if(v == aq.id(R.id.bt_ch_7).getView()){
            myHandler.sendEmptyMessage(6);
        }else if(v == aq.id(R.id.bt_ch_8).getView()){
            myHandler.sendEmptyMessage(7);
        }else if(v == aq.id(R.id.bt_ch_9).getView()){
            myHandler.sendEmptyMessage(8);
        }else if(v == aq.id(R.id.bt_ch_10).getView()){
            myHandler.sendEmptyMessage(9);
        }else if(v == aq.id(R.id.bt_ch_11).getView()){
            myHandler.sendEmptyMessage(10);
        }else if(v == aq.id(R.id.bt_ch_12).getView()){
            myHandler.sendEmptyMessage(11);
        }else if(v == aq.id(R.id.bt_ch_13).getView()){
            myHandler.sendEmptyMessage(12);
        }else if(v == aq.id(R.id.bt_ch_14).getView()){
            myHandler.sendEmptyMessage(13);
        }else if(v == aq.id(R.id.bt_ch_15).getView()){
            myHandler.sendEmptyMessage(14);
        }else if(v == aq.id(R.id.bt_ch_16).getView()){
            myHandler.sendEmptyMessage(15);
        }else if(v == aq.id(R.id.bt_ch_17).getView()){
            myHandler.sendEmptyMessage(16);
        }else if(v == aq.id(R.id.bt_ch_18).getView()){
            myHandler.sendEmptyMessage(17);
        }else if(v == aq.id(R.id.bt_ch_19).getView()){
            myHandler.sendEmptyMessage(18);
        }else if(v == aq.id(R.id.bt_ch_20).getView()){
            myHandler.sendEmptyMessage(19);
        }

    }
    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            /*
            textView.setText("ch : " + arrayList.get(msg.what).getCh() +"\nip : "+arrayList.get(msg.what).getIp() +
                    "\nport : " + arrayList.get(msg.what).getPort() + "\nch_no : " + arrayList.get(msg.what).getCh_no() +
                    "\napid : " + arrayList.get(msg.what).getApid() + "\nppid : " + arrayList.get(msg.what).getPpid() +
                    "\nast : " + arrayList.get(msg.what).getAst() + "\nopid : " + arrayList.get(msg.what).getOpid() +
                    "\nctype : " + arrayList.get(msg.what).getCtype() + "\ntitle : "//+arrayList.get(msg.what).getTitle()
                     +
                    "\nimage : " + arrayList.get(msg.what).getImage() + "\nca_id : " + arrayList.get(msg.what).getCa_id() +
                    "\nca_pid : " + arrayList.get(msg.what).getCa_pid() +"\nchannelUri : " + arrayList.get(msg.what).getChannelUri());
             */

            aq.id(R.id.tv_xml_result).text("ch : " + music_channel.channel.get(msg.what).ch +"\nip : "+music_channel.channel.get(msg.what).ip +
                    "\nport : " + music_channel.channel.get(msg.what).port + "\nch_no : " + music_channel.channel.get(msg.what).ch_no  +
                    "\napid : " + music_channel.channel.get(msg.what).apid + "\nppid : " + music_channel.channel.get(msg.what).ppid +
                    "\nast : " + music_channel.channel.get(msg.what).ast + "\nopid : " + music_channel.channel.get(msg.what).opid +
                    "\nctype : " + music_channel.channel.get(msg.what).ctype  + "\ntitle : " + music_channel.channel.get(msg.what).title +
                    "\nca_id : " + music_channel.channel.get(msg.what).ca_id + "\nca_pid : " + music_channel.channel.get(msg.what).ca_pid +
                    "\nchannelUri : " + music_channel.channel.get(msg.what).channelUri).enabled(true).visible();
        }
    }
    public void myDocumentBuilderFactory(){
        try {

            Log.d("XMLParsing", "Before xml parsing");
            InputStream is = getResources().getAssets().open("music_channel.xml");

            //파서 인스턴스화
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("MyChannel");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Element element2 = (Element) node;

                    xml_musicChannel_data = new MyChannel();
                    xml_musicChannel_data.setCh(node.getAttributes().getNamedItem("ch").getNodeValue());
                    xml_musicChannel_data.setIp(node.getAttributes().getNamedItem("ip").getNodeValue());
                    xml_musicChannel_data.setPort(node.getAttributes().getNamedItem("port").getNodeValue());
                    xml_musicChannel_data.setCh_no(node.getAttributes().getNamedItem("ch_no").getNodeValue());
                    xml_musicChannel_data.setApid(node.getAttributes().getNamedItem("apid").getNodeValue());
                    xml_musicChannel_data.setPpid(node.getAttributes().getNamedItem("ppid").getNodeValue());
                    xml_musicChannel_data.setAst(node.getAttributes().getNamedItem("ast").getNodeValue());
                    xml_musicChannel_data.setOpid(node.getAttributes().getNamedItem("opid").getNodeValue());
                    xml_musicChannel_data.setCtype(node.getAttributes().getNamedItem("ctype").getNodeValue());
                    xml_musicChannel_data.setTitle(node.getAttributes().getNamedItem("title").getNodeValue());
                    xml_musicChannel_data.setImage(node.getAttributes().getNamedItem("image").getNodeValue());
                    xml_musicChannel_data.setCa_id(node.getAttributes().getNamedItem("ca_id").getNodeValue());
                    xml_musicChannel_data.setChannelUri(node.getAttributes().getNamedItem("channelUri").getNodeValue());
                    arrayList.add(xml_musicChannel_data);
                }
            }

            Log.d("XMLParsing", "After xml parsing");

        } catch (Exception e) {
            Log.d("XMLParsing","Exception e : " + e.getMessage());
        }
    }
    public void initView(){
        aq = new AQuery(this);

        aq.id(R.id.bt_ch_1).visible().clicked(this,"buttonClicked");
        aq.id(R.id.bt_ch_2).visible().clicked(this);
        aq.id(R.id.bt_ch_3).visible().clicked(this);
        aq.id(R.id.bt_ch_4).visible().clicked(this);
        aq.id(R.id.bt_ch_5).visible().clicked(this);
        aq.id(R.id.bt_ch_6).visible().clicked(this);
        aq.id(R.id.bt_ch_7).visible().clicked(this);
        aq.id(R.id.bt_ch_8).visible().clicked(this);
        aq.id(R.id.bt_ch_9).visible().clicked(this);
        aq.id(R.id.bt_ch_10).visible().clicked(this);
        aq.id(R.id.bt_ch_11).visible().clicked(this);
        aq.id(R.id.bt_ch_12).visible().clicked(this);
        aq.id(R.id.bt_ch_13).visible().clicked(this);
        aq.id(R.id.bt_ch_14).visible().clicked(this);
        aq.id(R.id.bt_ch_15).visible().clicked(this);
        aq.id(R.id.bt_ch_16).visible().clicked(this);
        aq.id(R.id.bt_ch_17).visible().clicked(this);
        aq.id(R.id.bt_ch_18).visible().clicked(this);
        aq.id(R.id.bt_ch_19).visible().clicked(this);
        aq.id(R.id.bt_ch_20).visible().clicked(this);
    }
}
