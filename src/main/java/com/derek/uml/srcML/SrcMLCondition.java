package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLCondition {
    private Element conditionEle;
    private SrcMLExpression expression;

    public SrcMLCondition(Element conditionEle) {
        this.conditionEle = conditionEle;
        parse();
    }
    private void parse(){
        parseExpression();
    }
    private void parseExpression(){
        List<Node> expressionNodes = XmlUtils.getImmediateChildren(conditionEle, "expr");
        for (Node expressionNode : expressionNodes){
            //can have 0 or 1
            expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
        }
    }
}
