package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLInterface {
    @Getter
    private Element interfaceEle;
    private SrcMLName name;
    private SrcMLSuper super1;
    private SrcMLBlock block;

    public SrcMLInterface(Element interfaceEle) {
        this.interfaceEle = interfaceEle;
        parse();
    }
    private void parse(){
        parseName();
        parseSuper();
        parseBlock();
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(interfaceEle, "name");
        for (Node nameNode : nameNodes){
            this.name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseSuper(){
        List<Node> superNodes = XmlUtils.getImmediateChildren(interfaceEle, "super");
        for (Node superNode : superNodes){
            this.super1 = new SrcMLSuper(XmlUtils.elementify(superNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(interfaceEle, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }

}
