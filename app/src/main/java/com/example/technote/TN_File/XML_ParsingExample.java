package com.example.technote.TN_File;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.tickaroo.tikxml.TikXml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okio.Buffer;

public class XML_ParsingExample extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<MyChannel> arrayList = new ArrayList<>();
    private MyChannel myChannel = new MyChannel();
    private Music_Channel music_channel = new Music_Channel();
    private Button bt_ch_1,bt_ch_2,bt_ch_3,bt_ch_4,bt_ch_5,bt_ch_6,bt_ch_7,bt_ch_8,bt_ch_9,bt_ch_10,
            bt_ch_11, bt_ch_12, bt_ch_13,bt_ch_14,bt_ch_15,bt_ch_16,bt_ch_17,bt_ch_18,bt_ch_19,bt_ch_20;
    private TextView textView;
    private MyHandler myHandler = new MyHandler();
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

            TikXml tikXml = new TikXml.Builder().addTypeAdapter(null,new MyTypeAdapter())
                    .exceptionOnUnreadXml(true)
                    .writeDefaultXmlDeclaration(true)
                    .build();

            InputStream is = getResources().getAssets().open("music_channel.xml");
            tikXml.write(new Buffer().readFrom(is), Music_Channel.class);
            //Log.d("getNode",tikXml.read(new Buffer().writeUtf8("music_channel.xml"), MyChannel.class).getTitle());
            //Log.d("getNode",music_channel.channel.get(0).getTitle());

        } catch (IOException e) {
            Log.d("getNode","IOException : " + e.toString());
            e.printStackTrace();
        }
    }

    public void initView(){
        textView = (TextView)findViewById(R.id.tv_xml_result);
        bt_ch_1 = (Button)findViewById(R.id.bt_ch_1);
        bt_ch_2 = (Button)findViewById(R.id.bt_ch_2);
        bt_ch_3 = (Button)findViewById(R.id.bt_ch_3);
        bt_ch_4 = (Button)findViewById(R.id.bt_ch_4);
        bt_ch_5 = (Button)findViewById(R.id.bt_ch_5);
        bt_ch_6 = (Button)findViewById(R.id.bt_ch_6);
        bt_ch_7 = (Button)findViewById(R.id.bt_ch_7);
        bt_ch_8 = (Button)findViewById(R.id.bt_ch_8);
        bt_ch_9 = (Button)findViewById(R.id.bt_ch_9);
        bt_ch_10 = (Button)findViewById(R.id.bt_ch_10);
        bt_ch_11 = (Button)findViewById(R.id.bt_ch_11);
        bt_ch_12 = (Button)findViewById(R.id.bt_ch_12);
        bt_ch_13 = (Button)findViewById(R.id.bt_ch_13);
        bt_ch_14 = (Button)findViewById(R.id.bt_ch_14);
        bt_ch_15 = (Button)findViewById(R.id.bt_ch_15);
        bt_ch_16 = (Button)findViewById(R.id.bt_ch_16);
        bt_ch_17 = (Button)findViewById(R.id.bt_ch_17);
        bt_ch_18 = (Button)findViewById(R.id.bt_ch_18);
        bt_ch_19 = (Button)findViewById(R.id.bt_ch_19);
        bt_ch_20 = (Button)findViewById(R.id.bt_ch_20);

        bt_ch_1.setOnClickListener(this);
        bt_ch_2.setOnClickListener(this);
        bt_ch_3.setOnClickListener(this);
        bt_ch_4.setOnClickListener(this);
        bt_ch_5.setOnClickListener(this);
        bt_ch_6.setOnClickListener(this);
        bt_ch_7.setOnClickListener(this);
        bt_ch_8.setOnClickListener(this);
        bt_ch_9.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v==bt_ch_1){
            myHandler.sendEmptyMessage(0);
        }else if(v==bt_ch_2){
            myHandler.sendEmptyMessage(1);
        }else if(v==bt_ch_3){
            myHandler.sendEmptyMessage(2);
        }else if(v==bt_ch_4){
            myHandler.sendEmptyMessage(3);
        }else if(v==bt_ch_5){
            myHandler.sendEmptyMessage(4);
        }

    }
    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            textView.setText("ch : " + arrayList.get(msg.what).getCh() +"\nip : "+arrayList.get(msg.what).getIp() +
                    "\nport : " + arrayList.get(msg.what).getPort() + "\nch_no : " + arrayList.get(msg.what).getCh_no() +
                    "\napid : " + arrayList.get(msg.what).getApid() + "\nppid : " + arrayList.get(msg.what).getPpid() +
                    "\nast : " + arrayList.get(msg.what).getAst() + "\nopid : " + arrayList.get(msg.what).getOpid() +
                    "\nctype : " + arrayList.get(msg.what).getCtype() + "\ntitle : "+arrayList.get(msg.what).getTitle() +
                    "\nimage : " + arrayList.get(msg.what).getImage() + "\nca_id : " + arrayList.get(msg.what).getCa_id() +
                    "\nca_pid : " + arrayList.get(msg.what).getCa_pid() +"\nchannelUri : " + arrayList.get(msg.what).getChannelUri());
        }
    }
}
