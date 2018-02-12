package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {
    private MutableGraph<SrcMLNode> callTree;

    public UMLMessageGenerationUtils(){
    }

    public static MutableGraph<UMLMessage> getEntireCallTree(SrcMLBlock block){
        List<MutableGraph<UMLMessage>> messages = new ArrayList<>();
        for (SrcMLNode node : block.getChildNodeOrder()){
            //need to preserve order.
            switch (node.getElement().getNodeName()){
                case "if":
                case "elseif":
                    messages.add(getIfMessages((SrcMLIf)node));
                    break;
            }
        }
    }

    private static MutableGraph<UMLMessage> getIfMessages(SrcMLIf node){
        MutableGraph<UMLMessage> conditions = convertNodeTreeToMessageTree(node.getConditionPath());
        MutableGraph<UMLMessage> trues = convertNodeTreeToMessageTree(node.getTruePath());
        MutableGraph<UMLMessage> elses = convertNodeTreeToMessageTree(node.getFalsePath());
        //might need to add more elseif logic here.. likely will need to but idk what it will look like yet
        //MutableGraph<UMLMessage> elseIfs = getEntireCallTree(node.getElseIf())
        MutableGraph<UMLMessage> combiner = joinMutableGraphs(conditions, trues);
        combiner = joinMutableGraphs(combiner, elses);
        return combiner;
    }

    private static MutableGraph<UMLMessage> convertNodeTreeToMessageTree(MutableGraph<SrcMLNode> callTree){
        UMLClassifier from = getFromClassifier(callTree)



    }

    //should have same root nodes. -- need to test this
    private static MutableGraph<UMLMessage> joinMutableGraphs(MutableGraph<UMLMessage> one, MutableGraph<UMLMessage> two){

        //this is not correct i think.
        one.nodes().addAll(two.nodes());
        return one;
    }

}
