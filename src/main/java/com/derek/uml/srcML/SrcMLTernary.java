package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLTernary {
    private Element ternaryEle;
    private SrcMLCondition condition;
    private SrcMLThen then;
    private SrcMLElse else1;

    public SrcMLTernary(Element ternaryEle){
        this.ternaryEle = ternaryEle;
        parse();
    }
    private void parse(){
        parseCondition();
        parseThen();
        parseElse();
    }
    private void parseCondition(){
        List<Node> conditionNodes = XmlUtils.getImmediateChildren(ternaryEle, "condition");
        for (Node conditionNode : conditionNodes){
            //should only be 1 anyway
            condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
        }
    }
    private void parseThen(){
        List<Node> thenNodes = XmlUtils.getImmediateChildren(ternaryEle, "then");
        for (Node thenNode : thenNodes){
            //should only be 1 anyway
            then = new SrcMLThen(XmlUtils.elementify(thenNode));
        }
    }
    private void parseElse(){
        List<Node> elseNodes = XmlUtils.getImmediateChildren(ternaryEle, "else");
        for (Node elseNode : elseNodes){
            //should only be 1 anyway
            else1 = new SrcMLElse(XmlUtils.elementify(elseNode));
        }
    }
}
