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
        //function.getCallTreeString().printTree();
        CallTreeNode<String> root = function.getCallTreeString();
        UMLLifeline lifeLineScope = new UMLLifeline(scope);
        lifeLineScope.addMessageToLifeline(new UMLMessage("init", null, scope));
        for (CallTreeNode<String> child : root.getChildren()){
            String operator = child.getTagName();
            UMLClassifier type;
            //lots of this is commented out because I don't techincally need to use if for research purposes. IT is mostly type matching

            if (child.getName().contains("//.")){
                //multiple calls --> as in foo.bar.foo2
                //I need to separate calls.
            }
            if (operator.contains("decl")){
                //get type
//                String stringType = getTypeFromDeclaration(child.getTagName());
//                type = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, scope, stringType);
//                addMessageIfKnown(lifeLineScope, scope, type, "declaration");
            }

            if (operator.contains("{")){
                //controlling character, such as '{if}'

            }else{
                //operator is a call
//                if (child.getName().equals("cmdArray.addAll")){
//                    System.out.println("null called type from: " + scope + " " + child.getName());
//
//                }
//                UMLClassifier calledType = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, scope, child.getName());
//                addMessageIfKnown(lifeLineScope, scope, calledType, child.getName());

            }
        }
    }

    private void addMessageIfKnown(UMLLifeline lifeLine, UMLClassifier from, UMLClassifier to, String messageName){
        if (!to.getIdentifier().equals("unknown") && !to.getIdentifier().equals("thirdPartyClass")) {
            lifeLine.addMessageToLifeline(new UMLMessage(messageName, from, to));
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
