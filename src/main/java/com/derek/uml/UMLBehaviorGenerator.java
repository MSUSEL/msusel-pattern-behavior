package com.derek.uml;

import com.google.common.graph.MutableValueGraph;

public class UMLBehaviorGenerator {
    private MutableValueGraph<UMLClassifier, Relationship> classDiagram;
    private UMLClassifier scope;
    private String defaultFunction = "startProfile";

    public UMLBehaviorGenerator(UMLClassDiagram umlClassDiagram){
        this.classDiagram = umlClassDiagram.getClassDiagram();
        scope = getScope("FirefoxBinary");
        buildBehavioralUML(scope);
    }

    private void buildBehavioralUML(UMLClassifier scope){
        buildSequenceDiagram(scope, matchFunction(defaultFunction));
    }

    private void buildSequenceDiagram(UMLClassifier scope, UMLOperation function){
        function.getCallTreeString().printTree();
    }

    private UMLOperation matchFunction(String functionName){
        for (UMLOperation operation : scope.getOperations()){
            if (operation.getName().equals(functionName)){
                return operation;
            }
        }
        return null;
    }

    private UMLClassifier getScope(String searcher){
        UMLClassifier toRet = null;
        for (UMLClassifier umlClassifier : classDiagram.nodes()){
            if (umlClassifier.getName().equals(searcher)){
                return umlClassifier;
            }
        }
        return toRet;
    }
}
