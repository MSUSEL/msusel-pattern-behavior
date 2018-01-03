package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLArgumentList {
    //not sure how much of this I will need, but I'm creatin gthe class just in case.
    //a lot of the time the arguments are just generics. But they can also be parameterized arguments (String[] args)
    @Getter
    private Element argumentListEle;
    @Getter private List<SrcMLArgument> arguments;
    //typeAttribute is for capturing the optional attribute on argumentlists.
    @Getter private String typeAttribute;

    public SrcMLArgumentList(Element argumentListEle){
        this.argumentListEle = argumentListEle;
        parse();
    }

    public void parse(){
        parseOptionalAttribute();
        parseArguments();
    }

    private void parseOptionalAttribute(){
        //this will have zero or 1 attributes.
        typeAttribute = argumentListEle.getAttribute("type");
    }

    private void parseArguments(){
        arguments = new ArrayList<>();
        List<Node> argumentNodes = XmlUtils.getImmediateChildren(argumentListEle, "argument");
        for (Node argumentNode : argumentNodes){
            arguments.add(new SrcMLArgument(XmlUtils.elementify(argumentNode)));
        }
    }

    public class SrcMLArgument {
        //nested class for arguments.. I am doing it this way because I think its best.... otherwise I would need to create a standalone class.

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
}
