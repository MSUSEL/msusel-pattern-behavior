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
import lombok.Getter;
import lombok.Setter;

@Getter
public class UMLBehaviorGenerator {
    private UMLClassDiagram umlClassDiagram;
    private UMLClassifier scopeClassifier;
    private UMLOperation scopeOperation;
    @Setter
    private String defaultFunction = "figureInvalidated";
    //"startProfile"; //used for selenium tests
    @Setter
    private String defaultClass = "StandardDrawing";
            //"FirefoxBinary"; //used for selenium tests

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
