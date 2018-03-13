package com.derek.uml;

import com.google.common.graph.MutableValueGraph;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UMLBehaviorGenerator {
    private UMLClassDiagram umlClassDiagram;
    private UMLClassifier scopeClassifier;
    private UMLOperation scopeOperation;
    @Setter
    private String defaultFunction = "startProfile";
    @Setter
    private String defaultClass = "FirefoxBinary";

    public UMLBehaviorGenerator(UMLClassDiagram umlClassDiagram){
        this.umlClassDiagram = umlClassDiagram;
        scopeClassifier = getClassScope(defaultClass);
        scopeOperation = matchFunction(defaultFunction);

        buildSequenceDiagram(scopeClassifier, scopeOperation);
    }

    private void buildSequenceDiagram(UMLClassifier scope, UMLOperation function){
        function.getCallTreeString().printTree();
        CallTreeNode<String> root = function.getCallTreeString();
        System.out.println(root);
        for (CallTreeNode<String> child : root.getChildren()){
            String operator = child.getTagName();
            UMLClassifier type;
            if (child.getTagName().contains("decl")){
                //get type
                String stringType = getTypeFromDeclaration(child.getTagName());
                type = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, scope, stringType);
            }

            if (child.getName().contains("{")){
                //controlling character, such as '{if}'

            }else{
                UMLClassifier calledType = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, scope, child.getName());

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
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            if (umlClassifier.getName().equals(searcher)){
                return umlClassifier;
            }
        }
        return toRet;
    }



    private String getTypeFromDeclaration(String decl){
        int start = decl.indexOf("{");
        int end = decl.lastIndexOf("}");
        return decl.substring(start+1, end);
    }
}
