package com.derek.uml;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import javafx.util.Pair;

import javax.management.relation.Relation;
import java.util.List;

public class UMLClassDiagram {

    //I am assuming the graph for the uml will be standard, as in g = <<V>,<E>> and e = <v1, v2>
    //while it is true that UML can do ternary relationships, which would be represented as e = <v1,v2,v3>,
    //I assume this wont happen, and if it does I will improvise, adapt, overcome
    private MutableValueGraph<UMLClassifier, Relationship> classDiagram;

    public UMLClassDiagram(){
        classDiagram = ValueGraphBuilder.undirected().build();
    }

    //add node (class with attributes/operations/ocnstructor/relationships) to graph.
    //list of pairs are the connecting classes to umlClass. Each pair is <relationship type, other class>.
    public void addClassToDiagram(UMLClass umlClass, List<Pair<Relationship, UMLClassifier>> connectorClasses){
        classDiagram.addNode(umlClass);
        for (Pair<Relationship, UMLClassifier> p : connectorClasses) {
            classDiagram.putEdgeValue(umlClass, p.getValue(), p.getKey());
        }

    }

    //add class without edges; should be used for testing primarily.
    public void addClassToDiagram(UMLClassifier umlClass){
        classDiagram.addNode(umlClass);
    }

    public MutableValueGraph<UMLClassifier, Relationship> getClassDiagram() {
        return classDiagram;
    }
}
