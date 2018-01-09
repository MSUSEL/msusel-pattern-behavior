package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLIf {
    //will likely only use for sequence diagrams
    private Element ifEle;
    private SrcMLCondition condition;
    private SrcMLThen then;
    private SrcMLElse else1;
    private SrcMLIf elseIf;

    public SrcMLIf(Element ifEle) {
        this.ifEle = ifEle;
        parse();
    }
    private void parse(){
        parseCondition();
        parseThen();
        parseElse();
        parseElseIf();
    }
    private void parseCondition(){
        List<Node> conditionNodes = XmlUtils.getImmediateChildren(ifEle, "condition");
        for (Node conditionNode : conditionNodes){
            //should only be 1 anyway
            condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
        }
    }
    private void parseThen(){
        List<Node> thenNodes = XmlUtils.getImmediateChildren(ifEle, "then");
        for (Node thenNode : thenNodes){
            //should only be 1 anyway
            then = new SrcMLThen(XmlUtils.elementify(thenNode));
        }
    }
    private void parseElse(){
        List<Node> elseNodes = XmlUtils.getImmediateChildren(ifEle, "else");
        for (Node elseNode : elseNodes){
            //should only be 1 anyway
            else1 = new SrcMLElse(XmlUtils.elementify(elseNode));
        }
    }
    private void parseElseIf(){
        List<Node> elseIfNodes = XmlUtils.getImmediateChildren(ifEle, "elseif");
        for (Node elseIfNode : elseIfNodes){
            //should only be 1 anyway
            elseIf = new SrcMLIf(XmlUtils.elementify(elseIfNode));
        }
    }

}
