package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLSuper {
    //will likely need this to show generalizations in uml.

    @Getter
    private Element superEle;
    @Getter
    private List<SrcMLExtends> extender;
    @Getter
    private List<SrcMLImplements> implementor;
    @Getter
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
        extender = new ArrayList<>();
        List<Node> extendsNodes = XmlUtils.getImmediateChildren(superEle, "extends");
        for (Node extendsNode : extendsNodes){
            extender.add(new SrcMLExtends(XmlUtils.elementify(extendsNode)));
        }
    }
    private void parseImplements(){
        implementor = new ArrayList<>();
        List<Node> implementorNodes = XmlUtils.getImmediateChildren(superEle, "implements");
        for (Node implementorNode : implementorNodes){
            implementor.add(new SrcMLImplements(XmlUtils.elementify(implementorNode)));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(superEle, "name");
        for (Node name : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(name));
        }
    }


}
