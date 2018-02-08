package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {
    private MutableGraph<SrcMLNode> callTree;


    private void buildCallTree(SrcMLNode parent, SrcMLExpression expression){
        callTree = GraphBuilder.directed().allowsSelfLoops(false).build();
        fillCallTree(callTree, null, this);
    }

    private void fillCallTree(MutableGraph<SrcMLNode> callTree, SrcMLNode parent, SrcMLExpression childNode){
        List<SrcMLCall> calls = childNode.getCalls();
        for (SrcMLCall call : calls) {
            if (callTree.nodes().size() == 0){
                //first time through forest, add a new root as a caller..
                parent = call;
                callTree.addNode(parent);
            }else{
                //we already have a parent, add the call as a child of the parent. this will be repeated for each call in the expr.
                callTree.putEdge(parent, call);
            }
            //regardless, fill the rest of the forest with edges for each argument.
            for (SrcMLArgument argument : call.getArgumentList().getArguments()) {
                fillCallTree(callTree, call, argument.getExpression());
            }
        }
    }
}
