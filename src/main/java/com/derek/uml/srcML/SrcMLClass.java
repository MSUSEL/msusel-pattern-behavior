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
        specifiers = parseSpecifiers();
        name = parseName();
        superLink = parseSuper();
        argumentList = parseArgumentList();
        block = parseBlock();
    }

    protected SrcMLName parseName(){
        SrcMLName name = super.parseName();
        if (name == null){
            name = new SrcMLName("Anonymous Class");
        }
        return name;
    }
    public String getName(){
        return name.getName();
    }
}
