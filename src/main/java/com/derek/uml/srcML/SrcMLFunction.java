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

//class to hold important function-related info (xml 'function' element)
@Getter
public class SrcMLFunction extends SrcMLNode{

    private List<SrcMLAnnotation> annotations;
    private List<String> specifiers;
    private SrcMLDataType type;
    private SrcMLName name;
    private SrcMLParameterList parameterList;
    private List<SrcMLThrows> throws1;
    private SrcMLBlock block;
    private SrcMLDefault default1;

    private CallTreeNode<SrcMLNode> callTree;


    public SrcMLFunction(Element functionEle){
        super(functionEle);
    }

    protected void parse(){
        annotations = parseAnnotations();
        specifiers = parseSpecifiers();
        type = parseDataType();
        name = parseName();
        parameterList = parseParameterList();
        throws1 = parseThrows();
        block = parseBlock();
        //default is the only difference between 'function' and 'function_Decl' elements.. default exists in function_decl only
        default1 = parseDefault();
        callTree = new CallTreeNode<>(this, "function");
        block.fillCallTree(callTree);
    }
    public String getName(){
        return name.getName();
    }
}
