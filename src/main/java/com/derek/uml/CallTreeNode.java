/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CallTreeNode<T> {
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