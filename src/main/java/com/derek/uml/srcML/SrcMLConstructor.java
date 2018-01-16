package com.derek.uml.srcML;

import com.derek.uml.UMLGenerationUtils;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLConstructor {
    private Element constructorEle;
    private List<String> specifiers;
    private List<SrcMLAnnotation> annotations;
    private SrcMLName name;
    private SrcMLParameterList parameterList;
    private SrcMLBlock block;

    public SrcMLConstructor(Element constructorEle) {
        this.constructorEle = constructorEle;
        parse();
    }

    private void parse() {
        parseSpecifiers();
        parseAnnotations();
        parseName();
        parseParameterList();
        parseBlock();
    }

    private void parseSpecifiers() {
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(constructorEle, "specifier");
        for (Node specifier : specifierNodes) {
            //can have any number of specifier strings (public abstract)
            specifiers.add(specifier.getTextContent());
        }
    }
    private void parseAnnotations() {
        annotations = new ArrayList<>();
        List<Node> annotationNodes = XmlUtils.getImmediateChildren(constructorEle, "annotation");
        for (Node annotationNode : annotationNodes) {
            //can have multiple annotations strings (@getter @setter, for example)
            annotations.add(new SrcMLAnnotation(XmlUtils.elementify(annotationNode)));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(constructorEle, "name");
        for (Node nameNode : nameNodes){
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseParameterList(){
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(constructorEle, "parameter_list");
        for (Node parameterNode : parameterNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(constructorEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    public String getName(){
        return name.getName();
    }
}