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
public class SrcMLIf {
    //will likely only use for sequence diagrams
    private Element ifEle;
    private SrcMLCondition condition;
    private SrcMLThen then;
    private SrcMLElse else1;
    private SrcMLIf elseIf;

    public SrcMLIf(Element ifEle) {
        this.ifEle = ifEle;
        parse();
    }
    private void parse(){
        parseCondition();
        parseThen();
        parseElse();
        parseElseIf();
    }
    private void parseCondition(){
        List<Node> conditionNodes = XmlUtils.getImmediateChildren(ifEle, "condition");
        for (Node conditionNode : conditionNodes){
            //should only be 1 anyway
            condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
        }
    }
    private void parseThen(){
        List<Node> thenNodes = XmlUtils.getImmediateChildren(ifEle, "then");
        for (Node thenNode : thenNodes){
            //should only be 1 anyway
            then = new SrcMLThen(XmlUtils.elementify(thenNode));
        }
    }
    private void parseElse(){
        List<Node> elseNodes = XmlUtils.getImmediateChildren(ifEle, "else");
        for (Node elseNode : elseNodes){
            //should only be 1 anyway
            else1 = new SrcMLElse(XmlUtils.elementify(elseNode));
        }
    }
    private void parseElseIf(){
        List<Node> elseIfNodes = XmlUtils.getImmediateChildren(ifEle, "elseif");
        for (Node elseIfNode : elseIfNodes){
            //should only be 1 anyway
            elseIf = new SrcMLIf(XmlUtils.elementify(elseIfNode));
        }
    }

}
