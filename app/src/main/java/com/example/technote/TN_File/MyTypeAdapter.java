package com.example.technote.TN_File;

import com.tickaroo.tikxml.TikXmlConfig;
import com.tickaroo.tikxml.XmlReader;
import com.tickaroo.tikxml.XmlWriter;
import com.tickaroo.tikxml.typeadapter.TypeAdapter;

import java.io.IOException;


class MyTypeAdapter implements TypeAdapter<MyChannel> {

    @Override
    public MyChannel fromXml(XmlReader reader, TikXmlConfig config) throws IOException {

        return null;
    }

    @Override
    public void toXml(XmlWriter writer, TikXmlConfig config, MyChannel value, String overridingXmlElementTagName) throws IOException {

    }
}
