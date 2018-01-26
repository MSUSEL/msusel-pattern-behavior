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
public class SrcMLDecl extends SrcMLNode{
    private SrcMLDataType type;
    private SrcMLName name;
    private SrcMLRange range;
    private SrcMLInit init;
    private List<String> specifiers;

    public SrcMLDecl(Element declEle) {
        super(declEle);
    }
    protected void parse(){
        parseDataType();
        parseName();
        parseRange();
        parseInit();
        parseSpecifiers();
    }
    private void parseDataType(){
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
    private void parseRange(){
        List<Node> rangeNodes = XmlUtils.getImmediateChildren(element, "range");
        for (Node rangeNode : rangeNodes){
            range = new SrcMLRange(XmlUtils.elementify(rangeNode));
        }
    }
    private void parseInit(){
        List<Node> initNodes = XmlUtils.getImmediateChildren(element, "init");
        for (Node initNode : initNodes){
            init = new SrcMLInit(XmlUtils.elementify(initNode));
        }
    }
    private void parseSpecifiers(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(element, "specifier");
        for (Node specifierNode : specifierNodes){
            specifiers.add(specifierNode.getTextContent());
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

    @Getter
    public class SrcMLRange extends SrcMLNode{
        //only used by decl and is a pretty simple class
        private SrcMLExpression expression;

        public SrcMLRange(Element rangeEle) {
            super(rangeEle);
        }
        protected void parse(){
            parseExpression();
        }
        private void parseExpression(){
            List<Node> expressionNodes = XmlUtils.getImmediateChildren(element, "expr");
            for (Node expressionNode : expressionNodes){
                //can have 0 or 1
                expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
            }
        }
    }
}
