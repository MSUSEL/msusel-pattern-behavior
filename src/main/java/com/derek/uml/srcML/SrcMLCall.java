package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLCall {
    //will likely have ot use this class for sequence diagram generation
    @Getter
    private Element callEle;
    @Getter
    private SrcMLName name;
    @Getter
    private SrcMLArgumentList argumentList;


    public SrcMLCall(Element callEle){
        this.callEle = callEle;
        parse();
    }

    private void parse(){
        parseName();
        parseArgumentList();
    }

    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(callEle, "name");
        for (Node name : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(name));
        }
    }
    private void parseArgumentList(){
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(callEle, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentListNode));
        }
    }
}
