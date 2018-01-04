package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class SrcMLInit {
    //there are a few different types of init, but they generally have the form: (decl|expr)(","(decl|expr))* or some degree
    @Getter
    private Element initEle;
    @Getter
    private List<SrcMLDecl> declarations;
    @Getter
    private List<SrcMLExpression> expressions;

    public SrcMLInit(Element initEle) {
        this.initEle = initEle;
        parse();
    }
    private void parse(){
        parseDeclarations();
        parseExpressions();
    }
    private void parseDeclarations(){
        declarations = new ArrayList<>();
        List<Node> declNodes = XmlUtils.getImmediateChildren(initEle, "decl");
        for (Node declNode : declNodes){
            declarations.add(new SrcMLDecl(XmlUtils.elementify(declNode)));
        }
    }
    private void parseExpressions(){
        expressions = new ArrayList<>();
        List<Node> exprNodes = XmlUtils.getImmediateChildren(initEle, "expr");
        for (Node exprNode : exprNodes){
            expressions.add(new SrcMLExpression(XmlUtils.elementify(exprNode)));
        }
    }

}
