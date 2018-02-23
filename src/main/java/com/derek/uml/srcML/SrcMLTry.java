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

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLTry extends SrcMLNode{
    private SrcMLInit init;
    private SrcMLBlock block;
    private List<SrcMLCatch> catches;
    private List<SrcMLFinally> finallies;

    private CallTreeNode<SrcMLNode> callTree;

    public SrcMLTry(Element tryEle) {
        super(tryEle);
    }
    protected void parse(){
        init = parseInit();
        block = parseBlock();
        catches = parseCatches();
        finallies = parseFinallies();

        callTree = new CallTreeNode<>(this, "try");
        if (init != null) {
            //will rarely happen, but it might -- see resource statement of java 7
            callTree.addChild(init.getCallTree());
        }
        block.fillCallTree(callTree);
        for (SrcMLCatch srcMLCatch : catches){
            srcMLCatch.fillCallTree(callTree);
        }
    }
    private List<SrcMLCatch> parseCatches(){
        List<SrcMLCatch> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("catch")){
            nodes.add((SrcMLCatch)node);
        }
        return nodes;
    }

}
