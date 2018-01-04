package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLThen {
    //only 2 classes use this; 'SrcMLIf' uses it for the block, and 'SrcMLTernary' uses it for the expression.
    @Getter
    private Element thenEle;
    @Getter
    private SrcMLBlock block;
    @Getter
    private SrcMLExpression expression;

    public SrcMLThen(Element thenEle) {
        this.thenEle = thenEle;
        parse();
    }
    private void parse(){
        parseBlock();
        parseExpression();
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(thenEle, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    private void parseExpression(){
        List<Node> expressionNodes = XmlUtils.getImmediateChildren(thenEle, "expr");
        for (Node expressionNode : expressionNodes){
            //can have 0 or 1
            expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
        }
    }
}
