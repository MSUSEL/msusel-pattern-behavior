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


    public SrcMLFunction(Element functionEle){
        super(functionEle);
    }

    protected void parse(){
        parseAnnotations();
        parseSpecifiers();
        parseType();
        parseName();
        parseParameterList();
        parseThrows();
        parseBlock();
        //default is the only difference between 'function' and 'function_Decl' elements.. default exists in function_decl only
        parseDefault();
    }

    private void parseAnnotations(){
        annotations = new ArrayList<>();
        List<Node> annotationNodes = XmlUtils.getImmediateChildren(element, "annotation");
        for (Node annotationNode : annotationNodes){
            //can have multiple annotations strings (@getter @setter, for example)
            annotations.add(new SrcMLAnnotation(XmlUtils.elementify(annotationNode)));
        }
    }
    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(element, "specifier");
        for (Node specifier : specifierNodes){
            //can have any number of specifier strings (public abstract)
            specifiers.add(specifier.getTextContent());
        }
    }
    private void parseType(){
        List<Node> typeNodes = XmlUtils.getImmediateChildren(element, "type");
        for (Node typeNode : typeNodes){
            type = new SrcMLDataType(XmlUtils.elementify(typeNode));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "name");
        for (Node nameNode : nameNodes){
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseParameterList(){
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(element, "parameter_list");
        for (Node parameterNode : parameterNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }
    private void parseThrows(){
        throws1 = new ArrayList<>();
        List<Node> throwsNodes = XmlUtils.getImmediateChildren(element, "throws");
        for (Node throwsNode : throwsNodes){
            throws1.add(new SrcMLThrows(XmlUtils.elementify(throwsNode)));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(element, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
    private void parseDefault(){
        List<Node> defaultNodes = XmlUtils.getImmediateChildren(element, "default");
        for (Node defaultNode : defaultNodes){
            default1 = new SrcMLDefault(XmlUtils.elementify(defaultNode));
        }
    }
    public String getName(){
        return name.getName();
    }
}
