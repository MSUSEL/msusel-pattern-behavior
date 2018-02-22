package com.derek.uml;

import com.derek.uml.UMLClassifier;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CallTree {

    private CallTreeNode root;

    public CallTree(CallTreeNode rootName){
        this.root = new CallTreeNode(rootName);
    }

}
