package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {

    public UMLMessageGenerationUtils(){

    }

    public static void getCallTreeFromBlock(CallTreeNode<SrcMLNode> callTreeNode, SrcMLBlock block){
        //will use pass by reference to make changes to callTree.
        List<CallTreeNode<SrcMLNode>> children = callTreeNode.getChildren();
        for (SrcMLNode node : block.getChildNodeOrder()){
            //need to preserve order.
            switch (node.getElement().getNodeName()){
                case "if":
                case "elseif":
                    callTreeNode.addChild(((SrcMLIf)node).getCallTree());
                    break;
            }
        }
    }

    public static CallTreeNode<String> convertSrcMLCallTreeToString(CallTreeNode<SrcMLNode> callTreeNode){
        CallTreeNode<String> callTreeString = null;

        return callTreeString;
    }
}
