package com.derek.uml;

import com.google.common.graph.ValueGraph;

import java.util.List;

public class UMLClassDiagram {

    //I am assuming the graph for the uml will be standard, as in g = <<V>,<E>> and e = <v1, v2>
    //while it is true that UML can do ternary relationships, which would be represented as e = <v1,v2,v3>,
    //I assume this wont happen, and if it does I will improvise, adapt, overcome
    private ValueGraph<UMLClass, Relationship> classDiagram;




}
