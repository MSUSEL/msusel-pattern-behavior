package com.derek.uml;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//simple tree class.
@Getter
public class PackageTree {
    private PackageNode root;

    public void addEntirePackage(UMLClassifier umlClassifier){
        List<String> packageStructure = umlClassifier.getResidingPackage();
        PackageNode holder = null;
        for (int i = 0; i < packageStructure.size(); i++){
            String s = packageStructure.get(i);
            if (i == 0){
                if (root == null){
                    root = new PackageNode(s, null);
                }
                //first iteration, need this to kick things off
                holder = root;
            }
            UMLClassifier isLeafPackage = null;
            if (i == packageStructure.size() - 1){
                //last one.
                isLeafPackage = umlClassifier;
            }

            PackageNode potentialChild = packageExistsOfChild(holder, s);
            if (potentialChild == null){
                PackageNode newChild = new PackageNode(s, isLeafPackage);
                holder.addChild(newChild);
                holder = newChild;
            }else{
                //found a child already;
                potentialChild.addClassifierAtThisLevel(isLeafPackage);
                holder = potentialChild;
            }
            //we have package already, continue;

        }

    }

    private PackageNode packageExistsOfChild(PackageNode node, String packageSubName){
        if (node.getChildren() != null) {
            for (PackageNode child : node.getChildren()) {
                if (child.getName().equals(packageSubName)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void printInOrder(PackageNode node){
        if (node != null){
            for (PackageNode child : node.getChildren()){
                System.out.println(child.toString());
                printInOrder(child);
            }
        }
    }

    @Getter
    public class PackageNode{
        private String name;
        private List<PackageNode> children;
        private List<UMLClassifier> classifiers;
        public PackageNode(String name, UMLClassifier umlClassifier){
            this.name = name;
            classifiers = new ArrayList<>();
            children = new ArrayList<>();
            addClassifierAtThisLevel(umlClassifier);
        }
        public void addChild(PackageNode toAdd){
            children.add(toAdd);
        }
        public void addClassifierAtThisLevel(UMLClassifier umlClassifier){
            if (umlClassifier != null) {
                classifiers.add(umlClassifier);
            }
        }
        public String toString(){
            String s = name;
            for (UMLClassifier umlClassifier : classifiers) {
                s += "\t" + umlClassifier.getName() + "\n";
            }
            return s;
        }

    }


}
