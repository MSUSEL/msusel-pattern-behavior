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
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLSwitch extends SrcMLNode{
    private SrcMLCondition condition;
    private SrcMLBlock block;
    private CallTreeNode<SrcMLNode> callTree;

    public SrcMLSwitch(Element switchEle) {
        super(switchEle);
    }
    protected void parse(){
        condition = parseCondition();
        block = parseBlock();

        callTree = new CallTreeNode<>(this, "conditional");
        //pulling this directly from srcmlIf.
        if (condition != null) {
            //tricky situation here. So, every if has (at least one) condition statement, and the condition can be a
            //call or not. However, typically the condition is placed in the guard during sequence diagram generation.
            //if the condition is a call, I still care about its messages because they represent dependencies.
            //so, I think im just going to avoid the guard. In the basic case, the condition will be a simple expression
            //(not a call) and therefore will not show up anyway.
            buildCallTree(callTree, condition.getExpression());
        }
        if (block != null) {
            block.fillCallTree(callTree);
        }
    }
    public String toString(){
        return "{switch}";
    }

}
