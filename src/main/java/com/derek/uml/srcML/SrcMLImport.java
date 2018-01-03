package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLImport {

    @Getter
    private Element importEle;
    @Getter
    private List<SrcMLName> names;

    public SrcMLImport(Element importEle) {
        this.importEle = importEle;
        parse();
    }
    private void parse(){
        parseName();
    }

    private void parseName(){
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(importEle, "name");
        for (Node name : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(name)));
        }
    }

}
