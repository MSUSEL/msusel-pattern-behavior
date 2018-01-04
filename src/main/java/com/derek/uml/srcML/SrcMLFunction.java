package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

//class to hold important function-related info (xml 'function' element)
public class SrcMLFunction {

    @Getter private Element functionEle;
    private List<SrcMLAnnotation> annotations;
    private List<String> specifiers;
    private SrcMLDataType type;
    private SrcMLName name;
    private SrcMLParameterList parameterList;
    private List<SrcMLThrows> throws1;
    private SrcMLBlock block;
    private SrcMLDefault default1;


    public SrcMLFunction(Element functionEle){
        this.functionEle = functionEle;
        parse();
    }

    private void parse(){
        parseAnnotations();
        parseSpecifiers();
        parseType();
        parseName();
        parseParameterList();
        parseThrows();
        parseBlock();
        //default is the only difference between 'function' and 'function_Decl' elements.. default exists in function_decl only
        parseDefault();
    }

    private void parseAnnotations(){
        annotations = new ArrayList<>();
        List<Node> annotationNodes = XmlUtils.getImmediateChildren(functionEle, "annotation");
        for (Node annotationNode : annotationNodes){
            //can have multiple annotations strings (@getter @setter, for example)
            annotations.add(new SrcMLAnnotation(XmlUtils.elementify(annotationNode)));
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
        List<Node> typeNodes = XmlUtils.getImmediateChildren(functionEle, "type");
        for (Node typeNode : typeNodes){
            type = new SrcMLDataType(XmlUtils.elementify(typeNode));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(functionEle, "name");
        for (Node nameNode : nameNodes){
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseParameterList(){
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(functionEle, "parameter_list");
        for (Node parameterNode : parameterNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }
    private void parseThrows(){
        throws1 = new ArrayList<>();
        List<Node> throwsNodes = XmlUtils.getImmediateChildren(functionEle, "throws");
        for (Node throwsNode : throwsNodes){
            throws1.add(new SrcMLThrows(XmlUtils.elementify(throwsNode)));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(functionEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    private void parseDefault(){
        List<Node> defaultNodes = XmlUtils.getImmediateChildren(functionEle, "default");
        for (Node defaultNode : defaultNodes){
            default1 = new SrcMLDefault(XmlUtils.elementify(defaultNode));
        }
    }



}
