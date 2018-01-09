package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLSwitch {
    private Element switchEle;
    private SrcMLCondition condition;
    private SrcMLBlock block;

    public SrcMLSwitch(Element switchEle) {
        this.switchEle = switchEle;
        parse();
    }
    private void parse(){
        parseCondition();
        parseBlock();
    }
    private void parseCondition(){
        List<Node> conditionNodes = XmlUtils.getImmediateChildren(switchEle, "condition");
        for (Node conditionNode : conditionNodes){
            //should only be 1 anyway
            condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(switchEle, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }

}
