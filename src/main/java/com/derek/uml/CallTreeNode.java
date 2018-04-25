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
    private String tagName;

    @Setter
    private UMLClassifier umlClassifier;

    public CallTreeNode(T name, String tagName){
        //call tree will start with a name of the 'to' node in the tree. The name starts as a string.
        //in the second pass through the tree, once the UMLClassifiers are identified, thew name will be
        //turned into the Classifier
        this.name = name;
        this.tagName = tagName;
        children = new ArrayList<>();
    }
    public void addChild(CallTreeNode<T> child){
        children.add(child);
    }

    public void printTree() {
        printTree("", true);
    }

    private void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + name.toString() + " " + tagName);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .printTree(prefix + (isTail ?"    " : "│   "), true);
        }
    }


    public List<CallTreeNode<T>> convertMeToOrderedList(){
        //method to turn me and my children to an ordered list. child links will still be maintained in this structure.
        List<CallTreeNode<T>> asList = new ArrayList<>();
        asList.add(this);
        for (CallTreeNode<T> child : children){
            asList.addAll(child.convertMeToOrderedList());
        }
        return asList;
    }
}