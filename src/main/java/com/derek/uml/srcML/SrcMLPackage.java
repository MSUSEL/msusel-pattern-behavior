package com.derek.uml.srcML;


import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLPackage {
    @Getter
    private Element packageEle;

    @Getter
    private List<SrcMLName> names;

    public SrcMLPackage(Element packageEle){
        this.packageEle = packageEle;
        parse();
    }

    private void parse(){
        parseName();
    }

    private void parseName(){
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(packageEle, "name");
        for (Node nameNode : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(nameNode)));
        }
    }

}
