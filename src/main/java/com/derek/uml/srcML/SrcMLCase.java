package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLCase extends SrcMLNode{
    private SrcMLName name;
    private String literal;

    public SrcMLCase(Element caseEle) {
        super(caseEle);
    }
    protected void parse(){
        name = parseName();
        parseLiteral();
    }
    private void parseLiteral(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "literal");
        for (Node nameNode : nameNodes){
            literal = nameNode.getTextContent();
        }
    }
}