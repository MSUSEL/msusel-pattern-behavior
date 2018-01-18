package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLSuper {
    //will likely need this to show generalizations in uml.
    private Element superEle;
    private List<SrcMLExtends> extenders;
    private List<SrcMLImplements> implementors;
    private SrcMLName name;

    public SrcMLSuper(Element superEle) {
        this.superEle = superEle;
        parse();
    }
    private void parse(){
        parseExtends();
        parseImplements();
        parseName();
    }
    private void parseExtends(){
        extenders = new ArrayList<>();
        List<Node> extendsNodes = XmlUtils.getImmediateChildren(superEle, "extends");
        for (Node extendsNode : extendsNodes){
            extenders.add(new SrcMLExtends(XmlUtils.elementify(extendsNode)));
        }
    }
    private void parseImplements(){
        implementors = new ArrayList<>();
        List<Node> implementorNodes = XmlUtils.getImmediateChildren(superEle, "implements");
        for (Node implementorNode : implementorNodes){
            implementors.add(new SrcMLImplements(XmlUtils.elementify(implementorNode)));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(superEle, "name");
        for (Node name : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(name));
        }
    }
    public String getName(){
        return name.getName();
    }
}
