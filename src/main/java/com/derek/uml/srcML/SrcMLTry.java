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
public class SrcMLTry {
    private Element tryEle;
    private SrcMLInit init;
    private SrcMLBlock block;
    private List<SrcMLCatch> catches;
    private List<SrcMLFinally> finallys;

    public SrcMLTry(Element tryEle) {
        this.tryEle = tryEle;
        parse();
    }
    private void parse(){
        parseInit();
        parseBlock();
        parseCatch();
        parseFinally();
    }
    private void parseInit(){
        List<Node> initNodes = XmlUtils.getImmediateChildren(tryEle, "init");
        for (Node initNode : initNodes){
            init = new SrcMLInit(XmlUtils.elementify(initNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(tryEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    private void parseCatch(){
        catches = new ArrayList<>();
        List<Node> catchNodes = XmlUtils.getImmediateChildren(tryEle, "catch");
        for (Node catchNode : catchNodes){
            catches.add(new SrcMLCatch(XmlUtils.elementify(catchNode)));
        }
    }
    private void parseFinally(){
        finallys = new ArrayList<>();
        List<Node> finallyNodes = XmlUtils.getImmediateChildren(tryEle, "finally");
        for (Node finallyNode : finallyNodes){
            finallys.add(new SrcMLFinally(XmlUtils.elementify(finallyNode)));
        }
    }
}
