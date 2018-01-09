package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLTry {
    private Element tryEle;
    private SrcMLInit init;
    private SrcMLBlock block;
    private List<SrcMLCatch> catches;
    private List<SrcMLFinally> finallys;

    public SrcMLTry(Element tryEle) {
        this.tryEle = tryEle;
        parse();
    }
    private void parse(){
        parseInit();
        parseBlock();
        parseCatch();
        parseFinally();
    }
    private void parseInit(){
        List<Node> initNodes = XmlUtils.getImmediateChildren(tryEle, "init");
        for (Node initNode : initNodes){
            init = new SrcMLInit(XmlUtils.elementify(initNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(tryEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    private void parseCatch(){
        catches = new ArrayList<>();
        List<Node> catchNodes = XmlUtils.getImmediateChildren(tryEle, "catch");
        for (Node catchNode : catchNodes){
            catches.add(new SrcMLCatch(XmlUtils.elementify(catchNode)));
        }
    }
    private void parseFinally(){
        finallys = new ArrayList<>();
        List<Node> finallyNodes = XmlUtils.getImmediateChildren(tryEle, "finally");
        for (Node finallyNode : finallyNodes){
            finallys.add(new SrcMLFinally(XmlUtils.elementify(finallyNode)));
        }
    }
}
