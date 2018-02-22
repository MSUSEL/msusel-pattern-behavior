package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {
    private MutableGraph<SrcMLNode> callTree;

    public UMLMessageGenerationUtils(){

    }

    public static void getCallTreeFromBlock(CallTreeNode callTreeNode, SrcMLBlock block){
        //will use pass by reference to make changes to callTree.
        List<CallTreeNode> children = callTreeNode.getChildren();
        for (SrcMLNode node : block.getChildNodeOrder()){
            //need to preserve order.
            switch (node.getElement().getNodeName()){
                case "if":
                case "elseif":
                    callTreeNode.addChildren(getIfMessages((SrcMLIf)node));
                    break;
            }
        }
    }

    private static List<CallTreeNode<SrcMLNode>> getIfMessages(SrcMLIf node){
        CallTreeNode<SrcMLNode> conditionRoot = node.getConditionPath();
        CallTreeNode<SrcMLNode> trueRoot = node.getTruePath();
        CallTreeNode<SrcMLNode> falseRoot = node.getFalsePath();
        //might need to add more elseif logic here.. likely will need to but idk what it will look like yet
        List<CallTreeNode<SrcMLNode>> ifMessages = new ArrayList<>();
        ifMessages.add(conditionRoot);
        ifMessages.add(trueRoot);
        ifMessages.add(falseRoot);
        return ifMessages;
    }
}
