package com.derek.uml;

import com.google.common.graph.MutableValueGraph;

public class UMLBehaviorGenerator {
    MutableValueGraph<UMLClassifier, Relationship> classDiagram;

    public UMLBehaviorGenerator(UMLClassDiagram umlClassDiagram, UMLClassifier scope){
        this.classDiagram = umlClassDiagram.getClassDiagram();
        buildBehavioralUML(scope);
    }

    private void buildBehavioralUML(UMLClassifier scope){
        buildSequenceDiagram(scope);
    }

    private void buildSequenceDiagram(UMLClassifier scope){
        for (UMLClassifier umlClassifier : classDiagram.nodes()){
            //
        }
    }
}
