package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLAnnotation {
    @Getter
    private Element annotationEle;
    private SrcMLName name;
    private SrcMLArgumentList argumentList;

    public SrcMLAnnotation(Element annotationEle) {
        this.annotationEle = annotationEle;
        parse();
    }
    private void parse(){
        parseName();
        parseArgumentList();
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(annotationEle, "name");
        for (Node nameNode : nameNodes){
            this.name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseArgumentList(){
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(annotationEle, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentListNode));
        }
    }
}
