package com.derek.uml;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//simple tree class.
@Getter
public class PackageTree {
    private PackageNode root;

    public void addNode(String name, String parentName, UMLClassifier umlClassifier){
        if (root == null){
            root = new PackageNode(name, umlClassifier);
        }else{
            PackageNode parentObj = getParentFromString(root, parentName);
            if (!nodeExistsInGraph(root, name)) {
                //does node already exist?
                parentObj.addChild(new PackageNode(name, umlClassifier));
            }
            //otherwise, i don't care.
        }
    }
    private boolean nodeExistsInGraph(PackageNode node, String name){
        if (node.getName().equals(name)){
            return true;
        }else{
            if (node.getChildren() != null) {
                for (PackageNode child : node.getChildren()) {
                    return nodeExistsInGraph(child, name);
                }
            }
        }
        return false;
    }
    private PackageNode getParentFromString(PackageNode packageNode, String parentName){
        if (packageNode.getName().equals(parentName)){
            return packageNode;
        }else{
            for (PackageNode child : packageNode.getChildren()) {
                return getParentFromString(child, parentName);
            }
        }
        return null;
    }

    public String printInOrder(PackageNode node){
        if (node != null){
            for (PackageNode child : node.getChildren()){
                System.out.println(child.toString());
                return printInOrder(child);
            }
        }
        return null;
    }

    @Getter
    public class PackageNode{
        private String name;
        private List<PackageNode> children;
        private List<UMLClassifier> classifiers;
        public PackageNode(String name, UMLClassifier umlClassifier){
            this.name = name;
            addClassifierAtThisLevel(umlClassifier);
        }
        public void addChild(PackageNode toAdd){
            if (children == null){
                children = new ArrayList<>();
            }
            children.add(toAdd);
        }
        public void addClassifierAtThisLevel(UMLClassifier umlClassifier){
            if (classifiers == null){
                classifiers = new ArrayList<>();
            }
            classifiers.add(umlClassifier);
        }
        public String toString(){
            String s = name;
            for (UMLClassifier umlClassifier : classifiers){
                s += "\t" + umlClassifier.getName() + "\n";
            }
            return s;
        }

    }


}
