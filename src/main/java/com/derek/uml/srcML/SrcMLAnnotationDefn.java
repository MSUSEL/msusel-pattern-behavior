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

import java.util.List;

@Getter
public class SrcMLAnnotationDefn extends SrcMLNode{
    //will likely do nothing with this
    private SrcMLName name;
    private SrcMLSuper super1;
    private SrcMLBlock block;

    public SrcMLAnnotationDefn(Element annotationEle) {
        super(annotationEle);
    }
    protected void parse(){
        parseName();
        parseSuper();
        parseBlock();
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "name");
        for (Node nameNode : nameNodes){
            this.name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseSuper(){
        List<Node> superNodes = XmlUtils.getImmediateChildren(element, "super");
        for (Node superNode : superNodes){
            this.super1 = new SrcMLSuper(XmlUtils.elementify(superNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(element, "block");
        for (Node blockNode : blockNodes){
            this.block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    public String getName(){
        return name.getName();
    }
}
