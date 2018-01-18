package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLExtends {
    private Element extendsEle;
    private SrcMLName name;

    public SrcMLExtends(Element extendsEle) {
        this.extendsEle = extendsEle;
        parse();
    }
    private void parse(){
        parseName();
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(extendsEle, "name");
        for (Node name : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(name));
        }
    }
    public String getName(){
        return name.getName();
    }
}
