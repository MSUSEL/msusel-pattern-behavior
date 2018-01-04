package com.derek.uml.srcML;


import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLThrows{
    @Getter
    private Element throwsEle;
    private List<SrcMLArgument> arguments;

    public SrcMLThrows(Element throwsEle) {
        this.throwsEle = throwsEle;
        parse();
    }
    private void parse(){
        parseArguments();
    }
    private void parseArguments(){
        arguments = new ArrayList<>();
        List<Node> argumentNodes = XmlUtils.getImmediateChildren(throwsEle, "argument");
        for (Node argumentNode : argumentNodes){
            arguments.add(new SrcMLArgument(XmlUtils.elementify(argumentNode)));
        }
    }
}