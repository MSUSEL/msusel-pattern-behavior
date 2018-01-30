/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

@Getter
public class SrcMLName extends SrcMLNode{
    //can be any one of: (complex_)name|templateArgumentList|argumentList|parameterList|templateParameterList|specifier|operator|index
    //parsign for each sub-type can be delegated to each class matching the sub-type

    private List<String> names;
    private SrcMLArgumentList argumentList;
    private SrcMLParameterList parameterList;
    private List<String> specifiers;
    private List<String> operators;
    private List<String> indices;

    public SrcMLName(String name){
        super(null);
        //use case for simple names.
        names = new ArrayList<>();
        names.add(name);
    }

    public SrcMLName(Element nameEle){
        super(nameEle);
    }

    protected void parse(){
        //moving operator before name because the existance of an operator will dictate the name. (Class.subclass)
        operators = parseOperators();
        names = parseStringNames();
        argumentList = parseArgumentList();
        parameterList = parseParameterList();
        specifiers = parseSpecifiers();
        indices = parseIndices();
    }

    protected List<String> parseStringNames(){
        List<String> names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "name");
        if (nameNodes.size() == 0){
            //no child elements, this is a simple name. <name>foo</name>
            names.add(element.getTextContent().replaceAll("\\s", ""));
        }
        for (Node nameNode : nameNodes) {
            if (!nameNode.getTextContent().equals("")) {
                //case where <name><name>foo</name><stuff></stuff></name> and found a simple name
                names.add(nameNode.getTextContent().replaceAll("\\s", ""));
            }
        }
        return names;
    }

    private List<String> parseIndices(){
        List<String> indices = new ArrayList<>();
        List<Node> indexNodes = XmlUtils.getImmediateChildren(element, "index");
        for (Node indexNode :  indexNodes){
            //this is likely going to require expansion in the future because an expression can be put in the index (foo[x+1])
            indices.add(indexNode.getTextContent());
        }
        return indices;
    }

    public String getName(){
        return consolidateNames();
    }

    /**
     * consolidates the list of names into one single string. this happens across owned names (foo.bar) and through generic-ally stacked names (foo<bar>)
     *
     * @return
     */
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
                    if (i < argumentsPointer.size() - 1){
                        consolidatedName += ",";
                    }
                }
                consolidatedName += ">";
            }
        }
        return consolidatedName;
    }
}
