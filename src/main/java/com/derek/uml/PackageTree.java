package com.derek.uml;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//simple tree class.
@Getter
public class PackageTree {
    private PackageNode root;


    //TODO - bug here. root is org, but second iteration makes a second org as a child of root....
    public void addEntirePackage(UMLClassifier umlClassifier){
        List<String> packageStructure = umlClassifier.getResidingPackage();
        PackageNode holder = null;
        for (int i = 0; i < packageStructure.size(); i++){
            String s = packageStructure.get(i);
            if (i == 0){
                if (root == null){
                    root = new PackageNode(s, null);
                    holder = root;
                    continue;

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
        if(node == root){
            System.out.println(node.toString());
        }else if (node != null){
            for (PackageNode child : node.getChildren()){
                System.out.println(child.toString());
                printInOrder(child);
            }
        }
    }

    public PackageNode getLevelOfClassifier(UMLClassifier owningType){
        PackageNode pointer = root;
        List<String> residingPackage = owningType.getResidingPackage();
        for (int i = 0; i < residingPackage.size(); i++){
            String currentPackage = residingPackage.get(i);
            for (PackageNode n : pointer.getChildren()){
                if (n.getName().equals(currentPackage)){
                    //found match
                    pointer = n;
                    break;
                }
            }
        }
        return pointer;
    }


    //happens when an import ends in *
    public List<UMLClassifier> getClassifiersFromImport(List<String> imports, int pointer, PackageNode node){
        if (pointer == imports.size()-1){
            //base case
            return node.getClassifiers();
        }
        for (PackageNode child : node.getChildren()){
            if (imports.get(pointer).equals(child.getName())){
                //package level node we matched
                return getClassifiersFromImport(imports, pointer++, child);
            }
        }
        //if we get here, we never reached a match. This coudl be because a third party lib has a smiilar package structure
        System.out.println("no match: " + imports  + " is not in the package structure.");
        return null;
    }

    public UMLClassifier getClassifierFromImport(List<String> imports, int pointer, PackageNode node){
        if (pointer == imports.size()-1){
            //base case
            for (UMLClassifier umlClassifier : node.getClassifiers()){
                if (imports.get(pointer).equals(umlClassifier.getName())){
                    //positive match
                    return umlClassifier;
                }
            }
            //somehow did not find.. this should never happen.
            System.out.println("did not find class file even though we dug down into hte package successfully. big issue.");
            System.exit(0);
        }
        for (PackageNode child : node.getChildren()){
            if (imports.get(pointer).equals(child.getName())){
                //package level node we matched
                return getClassifierFromImport(imports, pointer++, child);
            }
        }
        //if we get here, we never reached a match. This coudl be because a third party lib has a smiilar package structure
        System.out.println("no match: " + imports  + " is not in the package structure.");
        return null;
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
