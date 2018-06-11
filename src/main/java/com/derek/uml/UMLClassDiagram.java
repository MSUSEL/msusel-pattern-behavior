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

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UMLClassDiagram {

    //I am assuming the graph for the uml will be standard, as in g = <<V>,<E>> and e = <v1, v2>
    //while it is true that UML can do ternary relationships, which would be represented as e = <v1,v2,v3>,
    //I assume this wont happen, and if it does I will improvise, adapt, overcome
    private MutableValueGraph<UMLClassifier, Relationship> classDiagram;
    @Setter
    private PackageTree packageTree;

    public UMLClassDiagram(){
        classDiagram = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
    }

    public void addRelationshipToDiagram(UMLClassifier from, UMLClassifier to, Relationship relationship){
        classDiagram.putEdgeValue(from, to, relationship);
    }

    //add class without edges; should be used for testing primarily.
    public void addClassToDiagram(UMLClassifier umlClass) {
        classDiagram.addNode(umlClass);
    }

    public void buildPackageTree(){
        PackageTree tree = new PackageTree();
        for (UMLClassifier umlClassifier : getClassDiagram().nodes()){
            tree.addEntirePackage(umlClassifier);
        }
        this.setPackageTree(tree);
    }

}
