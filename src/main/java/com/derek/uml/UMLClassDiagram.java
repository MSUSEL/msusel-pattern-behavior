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

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import javax.management.relation.Relation;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLClassDiagram {

    //I am assuming the graph for the uml will be standard, as in g = <<V>,<E>> and e = <v1, v2>
    //while it is true that UML can do ternary relationships, which would be represented as e = <v1,v2,v3>,
    //I assume this wont happen, and if it does I will improvise, adapt, overcome
    private MutableNetwork<UMLClassifier, Relationship> classDiagram;

    @Setter
    private PackageTree packageTree;

    public UMLClassDiagram(){
        classDiagram = NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(true).build();
                //ValueGraphBuilder.directed().allowsSelfLoops(true).build();
    }

    public void addRelationshipToDiagram(UMLClassifier from, UMLClassifier to, RelationshipType relationshipType){
        Relationship newRelationship = new Relationship(from, to, relationshipType);
        classDiagram.addEdge(from, to, newRelationship);
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

    public List<UMLClassifier> getAllGeneralizationHierarchyChildren(UMLClassifier umlClassifier){
        try {
            List<UMLClassifier> children = new ArrayList<>();
            if (!classDiagram.nodes().contains(umlClassifier)) {
                //node not here, error condition
                return children;
            }
            List<UMLClassifier> immediateChildren = new ArrayList<>();
            for (UMLClassifier potentialChild : classDiagram.nodes()) {
                for (Relationship possibleEdge : classDiagram.edgesConnecting(potentialChild, umlClassifier)) {
                    if (possibleEdge.getRelationshipType() == RelationshipType.GENERALIZATION || possibleEdge.getRelationshipType() == RelationshipType.REALIZATION) {
                        if (!potentialChild.equals(umlClassifier)){
                            //might occur when a class implements a generic of its type.
                            immediateChildren.add(potentialChild);
                        }
                    }
                }
            }
            children.addAll(immediateChildren);
            for (UMLClassifier immediateChild : immediateChildren) {
                //recursively add all children
                children.addAll(getAllGeneralizationHierarchyChildren(immediateChild));
            }
            return children;
        }
        catch(StackOverflowError e){
            e.printStackTrace();;
        }
        return null;
    }

    public List<UMLClassifier> getAllGeneralizationHierarchyImmediateParents(UMLClassifier child){
        List<UMLClassifier> parents = new ArrayList<>();
        if (!classDiagram.nodes().contains(child)) {
            //node not here, error condition
            return parents;
        }
        List<UMLClassifier> immediateParents = new ArrayList<>();
        for (UMLClassifier potentialParent : classDiagram.nodes()){
            for (Relationship possibleEdge : classDiagram.edgesConnecting(child, potentialParent)){
                if (possibleEdge.getRelationshipType() == RelationshipType.GENERALIZATION || possibleEdge.getRelationshipType() == RelationshipType.REALIZATION){
                    immediateParents.add(potentialParent);
                }
            }
        }
        parents.addAll(immediateParents);
        //if I want to add ALL parents, I would recursively add it here... I won't go up the entire way though.
        return parents;
    }

    /***
     * method to retun the neighbors of input starting nodes. (both afferent and efferent
     * @param startingNodes
     * @return
     */
    public List<UMLClassifier> getNeighbors(List<UMLClassifier> startingNodes){
        List<UMLClassifier> neighbors = new ArrayList<>();
        for (UMLClassifier umlClassifier : startingNodes){
            neighbors.addAll(getNeighborsSingle(umlClassifier));
        }
        return neighbors;
    }

    public List<UMLClassifier> getNeighborsSingle(UMLClassifier startingNode){
        List<UMLClassifier> neighbors = new ArrayList<>();

        for (UMLClassifier potentialNeighbor : classDiagram.nodes()) {
            for (Relationship possibleEdge : classDiagram.edgesConnecting(startingNode, potentialNeighbor)) {
                if (possibleEdge.getRelationshipType() == RelationshipType.ASSOCIATION || possibleEdge.getRelationshipType() == RelationshipType.DEPENDENCY) {
                    if (!potentialNeighbor.equals(startingNode)){
                        //might occur when a class implements a generic of its type.
                        neighbors.add(potentialNeighbor);
                    }
                }
            }
            //think I need this for both directions.
            for (Relationship possibleEdge : classDiagram.edgesConnecting(potentialNeighbor, startingNode)){
                if (possibleEdge.getRelationshipType() == RelationshipType.ASSOCIATION || possibleEdge.getRelationshipType() == RelationshipType.DEPENDENCY) {
                    if (!potentialNeighbor.equals(startingNode)){
                        //might occur when a class implements a generic of its type.
                        neighbors.add(potentialNeighbor);
                    }
                }
            }
        }
        return neighbors;
    }


    public Relationship getRelationshipFromClassDiagram(UMLClassifier from, UMLClassifier to, RelationshipType relationshipType){
        for (Relationship r : classDiagram.edgesConnecting(from, to)){
            if (r.getRelationshipType() == relationshipType){
                return r;
            }
        }
        //should not get here.

        System.out.println("was not able to find relationship, when this relationship should already exist.");
        System.out.println("Exiting");
        System.exit(0);
        return null;
    }

}
