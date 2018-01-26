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
public class SrcMLArgumentList extends SrcMLNode{
    //not sure how much of this I will need, but I'm creatin gthe class just in case.
    //a lot of the time the arguments are just generics. But they can also be parameterized arguments (String[] args)
    private List<SrcMLArgument> arguments;
    //typeAttribute is for capturing the optional attribute on argumentlists.
    private String typeAttribute;

    public SrcMLArgumentList(Element argumentListEle){
        super(argumentListEle);
        this.element = argumentListEle;
    }

    protected void parse(){
        parseOptionalAttribute();
        parseArguments();
    }

    private void parseOptionalAttribute(){
        //this will have zero or 1 attributes.
        typeAttribute = element.getAttribute("type");
    }

    private void parseArguments(){
        arguments = new ArrayList<>();
        List<Node> argumentNodes = XmlUtils.getImmediateChildren(element, "argument");
        for (Node argumentNode : argumentNodes){
            arguments.add(new SrcMLArgument(XmlUtils.elementify(argumentNode)));
        }
    }

}
