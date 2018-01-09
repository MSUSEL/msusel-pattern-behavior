package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLWhile {
    //this class will fulfill both the 'while' and 'do-while' keywords, because they are the same at a grammar perspective.
    private Element whileEle;
    private SrcMLCondition condition;
    private SrcMLBlock block;

    public SrcMLWhile(Element whileEle) {
        this.whileEle = whileEle;
        parse();
    }
    private void parse(){
        parseCondition();
        parseBlock();
    }
    private void parseCondition(){
        List<Node> conditionNodes = XmlUtils.getImmediateChildren(whileEle, "condition");
        for (Node conditionNode : conditionNodes){
            //should only be 1 anyway
            condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(whileEle, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
}
