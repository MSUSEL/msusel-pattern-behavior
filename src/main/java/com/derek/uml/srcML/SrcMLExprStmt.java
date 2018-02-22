package com.derek.uml.srcML;

import com.derek.uml.CallTreeNode;
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

    public SrcMLExprStmt(Element exprStmtEle) {
        super(exprStmtEle);
    }
    protected void parse(){
        expressions = parseExpressions();
        operators = parseOperators();
    }

    public void fillCallTree(CallTreeNode<SrcMLNode> root){
        for (SrcMLExpression srcMLExpression : expressions) {
            buildCallTree(root, srcMLExpression);
        }
    }

}