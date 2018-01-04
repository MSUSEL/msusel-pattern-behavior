package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLArgument {

    @Getter
    private Element argumentEle;
    @Getter private String modifier;
    @Getter private SrcMLExpression expression;
    @Getter private SrcMLName name;

    public SrcMLArgument(Element argumentEle){
        this.argumentEle = argumentEle;
        parse();
    }
    private void parse(){
        parseModifier();
        parseExpression();
        parseName();
    }
    private void parseModifier(){
        List<Node> modifierNodes = XmlUtils.getImmediateChildren(argumentEle, "modifier");
        for (Node modifierNode : modifierNodes){
            //should be 0 or 1
            modifier = modifierNode.getTextContent();
        }
    }
    private void parseExpression(){
        List<Node> expressionNodes = XmlUtils.getImmediateChildren(argumentEle, "expr");
        for (Node expressionNode : expressionNodes){
            //should only be 1 anyway
            expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(argumentEle, "name");
        for (Node nameNode : nameNodes){
            //should only be 1 anyway
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
}
