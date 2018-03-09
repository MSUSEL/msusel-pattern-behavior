package com.derek.uml;

import com.google.common.graph.MutableValueGraph;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UMLBehaviorGenerator {
    private MutableValueGraph<UMLClassifier, Relationship> classDiagram;
    private UMLClassifier scopeClassifier;
    private UMLOperation scopeOperation;
    @Setter
    private String defaultFunction = "startProfile";
    @Setter
    private String defaultClass = "FirefoxBinary";

    public UMLBehaviorGenerator(UMLClassDiagram umlClassDiagram){
        this.classDiagram = umlClassDiagram.getClassDiagram();
        scopeClassifier = getClassScope(defaultClass);
        scopeOperation = matchFunction(defaultFunction);

        buildSequenceDiagram(scopeClassifier, scopeOperation);
    }

    private void buildSequenceDiagram(UMLClassifier scope, UMLOperation function){
        function.getCallTreeString().printTree();
        CallTreeNode<String> root = function.getCallTreeString();
        System.out.println(root);
        for (CallTreeNode<String> child : root.getChildren()){
            if (child.getName().contains("{")){
                //controlling character, such as '{if}'

            }else{
                //todo
                UMLClassifier calledType = giveMeThatType(scope, child.getName());

            }
        }
    }

    private UMLOperation matchFunction(String functionName){
        for (UMLOperation operation : scopeClassifier.getOperations()){
            if (operation.getName().equals(functionName)){
                return operation;
            }
        }
        return null;
    }

    private UMLClassifier getClassScope(String searcher){
        UMLClassifier toRet = null;
        for (UMLClassifier umlClassifier : classDiagram.nodes()){
            if (umlClassifier.getName().equals(searcher)){
                return umlClassifier;
            }
        }
        return toRet;
    }

    private UMLClassifier giveMeThatType(UMLClassifier initialScope, String searchString){
        return null;
    }
}
