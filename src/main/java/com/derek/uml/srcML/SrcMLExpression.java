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
        parseJavaLambdas();
        parseNames();
        parseCalls();
        parseDefaultVals();
        parseInitBlocks();
        parseOperators();
        parseLiterals();
        parseAnonymousClasses();
        parseExpressions();
        parseTernarys();
    }

    private void parseJavaLambdas(){
        javaLambdas = new ArrayList<>();
        List<Node> lambdaNodes = XmlUtils.getImmediateChildren(element, "lambda");
        for (Node lambda : lambdaNodes){
            javaLambdas.add(new SrcMLJavaLambda(XmlUtils.elementify(lambda)));
        }
    }
    private void parseNames(){
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(element, "name");
        for (Node name : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(name)));
        }
    }
    private void parseCalls(){
        calls = new ArrayList<>();
        List<Node> callNodes = XmlUtils.getImmediateChildren(element, "call");
        for (Node call : callNodes){
            calls.add(new SrcMLCall(XmlUtils.elementify(call)));
        }
    }
    private void parseDefaultVals(){
        defaultVals = new ArrayList<>();
        List<Node> defaultNodes = XmlUtils.getImmediateChildren(element, "default");
        for (Node default1 : defaultNodes){
            defaultVals.add(new SrcMLDefault(XmlUtils.elementify(default1)));
        }
    }
    private void parseInitBlocks(){
        initBlocks = new ArrayList<>();
        List<Node> blockNodes = XmlUtils.getImmediateChildren(element, "block");
        for (Node block : blockNodes){
            initBlocks.add(new SrcMLBlock(XmlUtils.elementify(block)));
        }
    }
    private void parseOperators(){
        operators = new ArrayList<>();
        List<Node> operatorNodes = XmlUtils.getImmediateChildren(element, "operator");
        for (Node operator : operatorNodes){
            operators.add(operator.getTextContent());
        }
    }
    private void parseLiterals(){
        literals = new ArrayList<>();
        List<Node> literalNodes = XmlUtils.getImmediateChildren(element, "literal");
        for (Node literal : literalNodes){
            literals.add(literal.getTextContent());
        }
    }
    private void parseAnonymousClasses(){
        javaAnonymousClasses = new ArrayList<>();
        List<Node> anonymousNodes = XmlUtils.getImmediateChildren(element, "class");
        for (Node anonymous : anonymousNodes){
            javaAnonymousClasses.add(new SrcMLClass(XmlUtils.elementify(anonymous)));
        }
    }
    private void parseExpressions(){
        expressions = new ArrayList<>();
        List<Node> expressionNodes = XmlUtils.getImmediateChildren(element, "expr");
        for (Node expression : expressionNodes){
            expressions.add(new SrcMLExpression(XmlUtils.elementify(expression)));
        }
    }
    private void parseTernarys(){
        ternarys = new ArrayList<>();
        List<Node> ternaryNodes = XmlUtils.getImmediateChildren(element, "ternary");
        for (Node ternary : ternaryNodes){
            ternarys.add(new SrcMLTernary(XmlUtils.elementify(ternary)));
        }
    }

}
