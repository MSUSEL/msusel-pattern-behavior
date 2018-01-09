package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLArgumentList {
    //not sure how much of this I will need, but I'm creatin gthe class just in case.
    //a lot of the time the arguments are just generics. But they can also be parameterized arguments (String[] args)
    private Element argumentListEle;
    private List<SrcMLArgument> arguments;
    //typeAttribute is for capturing the optional attribute on argumentlists.
    private String typeAttribute;

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

}
