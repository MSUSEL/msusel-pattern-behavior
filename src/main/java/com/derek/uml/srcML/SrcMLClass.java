package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLClass {
    //there is some differences in class when considering anonymous classes vs normal classes.
    //Specifically, normal classes have names and anonymous classes don't. I think I can get around this by
    //automatically assigning a class name to anonymous classes. 'AnonymousClass'

    //right now im not getting a list of specifiers from the class definitions.. mostly because its not in the spec.
    //I feel like I should put that functionality in..
    @Getter
    private Element classEle;
    @Getter
    private SrcMLName name;
    @Getter
    private SrcMLSuper superLink;
    @Getter
    private SrcMLArgumentList argumentList;
    @Getter
    private SrcMLBlock block;

    public SrcMLClass(Element classEle) {
        this.classEle = classEle;
        parse();
    }

    private void parse(){
        parseName();
        parseSuper();
        parseArgumentList();
        parseBlock();
    }

    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(classEle, "name");
        for (Node name : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(name));
        }
        if (name == null){
            //anonymous class
            this.name = new SrcMLName("Anonymous Class");
        }
    }
    private void parseSuper(){
        List<Node> superNodes = XmlUtils.getImmediateChildren(classEle, "super");
        for (Node superNode : superNodes){
            this.superLink = new SrcMLSuper(XmlUtils.elementify(superNode));
        }
    }
    private void parseArgumentList(){
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(classEle, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentListNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(classEle, "block");
        for (Node block : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(block));
        }
    }


}
