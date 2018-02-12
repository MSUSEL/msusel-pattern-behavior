package com.derek.uml;

import com.google.common.graph.MutableValueGraph;

public class UMLBehaviorGenerator {
    MutableValueGraph<UMLClassifier, Relationship> classDiagram;

    public UMLBehaviorGenerator(MutableValueGraph<UMLClassifier, Relationship> classDiagram, UMLClassifier scope){
        this.classDiagram = classDiagram;
        buildBehavioralUML(scope);
    }


    private void buildBehavioralUML(UMLClassifier scope){
        buildSequenceDiagram(scope);
    }


    private void buildSequenceDiagram(UMLClassifier scope){
        for (UMLClassifier umlClassifier : classDiagram.nodes()){
            if (scope.equals())
        }
    }
}
