package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLImport {

    private Element importEle;
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
    public List<String> getNames(){
        List<String> s = new ArrayList<>();
        for (SrcMLName name : names){
            s.add(name.getName());
        }
        return s;
    }

}
