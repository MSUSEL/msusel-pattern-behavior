package com.derek.uml.srcML;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLName {
    //can be any one of: (complex_)name|templateArgumentList|argumentList|parameterList|templateParameterList|specifier|operator|index
    //parsign for each sub-type can be delegated to each class matching the sub-type

    private Element nameEle;
    private List<String> names;
    private SrcMLArgumentList argumentList;
    private SrcMLParameterList parameterList;
    private List<String> specifiers;
    private List<String> operators;
    private List<String> indices;

    public SrcMLName(String name){
        //use case for simple names.
        this.names = new ArrayList<>();
        this.names.add(name);
    }

    public SrcMLName(Element nameEle){
        this.nameEle = nameEle;
        parse();
    }

    private void parse(){
        parseName();
        parseArgumentList();
        parseParameterList();
        parseSpecifiers();
        parseOperators();
        parseIndices();
    }

    private void parseName(){
        this.names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(nameEle, "name");
        if (nameNodes.size() == 0){
            //no child elements, this is a simple name. <name>foo</name>
            names.add(nameEle.getTextContent());
        }
        for (Node nameNode : nameNodes) {
            if (!nameNode.getTextContent().equals("")) {
                //case where <name><name>foo</name><stuff></stuff></name> and found a simple name
                names.add(nameNode.getTextContent());
            }
        }
    }

    private void parseArgumentList(){
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(nameEle, "argument_list");
        for (Node argumentNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentNode));
            //generics handler
            if (argumentList.getTypeAttribute().equals("generic")){
                //generic type found.
                String genericBuilder = "<";
                for (int i = 0; i < argumentList.getArguments().size(); i++){
                    SrcMLArgument arg = argumentList.getArguments().get(i);
                    genericBuilder += arg.getName();
                    if (i != argumentList.getArguments().size()-1){
                        //last argument
                        genericBuilder += ",";
                    }
                }
                genericBuilder += ">";
                String oldName = names.get(0);
                names.remove(0);
                names.add(0, oldName += genericBuilder);
            }
        }
    }

    private void parseParameterList(){
        List<Node> parameterListNodes = XmlUtils.getImmediateChildren(nameEle, "parameter_list");
        for (Node parameterNode :  parameterListNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }

    private void parseSpecifiers(){
        this.specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(nameEle, "specifier");
        for (Node specifierNode :  specifierNodes){
            specifiers.add(specifierNode.getTextContent());
        }
    }

    private void parseOperators(){
        this.operators = new ArrayList<>();
        List<Node> operatorNodes = XmlUtils.getImmediateChildren(nameEle, "operator");
        for (Node operatorNode :  operatorNodes){
            operators.add(operatorNode.getTextContent());
        }
    }

    private void parseIndices(){
        this.indices = new ArrayList<>();
        List<Node> indexNodes = XmlUtils.getImmediateChildren(nameEle, "index");
        for (Node indexNode :  indexNodes){
            //this is likely going to require expansion in the future because an expression can be put in the index (foo[x+1])
            indices.add(indexNode.getTextContent());
        }
    }

    public String getName(){
        if (names.size() > 1){
            System.out.println("two names:");
            for (String name : names){
                System.out.println(name);
            }
            System.exit(0);
        }
        return names.get(0);
    }

}
