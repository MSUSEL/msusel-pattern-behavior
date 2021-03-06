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

import java.util.List;

@Getter
public class SrcMLDecl extends SrcMLNode{
    private SrcMLDataType type;
    private SrcMLName name;
    private SrcMLRange range;
    private SrcMLInit init;
    private List<String> specifiers;
    private CallTreeNode<SrcMLNode> callTree;

    public SrcMLDecl(Element declEle) {
        super(declEle);
    }
    protected void parse(){
        type = parseDataType();
        name = parseName();
        range = parseRange();
        init = parseInit();
        specifiers = parseSpecifiers();

    }
    public void buildCallTree(){
        callTree = new CallTreeNode<>(this, "decl{" + type.getName() + "}");

        if (range != null){
            buildCallTree(callTree, range.getExpression());
        }
        if (init != null){
            init.fillCallTree(callTree);
        }
    }

    public String getName(){
        if (name == null){
            //happens ocassionally, specifically on enum declarations. -- LEFT_SHIFT(Keys.Shift)
            //there is no name, just a type setting.
            return "";
        }else {
            return name.getName();
        }
    }

    public String toString(){
        return getName();
    }


}
