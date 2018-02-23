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

import com.derek.uml.CallTreeNode;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLIf extends SrcMLNode{
    //will likely only use for sequence diagrams
    private SrcMLCondition condition;
    private SrcMLThen then;
    private SrcMLElse else1;
    private SrcMLIf elseIf;
    private CallTreeNode<SrcMLNode> callTree;

    public SrcMLIf(Element ifEle) {
        super(ifEle);
    }
    protected void parse(){
        condition = parseCondition();
        then = parseThen();
        else1 = parseElse();
        //don't know if I need to do anything here.
        elseIf = parseElseIf();


        callTree = new CallTreeNode<>(this, "if");

        if (condition != null) {
            buildCallTree(callTree, condition.getExpression());
        }
        if (then != null) {
            buildCallTree(callTree, then.getExpression());
            then.fillCallTree(callTree);
        }
        if (else1 != null) {
            //this is a special case because an if statment can be if{} with no else. or it can have an else.
            buildCallTree(callTree, else1.getExpression());
            else1.fillCallTree(callTree);
        }else{
            buildCallTree(callTree, null);
        }
        if (elseIf != null){
            callTree.addChild(elseIf.getCallTree());
        }
    }



}
