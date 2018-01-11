package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLEnum {
    private Element enumEle;
    private List<String> specifiers;
    private List<SrcMLAnnotation> annotations;
    private SrcMLName name;
    private SrcMLBlock block;

    public SrcMLEnum(Element enumEle) {
        this.enumEle = enumEle;
        parse();
    }

    private void parse() {
        parseAnnotations();
        parseSpecifiers();
        parseName();
        parseBlock();
    }

    private void parseAnnotations() {
        annotations = new ArrayList<>();
        List<Node> annotationNodes = XmlUtils.getImmediateChildren(enumEle, "annotation");
        for (Node annotationNode : annotationNodes) {
            annotations.add(new SrcMLAnnotation(XmlUtils.elementify(annotationNode)));
        }
    }
    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(enumEle, "specifier");
        for (Node specifierNode : specifierNodes) {
            specifiers.add(specifierNode.getTextContent());
        }
    }
    private void parseName() {
        List<Node> nameNodes = XmlUtils.getImmediateChildren(enumEle, "name");
        for (Node nameNode : nameNodes) {
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseBlock() {
        List<Node> blockNodes = XmlUtils.getImmediateChildren(enumEle, "block");
        for (Node blockNode : blockNodes) {
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    public String getName(){
        return name.getName();
    }

}