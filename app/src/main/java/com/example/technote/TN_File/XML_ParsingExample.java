package com.example.technote.TN_File;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XML_ParsingExample extends AppCompatActivity {
    private ArrayList<XML_MusicChannel_Data> arrayList = new ArrayList<>();
    private XML_MusicChannel_Data xml_musicChannel_data;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_xml_parsing_example);
        initView();
        try {
            InputStream is = getResources().getAssets().open("music_channel.xml");

            //파서 인스턴스화
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("Channel");
            Log.d("getNode", "nList.toString : " + String.valueOf(nList.getLength()));
            Log.d("getNode","nList.item(0).getNodeValue() : " +nList.item(0).getAttributes().getNamedItem("title").getNodeValue());
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Element element2 = (Element) node;

                    Log.d("getNode", "node.getAttributes().getNamedItem(title).getNodeValue() : " + String.valueOf(i) + " " +  node.getAttributes().getNamedItem("title").getNodeValue());
                    xml_musicChannel_data = new XML_MusicChannel_Data();
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
                    //xml_musicChannel_data.setCh(node.getAttributes().getNamedItem("ch").getNodeValue());
                    //textView.setText(textView.getText()+"\nip : " + getValue("Channel", element2)+"\n");
                    //textView.setText(textView.getText()+"title : " + getValue("Channel", element2)+"\n");
                    //textView.setText(textView.getText()+"-----------------------");
                    Log.d("getNode", "for End");
                }
            }

        } catch (Exception e) {
            Log.d("getNode","Exception e : " + e.getMessage());
        }

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public void initView(){
        textView = (TextView)findViewById(R.id.tv_xml_result);
    }
}
