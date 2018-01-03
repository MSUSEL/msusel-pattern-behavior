package com.derek.uml.srcML;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//class to hold important function-related info (xml 'function' element)
public class SrcMLFunctionParser {
    @Getter @Setter private List<String> annotations;
    @Getter @Setter private List<String> specifiers;
    @Getter @Setter private SrcMLDataType type;
    @Getter @Setter private SrcMLName name;
    @Getter @Setter private Element functionEle;


    public SrcMLFunctionParser(Element functionEle){
        this.functionEle = functionEle;
        parse();
    }

    private void parse(){
        parseAnnotations();
        parseSpecifiers();
    }

    private void parseAnnotations(){
        annotations = new ArrayList<>();
        List<Node> annotationNodes = XmlUtils.getImmediateChildren(functionEle, "annotation");
        for (Node annotation : annotationNodes){
            //can have multiple annotations strings (@getter @setter, for example)
            for (Node name : XmlUtils.getImmediateChildren(annotation, "name")){
                annotations.add(name.getTextContent());
            }
        }
    }

    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(functionEle, "specifier");
        for (Node specifier : specifierNodes){
            //can have any number of specifier strings (public abstract)
            specifiers.add(specifier.getTextContent());
        }
    }

    private void parseType(){
        //per srcML spec, a function can only have 1 type
        Node nodeType = XmlUtils.getImmediateChildren(functionEle, "type").get(0);
        type = new SrcMLDataType(XmlUtils.elementify(nodeType));
    }

}
