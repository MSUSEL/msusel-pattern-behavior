package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLAnnotationDefn {
    //will likely do nothing with this
    private Element annotationEle;
    private SrcMLName name;
    private SrcMLSuper super1;
    private SrcMLBlock block;

    public SrcMLAnnotationDefn(Element annotationEle) {
        this.annotationEle = annotationEle;
        parse();
    }
    private void parse(){
        parseName();
        parseSuper();
        parseBlock();
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(annotationEle, "name");
        for (Node nameNode : nameNodes){
            this.name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseSuper(){
        List<Node> superNodes = XmlUtils.getImmediateChildren(annotationEle, "super");
        for (Node superNode : superNodes){
            this.super1 = new SrcMLSuper(XmlUtils.elementify(superNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(annotationEle, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    public String getName(){
        return name.getName();
    }
}
