package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLExpression {
    //fulfills expr<expr>:(JavaLambda|name|call|defaultCall|name|initBlock|operator|literal|JavaAnonymousClass|expr|cast|ternary)+;
    //there are 2 'name' values.. I think that is a mistake. Also, the spec doesn't consider ternary a java thing but it is because it appears in teh xml

    @Getter
    private Element expressionEle;
    @Getter
    private List<SrcMLJavaLambda> javaLambdas;
    @Getter
    private List<SrcMLName> names;
    @Getter
    private List<SrcMLCall> calls;
    @Getter
    private List<SrcMLDefault> defaultVals;
    @Getter
    private List<SrcMLBlock> initBlocks;
    @Getter
    private List<String> operators;
    @Getter
    private List<String> literals;
    @Getter
    private List<SrcMLJavaAnonymousClass> javaAnonymousClass;
    @Getter
    private List<SrcMLExpression> expressions;
    @Getter
    private List<SrcMLTernary> ternarys;

    public SrcMLExpression(Element expressionEle){
        this.expressionEle = expressionEle;
        parse();
    }

    private void parse(){
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
        List<Node> lambdaNodes = XmlUtils.getImmediateChildren(expressionEle, "lambda");
        for (Node lambda : lambdaNodes){
            javaLambdas.add(new SrcMLJavaLambda(XmlUtils.elementify(lambda)));
        }
    }
    private void parseNames(){
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(expressionEle, "name");
        for (Node name : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(name)));
        }
    }
    private void parseCalls(){
        calls = new ArrayList<>();
        List<Node> callNodes = XmlUtils.getImmediateChildren(expressionEle, "call");
        for (Node call : callNodes){
            calls.add(new SrcMLCall(XmlUtils.elementify(call)));
        }
    }
    private void parseDefaultVals(){
        defaultVals = new ArrayList<>();
        List<Node> defaultNodes = XmlUtils.getImmediateChildren(expressionEle, "default");
        for (Node default1 : defaultNodes){
            defaultVals.add(new SrcMLDefault(XmlUtils.elementify(default1)));
        }
    }
    private void parseInitBlocks(){
        initBlocks = new ArrayList<>();
        List<Node> blockNodes = XmlUtils.getImmediateChildren(expressionEle, "block");
        for (Node block : blockNodes){
            initBlocks.add(new SrcMLBlock(XmlUtils.elementify(block)));
        }
    }
    private void parseOperators(){
        operators = new ArrayList<>();
        List<Node> operatorNodes = XmlUtils.getImmediateChildren(expressionEle, "operator");
        for (Node operator : operatorNodes){
            operators.add(operator.getTextContent());
        }
    }
    private void parseLiterals(){
        literals = new ArrayList<>();
        List<Node> literalNodes = XmlUtils.getImmediateChildren(expressionEle, "literal");
        for (Node literal : literalNodes){
            literals.add(literal.getTextContent());
        }
    }
    private void parseAnonymousClasses(){
        javaAnonymousClass = new ArrayList<>();
        List<Node> anonymousNodes = XmlUtils.getImmediateChildren(expressionEle, "class");
        for (Node anonymous : anonymousNodes){
            javaAnonymousClass.add(new SrcMLJavaAnonymousClass(XmlUtils.elementify(anonymous)));
        }
    }
    private void parseExpressions(){
        expressions = new ArrayList<>();
        List<Node> expressionNodes = XmlUtils.getImmediateChildren(expressionEle, "expr");
        for (Node expression : expressionNodes){
            expressions.add(new SrcMLExpression(XmlUtils.elementify(expression)));
        }
    }
    private void parseTernarys(){
        ternarys = new ArrayList<>();
        List<Node> ternaryNodes = XmlUtils.getImmediateChildren(expressionEle, "ternary");
        for (Node ternary : ternaryNodes){
            ternarys.add(new SrcMLTernary(XmlUtils.elementify(ternary)));
        }
    }
}
