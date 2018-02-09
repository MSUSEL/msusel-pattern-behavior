package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.jboss.shrinkwrap.descriptor.api.Mutable;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {
    private MutableGraph<SrcMLNode> callTree;

    public static UMLSequenceDiagram getUMLSequenceDiagram(SrcMLBlock block){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLNode node : block.getChildNodeOrder()){
            //need to preserve order.
            switch (node.getElement().getNodeName()){
                case "if":
                case "elseif":
                    messages.addAll(getIfMessages(block.getIfs()));
                    break;
            }
        }
    }

    private static List<UMLMessage> getIfMessages(List<SrcMLIf> srcMLIfs){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLIf srcMLIf : srcMLIfs){
            messages.add(convertCallTreeToMessage(srcMLIf.getConditionPath()));


        }
        return messages;
    }

    private static UMLMessage convertCallTreeToMessage(MutableGraph<SrcMLNode> callTree){
        UMLClassifier from = getFromClassifier(callTree)
    }

}
