package com.derek.uml.srcML;

import com.google.common.graph.MutableGraph;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLExprStmt extends SrcMLNode{
    private List<SrcMLExpression> expressions;
    private List<String> operators;
    //for random expressions...
    private List<MutableGraph<SrcMLNode>> expressionPaths;

    public SrcMLExprStmt(Element exprStmtEle) {
        super(exprStmtEle);
    }
    protected void parse(){
        expressions = parseExpressions();
        expressionPaths = new ArrayList<>();
        for (SrcMLExpression srcMLExpression : expressions) {
            expressionPaths.add(buildCallTree(this, srcMLExpression));
        }
        operators = parseOperators();
    }

}