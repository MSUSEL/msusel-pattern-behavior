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
package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import com.google.common.graph.EndpointPair;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PatternMapper {

    protected PatternInstance pi;
    protected UMLClassDiagram umlClassDiagram;

    public PatternMapper(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        this.pi = pi;
        this.umlClassDiagram = umlClassDiagram;
        this.mapToUML();
    }


    /***
     * abstract method that transforms the textual reprensetation of a pattern instance (PI object) to the uml representation.
     *
     */
    protected abstract void mapToUML();


    protected UMLClassifier getClassifierFromString(String majorRoleValue){
        return umlClassDiagram.getPackageTree().getClassifier(convertStringToPackagedString(majorRoleValue), 0, umlClassDiagram.getPackageTree().getRoot());
    }

    protected UMLClassifier getOneMajorRole(PatternInstance pi){
        return getClassifierFromString(pi.getValueOfMajorRole(pi));
    }

    protected UMLClassifier getSecondMajorRole(PatternInstance pi){
        return getClassifierFromString(pi.getValueOfSecondMajorRole(pi));
    }

    protected List<String> convertStringToPackagedString(String value){
        List<String> toRet = new ArrayList<>();
        String[] splitter = value.split("\\.");
        for (String s : splitter){
            toRet.add(s);
        }
        return toRet;
    }

    private UMLOperation matchOperation(UMLClassifier umlClassifier, String operationName, String returnType, List<String> params){
        //greedily try to find classifier - by removing package info. If this fails then just kill it.
        if (returnType.contains(".")) {
            String[] splitter = returnType.split("\\.");
            return matchOperation(umlClassifier, operationName, splitter[splitter.length-1], params);
        }
        for (UMLOperation op : umlClassifier.getOperations()){
            if (op.getName().equals(operationName)){
                //will work for most things but we also need ot check params and return type (basically check method signature)
                if (op.getStringReturnDataType().equals(returnType)){
                    boolean foundDifference = false;
                    if (params.size() != op.getParameters().size()){
                        //same method name, but different number of params.. so different method signature. break and check other ops.
                        break;
                    }
                    for (int i = 0; i < params.size(); i++){
                        //param order needs to be preserved too
                        String param = params.get(i);
                        if (param.contains("[")) {
                            //list, need to remove brackets
                            param = param.replace("[","");
                            param = param.replace("]","");
                        }
                        if (!param.equals(op.getParameters().get(i).getName())){
                            foundDifference = true;
                            break;
                        }
                    }
                    //same method signature!
                    if (foundDifference == false){
                        return op;
                    }
                }
            }
        }
        if (umlClassifier.getIdentifier().equals("class")){
            //need to check constructors if this is the case.
            UMLClass umlClass = (UMLClass)umlClassifier;
            for (UMLOperation op : umlClass.getConstructors()){
                if (op.getName().equals(operationName)){
                    boolean foundDifference = false;
                    //name match, now need to check param types.
                    for (int i = 0; i < params.size(); i++){
                        //param order needs to be preserved too
                        String param = params.get(i);
                        if (param.contains("[")) {
                            //list, need to remove brackets
                            param = param.replace("[","");
                            param = param.replace("]","");
                        }
                        if (!param.equals(op.getParameters().get(i).getName())){
                            foundDifference = true;
                            break;
                        }
                    }
                    //same method signature!
                    if (foundDifference == false){
                        return op;
                    }
                }
            }
        }
        //if we get here just super greedily search. -- will happen if the return type is a generic, such as a List.
        //this is because the detection tool does not separate generics from actual types correctly, because it uses byte-code analysis
        //and at the bytecode level generics are treated differently.
        for (UMLOperation op : umlClassifier.getOperations()){
            if (op.getName().equals(operationName)){
                return op;
            }
        }

        //should not happen - but will if we are looking at a third party return type rn.
        System.out.println("did not find a match between classifier: " + umlClassifier.getName() + " and method name: " + operationName);
        System.exit(0);
        return null;
    }


    //input will look like this: execute(org.openqa.selenium.remote.http.HttpRequest, boolean)
    protected List<String> getParamsFromNameParams(String minorClassOperationNameParams){
        List<String> paramsList = new ArrayList<>();
        String paramsBlock = minorClassOperationNameParams.split("\\(")[1];
        //minus 2 becuase last char is a ')'
        if (paramsBlock.length() == 1){
            return paramsList;
        }
        String[] params = paramsBlock.substring(0, paramsBlock.length()-1).split("\\,");
        for (String s : params){
            s = s.replace(" ", "");//remove spaces
            if (s.contains(".")){
                //package
                String[] splitter = s.split("\\.");
                s = splitter[splitter.length-1];
            }
            paramsList.add(s);
        }
        return paramsList;
    }

    protected UMLAttribute matchAttribute(UMLClassifier umlClassifier, String attributeName){
        for (UMLAttribute attribute : umlClassifier.getAttributes()){
            if (attribute.getName().equals(attributeName)){
                //dont need to check type because attribute names are unique.
                return attribute;
            }
        }
        //should not happen.
        System.out.println("did not find a match between classifier: " + umlClassifier.getName() + " and attribute: " + attributeName);
        System.exit(0);
        return null;
    }

    protected UMLAttribute getAttributeFromString(UMLClassifier umlClassifier, String attributeName){
        String parsedAttributeName = parseRoleName(attributeName);
        UMLAttribute attribute = matchAttribute(umlClassifier, parsedAttributeName);
        return attribute;
    }


    protected UMLOperation getOperationFromString(UMLClassifier umlClassifier, String operationName){
        //operation name is unparsed; need to parse it.
        String roleName = parseRoleName(operationName);
        String roleType = parseRoleType(operationName);
        List<String> params = getParamsFromNameParams(roleName);
        //remove parens from roleName
        roleName = roleName.split("\\(")[0];

        UMLOperation operation = matchOperation(umlClassifier, roleName, roleType, params);
        return operation;
    }


    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return fView or execute()
    private String parseRoleName(String role){
        String ownerRemoved = role.split("\\:\\:")[1];
        String typeRemoved = ownerRemoved.split("\\:")[0];
        return typeRemoved;
    }


    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return CH.ifa.draw.standard.AlignCommand in both cases.
    private String parseRoleOwner(String role){
        String nameRemoved = role.split("\\:\\:")[0];
        return nameRemoved;
    }

    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return CH.ifa.draw.framework.DrawingView or void
    private String parseRoleType(String role){
        String ownerRemoved = role.split("\\:\\:")[1];
        String nameRemoved = ownerRemoved.split("\\:")[1];
        if (nameRemoved.contains("\\.")){
            String[] typeSplitter = nameRemoved.split("\\.");
            nameRemoved = typeSplitter[typeSplitter.length-1];
        }
        return nameRemoved;
    }

    public abstract List<Pair<String, UMLClassifier>> getClassifierModelBlocks();
    public abstract List<Pair<String, UMLOperation>> getOperationModelBlocks();
    public abstract List<Pair<String, UMLAttribute>> getAttributeModelBlocks();

    public List<Pair<UMLClassifier, UMLClassifier>> getRelationships(Relationship relationship){
        List<Pair<UMLClassifier, UMLClassifier>> relationships = new ArrayList<>();
        for (Pair<String, UMLClassifier> self : getClassifierModelBlocks()) {
            for (UMLClassifier predecessor : umlClassDiagram.getClassDiagram().predecessors(self.getValue())){
                if (relationship.equals(umlClassDiagram.getClassDiagram().edgeValue(predecessor, self.getValue()).get())){
                    boolean hasBeenAdded = false;
                    Pair<UMLClassifier, UMLClassifier> potentialPair = new Pair<>(predecessor, self.getValue());
                    for (Pair<UMLClassifier, UMLClassifier> existingPair : relationships){
                        //check to see if already added
                        if (existingPair.getKey().equals(potentialPair.getKey()) && existingPair.getValue().equals(potentialPair.getValue())){
                            //already here.
                            hasBeenAdded = true;
                        }
                    }
                    if (!hasBeenAdded) {
                        //only add if unique.
                        relationships.add(new Pair<>(predecessor, self.getValue()));
                    }
                }
            }
            for (UMLClassifier successor : umlClassDiagram.getClassDiagram().successors(self.getValue())){
                if (relationship.equals(umlClassDiagram.getClassDiagram().edgeValue(self.getValue(), successor).get())){
                    boolean hasBeenAdded = false;
                    Pair<UMLClassifier, UMLClassifier> potentialPair = new Pair<>(self.getValue(), successor);
                    for (Pair<UMLClassifier, UMLClassifier> existingPair : relationships){
                        //check to see if already added
                        if (existingPair.getKey().equals(potentialPair.getKey()) && existingPair.getValue().equals(potentialPair.getValue())){
                            //already here.
                            hasBeenAdded = true;
                        }
                    }
                    if (!hasBeenAdded) {
                        //only add if unique.
                        relationships.add(new Pair<>(self.getValue(), successor));
                    }
                }
            }
        }
        return relationships;
    }


    public abstract void printSummary();

    /***
     * input is of form: decl{TYPE}, we want TYPE
     * @param tagName
     * @return
     */
    private String getTypeFromCallTreeTagDecl(String tagName){
        return tagName.replace("decl{", "").replace("}","");
    }

}
