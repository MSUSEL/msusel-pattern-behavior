package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {

    public UMLMessageGenerationUtils(){

    }


    public static CallTreeNode<String> convertSrcMLCallTreeToString(CallTreeNode<SrcMLNode> callTree){
        CallTreeNode<String> asString = new CallTreeNode<>(callTree.getName().toString(), callTree.getTagName());
        for (CallTreeNode<SrcMLNode> child : callTree.getChildren()){
            asString.addChild(convertSrcMLCallTreeToString(child));
        }
        return asString;
    }
}
