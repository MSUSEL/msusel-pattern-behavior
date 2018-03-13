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
    public List<UMLClassifier> getClassifiersFromImport(List<String> imports){
        List<UMLClassifier> packageClassifiers = new ArrayList<>();
        int importPointer = 0;
        String importVal = imports.get(importPointer);
        PackageNode packageTreePointer = root;
        if (!importVal.equals(packageTreePointer.getName())){
            //kick it off -- but if the roots are different then its a 3rd party lib.
            return packageClassifiers;
        }
        importPointer++;
        while (importPointer < imports.size()){
            importVal = imports.get(importPointer);
            if (importPointer == imports.size()-1){
                //end of tree, match classifier
                packageClassifiers = packageTreePointer.getClassifiers();
            }else{
                //middle of tree
                for (PackageNode nextPackageLevel : packageTreePointer.getChildren()){
                    if (importVal.equals(nextPackageLevel.getName())){
                        //found match, continue
                        importPointer++;
                        packageTreePointer = nextPackageLevel;
                    }
                }
            }
        }
        return packageClassifiers;
    }

    public UMLClassifier getClassifierFromImport(List<String> imports){
        int importPointer = 0;
        String importVal = imports.get(importPointer);
        PackageNode packageTreePointer = root;
        if (!importVal.equals(packageTreePointer.getName())){
            //kick it off -- but if the roots are different then its a 3rd party lib.
            return null;
        }
        importPointer++;
        while (importPointer < imports.size()){
            importVal = imports.get(importPointer);
            if (importPointer == imports.size()-1){
                //end of tree, match classifier
                for (UMLClassifier classifiers : packageTreePointer.getClassifiers()){
                    if (imports.get(importPointer).equals(classifiers.getName())){
                        System.out.println("Matched: " + imports + " to package level: " + packageTreePointer.getName());
                        return classifiers;
                    }
                }
            }else{
                //middle of tree
                for (PackageNode nextPackageLevel : packageTreePointer.getChildren()){
                    if (importVal.equals(nextPackageLevel.getName())){
                        //found match, continue
                        importPointer++;
                        packageTreePointer = nextPackageLevel;
                    }
                }
            }
        }
        System.out.println("no matches: " + imports);
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
