package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

@Getter
public class SrcMLName {
    //can be any one of: (complex_)name|templateArgumentList|argumentList|parameterList|templateParameterList|specifier|operator|index
    //parsign for each sub-type can be delegated to each class matching the sub-type

    private Element nameEle;
    private Queue<List<String>> names;
    private SrcMLArgumentList argumentList;
    private SrcMLParameterList parameterList;
    private List<String> specifiers;
    private List<String> operators;
    private List<String> indices;

    public SrcMLName(String name){
        //use case for simple names.
        names = new PriorityQueue<>();
        ArrayList<String> basicName = new ArrayList<>();
        basicName.add(name);
        this.names.add(basicName);
    }

    public SrcMLName(Element nameEle){
        this.nameEle = nameEle;
        parse();
    }

    private void parse(){
        //moving operator before name because the existance of an operator will dictate the name.
        parseOperators();
        parseName();
        parseArgumentList();
        parseParameterList();
        parseSpecifiers();
        parseIndices();
    }

    private void parseName(){
        this.names = new PriorityQueue<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(nameEle, "name");
        if (nameNodes.size() == 0){
            //no child elements, this is a simple name. <name>foo</name>
            List<String> nameBar = new ArrayList<>();
            nameBar.add(nameEle.getTextContent());
            names.add(nameBar);
        }
        List<String> nameList = new ArrayList<>();
        for (Node nameNode : nameNodes) {
            if (!nameNode.getTextContent().equals("")) {
                //case where <name><name>foo</name><stuff></stuff></name> and found a simple name
                nameList.add(nameNode.getTextContent());
                names.add(nameList);
            }
        }
    }

    /**
     * I am going to refactor this so that stacked names fill spots in teh arraylist (List<Integer> -> [List][Integer]
     */
    private void parseArgumentList(){
        //there is a bug where we are only ever going 1 name deep, yet the requirements are that we can go n name deep (List<List<List<List...)
        //to fix this bug I think its worth getting all argument list nodes from this name, not just the immediate children.
        //List<Node> argumentListNodes = XmlUtils.getImmediateChildren(nameEle, "argument_list");
        NodeList argumentNodes = nameEle.getElementsByTagName("argument_list");
        for (int i = 0; i < argumentNodes.getLength(); i++){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentNodes.item(i)));
            //generics handler
            if (argumentList.getTypeAttribute().equals("generic")){
                //generic type found.
                List<String> nameList = new ArrayList<>();
                for (int j = 0; j < argumentList.getArguments().size(); j++){
                    //case where foo.bar
                    SrcMLArgument arg = argumentList.getArguments().get(j);
                    nameList.add(arg.getName());
                }
                names.add(nameList);
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
            return XmlUtils.stringifyNames(names);
        }else {
            return XmlUtils.stringifyNames(names.remove());
        }
    }
    public Queue<List<String>> getNames(){
        return names;
    }

}
