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

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLClass extends SrcMLNode{
    //there is some differences in class when considering anonymous classes vs normal classes.
    //Specifically, normal classes have names and anonymous classes don't. I think I can get around this by
    //automatically assigning a class name to anonymous classes. 'AnonymousClass'

    //right now im not getting a list of specifiers from the class definitions.. mostly because its not in the spec.
    //I feel like I should put that functionality in..
    private SrcMLName name;
    private SrcMLSuper superLink;
    private SrcMLArgumentList argumentList;
    private SrcMLBlock block;
    private List<String> specifiers;

    public SrcMLClass(Element classEle) {
        super(classEle);
    }
    protected void parse(){
        parseSpecifiers();
        parseName();
        parseSuper();
        parseArgumentList();
        parseBlock();
    }

    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(element, "specifier");
        for (Node specifierNode : specifierNodes){
            specifiers.add(specifierNode.getTextContent());
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "name");
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
        List<Node> superNodes = XmlUtils.getImmediateChildren(element, "super");
        for (Node superNode : superNodes){
            this.superLink = new SrcMLSuper(XmlUtils.elementify(superNode));
        }
    }
    private void parseArgumentList(){
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(element, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentListNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(element, "block");
        for (Node block : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(block));
        }
    }
    public String getName(){
        return name.getName();
    }
}
