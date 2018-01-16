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
    private List<String> names;
    private SrcMLArgumentList argumentList;
    private SrcMLParameterList parameterList;
    private List<String> specifiers;
    private List<String> operators;
    private List<String> indices;

    public SrcMLName(String name){
        //use case for simple names.
        names = new ArrayList<>();
        names.add(name);
    }

    public SrcMLName(Element nameEle){
        this.nameEle = nameEle;
        parse();
    }

    private void parse(){
        //moving operator before name because the existance of an operator will dictate the name. (Class.subclass)
        parseOperators();
        parseName();
        parseArgumentList();
        parseParameterList();
        parseSpecifiers();
        parseIndices();
    }

    private void parseName(){
        this.names = new LinkedList<>();
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

    /**
     * I am going to refactor this so that stacked names fill spots in teh arraylist (List<Integer> -> [List][Integer]
     */
    private void parseArgumentList(){
        //there is a bug where we are only ever going 1 name deep, yet the requirements are that we can go n name deep (List<List<List<List...)
        //to fix this bug I think its worth getting all argument list nodes from this name, not just the immediate children.
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(nameEle, "argument_list");
        for (Node argumentNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentNode));
            //generics handler
            if (argumentList.getTypeAttribute().equals("generic")){
                //generic type found.
                //I don't think I need to care about this.
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
        return consolidateNames();
    }

    private String consolidateNames(){
        String consolidatedName = XmlUtils.stringifyNames(names);
        SrcMLArgumentList argumentListPointer = argumentList;
        if (argumentListPointer != null) {
            List<SrcMLArgument> argumentsPointer;
            Stack<SrcMLArgumentList> nameStack = new Stack<>();
            nameStack.push(argumentListPointer);
            while (!nameStack.isEmpty()) {
                argumentsPointer = nameStack.pop().getArguments();
                consolidatedName += "<";
                for (int i = 0; i < argumentsPointer.size(); i++) {
                    SrcMLArgument arg = argumentsPointer.get(i);
                    consolidatedName += arg.getName();
                    if (arg.getNameObj().getArgumentList() != null) {
                        //end of the stack; no more names.
                        //nameStack.push(arg.getNameObj().getArgumentList());
                    }
                }
                consolidatedName += ">";
            }
        }
        return consolidatedName;
    }
}
