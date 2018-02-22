package com.derek.uml;

import com.derek.uml.srcML.SrcMLNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CallTreeNode<T>{
    private T name;
    //list of children under this node. Note this is an ordered list
    private List<CallTreeNode<T>> children;

    @Setter
    private UMLClassifier umlClassifier;

    public CallTreeNode(T name){
        //call tree will start with a name of the 'to' node in the tree. The name starts as a string.
        //in the second pass through the tree, once the UMLClassifiers are identified, thew name will be
        //turned into the Classifier
        this.name = name;
        children = new ArrayList<>();
    }
    public void addChild(CallTreeNode<T> child){
        children.add(child);
    }

    //used for multiple additions for a given depth, such as if statements. Generally the list will not make up ALL of the children.
    public void addChildren(List<CallTreeNode<T>> children2){
        children.addAll(children2);
    }


    public void printSrcMLTree() {
        printSrcMLTree("", true);
    }

    private void printSrcMLTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + ((SrcMLNode)name).toString());
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printSrcMLTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .printSrcMLTree(prefix + (isTail ?"    " : "│   "), true);
        }
    }
}