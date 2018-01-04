package com.derek.uml.srcML;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLDataType {
    @Getter private Element typeEle;
    @Getter @Setter private List<String> specifiers;
    @Getter @Setter private List<String> modifiers;
    //not sure if I'll need argumentlist, but at least it is there.
    @Getter @Setter private List<SrcMLArgumentList> argumentList;
    @Getter @Setter private List<SrcMLName> names;

    //type<type>(typeTypeAttr?):(specifier|modifier|decltype|templateArgumentList|name)*;

    //this variable represents nested types, which are usually generics. Bear in mind this field can also be null.
    //not sure how I am going to access types n deep yet.. such as List<List<List<List<List<....String>>>>...>> (how do I get the string?
    private SrcMLDataType nestedType;

    public SrcMLDataType(Element typeEle){
        this.typeEle = typeEle;
        parse();
    }

    private void parse(){
        parseSpecifiers();
        parseModifiers();
        parseArgumentList();
        parseName();
    }

    private void parseSpecifiers(){
        //in the context of types, this could be any combination or number of: "final, public, static", etc.
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(typeEle, "specifier");
        for (Node specifier : specifierNodes){
            specifiers.add(specifier.getTextContent());
        }
    }

    private void parseModifiers(){
        modifiers = new ArrayList<>();
        List<Node> modifierNodes = XmlUtils.getImmediateChildren(typeEle, "modifier");
        for (Node modifier : modifierNodes){
            modifiers.add(modifier.getTextContent());
        }
    }

    private void parseArgumentList(){
        argumentList = new ArrayList<>();
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(typeEle, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList.add(new SrcMLArgumentList(XmlUtils.elementify(argumentListNode)));
        }
    }

    private void parseName(){
        //should only be 1 name, but it can have many apparently
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(typeEle, "name");
        for (Node nameNode : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(nameNode)));
        }
    }
}
