/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml;

import com.derek.Main;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//simple tree class.
@Getter
public class PackageTree {
    @Setter
    private PackageNode root;

    public void addEntirePackage(UMLClassifier umlClassifier) {

        List<String> packageStructure = umlClassifier.getResidingPackage();
        PackageNode holder = root;
        for (int i = 0; i < packageStructure.size(); i++) {
            String s = packageStructure.get(i);
            if (root == null) {
                root = new PackageNode(s, null);
                holder = root;
                continue;
            }
            if (i == 0) {
                //first time through, so we don't need to match anything.. btw this is assuming that any children under this are children of root.
                continue;
            }

            UMLClassifier isLeafPackage = null;
            if (i == packageStructure.size() - 1) {
                //last one.
                isLeafPackage = umlClassifier;
            }

            PackageNode potentialChild = packageExistsOfChild(holder, s);
            if (potentialChild == null) {
                PackageNode newChild = new PackageNode(s, isLeafPackage);
                holder.addChild(newChild);
                holder = newChild;
            } else {
                //found a child already;
                potentialChild.addClassifierAtThisLevel(isLeafPackage);
                holder = potentialChild;
            }
            //we have package already, continue;
        }
    }


    private PackageNode packageExistsOfChild(PackageNode node, String packageSubName) {
        if (node.getChildren() != null) {
            for (PackageNode child : node.getChildren()) {
                if (child.getName().equals(packageSubName)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void printInOrder(PackageNode node) {
        if (node == root) {
            System.out.println(node.toString());
        } else if (node != null) {
            for (PackageNode child : node.getChildren()) {
                System.out.println(child.toString());
                printInOrder(child);
            }
        }
    }

    public PackageNode getLevelOfClassifier(UMLClassifier owningType) {
        PackageNode pointer = root;
        List<String> residingPackage = owningType.getResidingPackage();
        for (int i = 0; i < residingPackage.size(); i++) {
            String currentPackage = residingPackage.get(i);
            for (PackageNode n : pointer.getChildren()) {
                if (n.getName().equals(currentPackage)) {
                    //found match
                    pointer = n;
                    break;
                }
            }
        }
        return pointer;
    }


    //happens when an import ends in *
    public List<UMLClassifier> getClassifiersFromImport(List<String> imports, int pointer, PackageNode node) {
        if (pointer == imports.size() - 2) {
            //base case
            return node.getClassifiers();
        }
        for (PackageNode child : node.getChildren()) {
            if (imports.get(pointer + 1).equals(child.getName())) {
                //package level node we matched
                return getClassifiersFromImport(imports, pointer + 1, child);
            }
        }
        //if we get here, we never reached a match
        return null;
    }


    public UMLClassifier getClassifier(List<String> importer, int pointer, PackageNode node) {
        if (pointer == importer.size() - 2) {
            for (UMLClassifier umlClassifier : node.getClassifiers()) {
                if (umlClassifier.getName().equals(importer.get(importer.size() - 1))) {
                    return umlClassifier;
                }
            }
        } else {
            if (pointer == 0) {
                if (!importer.get(pointer).equals(root.getName())) {
                    //root is different from package root.
                    return null;
                }
            }
            pointer++;
            for (PackageNode child : node.getChildren()) {
                if (importer.get(pointer).equals(child.getName())) {
                    //package level node we matched
                    return getClassifier(importer, pointer, child);
                }
            }
        }
        //search in current node for match.
        if (node.getClassifiers() != null) {
            for (UMLClassifier umlClassifier : node.getClassifiers()) {
                if (umlClassifier.getName().equals(importer.get(importer.size() - 1))) {
                    return umlClassifier;
                }
            }
        }
        //if we get here, we never reached a match. This coudl be because a third party lib has a smiilar package structure
        return null;
    }

    @Getter
    public class PackageNode {
        private String name;
        private List<PackageNode> children;
        private List<UMLClassifier> classifiers;

        public PackageNode(String name, UMLClassifier umlClassifier) {
            this.name = name;
            classifiers = new ArrayList<>();
            children = new ArrayList<>();
            addClassifierAtThisLevel(umlClassifier);
        }

        public void addChild(PackageNode toAdd) {
            children.add(toAdd);
        }

        public void addClassifierAtThisLevel(UMLClassifier umlClassifier) {
            if (umlClassifier != null) {
                classifiers.add(umlClassifier);
            }
        }

        public String toString() {
            String s = name;
            for (UMLClassifier umlClassifier : classifiers) {
                s += "\t" + umlClassifier.getName() + "\n";
            }
            return s;
        }
    }

}
