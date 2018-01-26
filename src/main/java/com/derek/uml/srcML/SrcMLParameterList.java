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

import javafx.util.Pair;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLParameterList extends SrcMLNode{
    //similar to SrcMLArgumentList, I'm not sure how much of this I will need, but I'm creatin gthe class just in case.
    private List<SrcMLParameter> parameters;

    public SrcMLParameterList(Element parameterListEle){
        super(parameterListEle);
    }
    protected void parse(){
        parseParameters();
    }
    private void parseParameters(){
        parameters = new ArrayList<>();
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(element, "parameter");
        for (Node parameterNode : parameterNodes){
            parameters.add(new SrcMLParameter(XmlUtils.elementify(parameterNode)));
        }
    }

    @Getter
    public class SrcMLParameter extends SrcMLNode{
        //after reviewing examples of parameterlist output, it appears that the documentation for this is wrong..
        //it appears that the <parameter> element ALWAYS has a <decl> element under it and not a <type> element
        private SrcMLDecl decl;
        private SrcMLDataType type;

        public SrcMLParameter(Element parameterEle) {
            super(parameterEle);
        }
        protected void parse(){
            parseDecl();
            parseType();
        }
        private void parseDecl(){
            List<Node> declNodes = XmlUtils.getImmediateChildren(element, "decl");
            for (Node declNode : declNodes){
                decl = new SrcMLDecl(XmlUtils.elementify(declNode));
            }
        }
        private void parseType(){
            List<Node> typeNodes = XmlUtils.getImmediateChildren(element, "type");
            for (Node typeNode : typeNodes){
                type = new SrcMLDataType(XmlUtils.elementify(typeNode));
            }
        }
    }

}
