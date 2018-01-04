package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLFinally {
    @Getter
    private Element finallyEle;
    private SrcMLBlock block;

    public SrcMLFinally(Element finallyEle) {
        this.finallyEle = finallyEle;
        parse();
    }
    private void parse(){
        parseBlock();
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(finallyEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
}
