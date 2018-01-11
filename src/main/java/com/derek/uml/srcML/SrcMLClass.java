package com.derek.uml.srcML;

import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClass;
import com.derek.uml.UMLGenerationUtils;
import com.derek.uml.UMLOperation;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLClass {
    //there is some differences in class when considering anonymous classes vs normal classes.
    //Specifically, normal classes have names and anonymous classes don't. I think I can get around this by
    //automatically assigning a class name to anonymous classes. 'AnonymousClass'

    //right now im not getting a list of specifiers from the class definitions.. mostly because its not in the spec.
    //I feel like I should put that functionality in..
    private Element classEle;
    private SrcMLName name;
    private SrcMLSuper superLink;
    private SrcMLArgumentList argumentList;
    private SrcMLBlock block;
    private List<String> specifiers;

    public SrcMLClass(Element classEle) {
        this.classEle = classEle;
        parse();
    }

    private void parse(){
        parseSpecifiers();
        parseName();
        parseSuper();
        parseArgumentList();
        parseBlock();
    }

    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(classEle, "specifier");
        for (Node specifierNode : specifierNodes){
            specifiers.add(specifierNode.getTextContent());
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(classEle, "name");
        for (Node nameNode : nameNodes){
            //will only happen once but im leaving hte looop in for safety (poor documentation in srcml)
            this.name = new SrcMLName(XmlUtils.elementify(nameNode));
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
    public String getName(){
        return name.getName();
    }
}
