package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLExprStmt extends SrcMLNode{
    private List<SrcMLExpression> expressions;
    private List<String> operators;

    public SrcMLExprStmt(Element exprStmtEle) {
        super(exprStmtEle);
    }
    protected void parse(){
        expressions = parseExpressions();
        operators = parseOperators();
    }

}