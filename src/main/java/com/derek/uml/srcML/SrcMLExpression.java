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
public class SrcMLExpression extends SrcMLNode{
    //fulfills expr<expr>:(JavaLambda|name|call|defaultCall|name|initBlock|operator|literal|JavaAnonymousClass|expr|cast|ternary)+;
    //there are 2 'name' values.. I think that is a mistake. Also, the spec doesn't consider ternary a java thing but it is because it appears in teh xml

    private List<SrcMLJavaLambda> javaLambdas;
    private List<SrcMLName> names;
    private List<SrcMLCall> calls;
    private List<SrcMLDefault> defaultVals;
    private List<SrcMLBlock> initBlocks;
    private List<String> operators;
    private List<String> literals;
    private List<SrcMLClass> javaAnonymousClasses;
    private List<SrcMLExpression> expressions;
    private List<SrcMLTernary> ternarys;

    public SrcMLExpression(Element expressionEle){
        super(expressionEle);
    }

    protected void parse(){
        javaLambdas = parseJavaLambdas();
        names = parseNames();
        calls = parseCalls();
        defaultVals = parseDefaults();
        initBlocks = parseBlocks();
        operators = parseOperators();
        literals = parseLiterals();
        javaAnonymousClasses = parseClasses();
        expressions = parseExpressions();
        ternarys = parseTernarys();
    }
    private List<SrcMLCall> parseCalls(){
        List<SrcMLCall> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("call")){
            nodes.add((SrcMLCall)node);
        }
        return nodes;
    }
    private List<SrcMLJavaLambda> parseJavaLambdas(){
        List<SrcMLJavaLambda> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("lambda")){
            nodes.add((SrcMLJavaLambda)node);
        }
        return nodes;
    }
    private List<SrcMLTernary> parseTernarys(){
        List<SrcMLTernary> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("ternary")){
            nodes.add((SrcMLTernary)node);
        }
        return nodes;
    }
    private List<String> parseLiterals(){
        List<String> literals = new ArrayList<>();
        List<Node> literalNodes = XmlUtils.getImmediateChildren(element, "literal");
        for (Node literalNode : literalNodes){
            literals.add(XmlUtils.elementify(literalNode).getNodeName());
        }
        return literals;
    }


}
