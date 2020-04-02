package com.example.technote.TN_File;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
class Music_Channel {
    @Attribute(name = "time")
    String time;

    @Path("root/ListVersion")
    @Attribute(name = "num")
    String num;

    @Path("root/ListVersion")
    @Attribute(name = "region")
    String region;

    @Path("root/Channel")
    @Element
    List<MyChannel> channel;
}
