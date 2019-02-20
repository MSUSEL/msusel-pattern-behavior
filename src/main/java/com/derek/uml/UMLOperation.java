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

import com.derek.uml.srcML.SrcMLNode;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class UMLOperation {

    private String name;
    //list of pairs, where each pair corresponds to a datatype and a name.
    private List<Pair<String,String>> stringParameters;

    //list of parameters referencing actual uml classifier objects.
    //at the time of writing this code I am not parsing ALL umlclassifiers, the ones not being parsed are specifically third party libs
    //and generics. Because of this there may be null elements in teh list.
    @Setter
    private List<UMLAttribute> parameters;

    //datatype of return value, empty string if void., "null" as string if constructor. I mikght need to change this in the future though.
    private String stringReturnDataType;
    private Visibility visibility;
    private boolean isStatic;

    @Setter
    private CallTreeNode<String> callTreeString;

    //type will be set after the first passthrough. - type is the return type of this operation
    @Setter
    private UMLClassifier type;

    @Setter
    private List<UMLAttribute> localVariableDecls;

    /***
     * variable table will contain a map of variables and a call tree node. The idea is to map each variable to the callTreeNode that uses it.
     * Each umlattribute in the map will either be from a local variable declaration or a class-level declaration.
     * Each call tree node in the list of call tree node refers to callTree objects that use the variable, the list enforces order.
     */
    private Map<UMLAttribute, List<CallTreeNode<String>>> variableTable;

    //classifier that owns this method.
    @Setter
    private UMLClassifier owningClassifier;


    public UMLOperation(String name, List<Pair<String, String>> stringParameters, String stringReturnDataType, Visibility visibility) {
        this.name = name;
        this.stringParameters = stringParameters;
        this.stringReturnDataType = stringReturnDataType;
        this.visibility = visibility;
        localVariableDecls = new ArrayList<>();
        variableTable = new HashMap<>();
    }

    /***
     * Constructor without method visibility
     * @param name function name
     * @param stringParameters list of pairs of strings for function's params
     * @param stringReturnDataType function return data type
     */
    public UMLOperation(String name, List<Pair<String, String>> stringParameters, String stringReturnDataType) {
        this(name, stringParameters, stringReturnDataType, Visibility.UNSPECIFIED);
    }

    /***
     * method to fill the variable table. This method should be called after relationships are built.. I think.
     * @param umlClassDiagram
     */
    public void fillVariableTable(UMLClassDiagram umlClassDiagram){

        //first thing is to fill variable table from owning class attributes.
        for (UMLAttribute umlAttribute : owningClassifier.getAttributes()){
            //variable has been declared at the class level, but might not be used/ever used in this method.
            addVariableToTable(umlAttribute, umlAttribute.getInstantiation());
        }
        //second thing to do is add parameters from this method
        for (UMLAttribute param : this.getParameters()){
            addVariableToTable(param, null);
        }
        if (this.getCallTreeString() == null){
            //no call tree, this is likely a method signature (from an interface) or an abstract method. Therefore, no need to continue buidling variable table
            return;
        }
        //using a for loop here, not for each because I use i to match variable to spot in variable table.
        for (int i = 0; i < this.getCallTreeString().convertMeToOrderedList().size(); i++) {
            CallTreeNode<String> callTreeNode = this.getCallTreeString().convertMeToOrderedList().get(i);
            if (callTreeNode.isDecl()) {
                //found a local declaration
                String typeName = callTreeNode.parseDeclTagName();

                UMLClassifier thisDecl = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, this.owningClassifier, typeName);
                UMLAttribute localAttribute = new UMLAttribute(callTreeNode.getName(), thisDecl);
                localAttribute.setOwningClassifier(this.owningClassifier);
                localVariableDecls.add(localAttribute);
                addVariableToTable(localAttribute, callTreeNode);
            } else if (callTreeNode.isCall()) {
                //is call
                String varName = callTreeNode.parseVarNameFromCall();
                UMLAttribute referencedVariable = findVariableUsageInTable(varName);
                if (referencedVariable != null) {
                    addVariableToTable(referencedVariable, callTreeNode);
                }
            } else if (callTreeNode.isInit()) {
                UMLClassifier potentialClassifier = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, this.owningClassifier, callTreeNode.getName());
                if (umlClassDiagram.getClassDiagram().nodes().contains(potentialClassifier)) {
                    //I can confidently say the call was a classifier - so its the declaration case. (Client)
                    //in this case I don't need to do anything else. i think.
                    ///after some debate I think its worth adding this.. its still a call but hopefully I can do logic in the future to distinguish
                    //calls from operators (for class grime findings)
                    String varName = callTreeNode.parseInitTagName();
                    UMLAttribute referencedVariable = findVariableUsageInTable(varName);
                    if (referencedVariable != null) {
                        addVariableToTable(referencedVariable, callTreeNode);
                    }
                }
            }
        }
    }

    private UMLAttribute findVariableUsageInTable(String varName){
        for (UMLAttribute key : variableTable.keySet()){
            if (key.getName().equals(varName)){
                //found a variable that matches names, but the locaiton might matter now...
                return key;
            }
        }
        return null;
    }

    private void addVariableToTable(UMLAttribute variable, CallTreeNode usage) {
        if (variableTable.get(variable) == null) {
            variableTable.put(variable, new ArrayList<>());
        }
        //based on java ojbect reference, I think this works.
        variableTable.get(variable).add(usage);
    }

    /***
     * wrapper method to return all usages of a given umlAttribute (which can be from a class or can be locally declared).
     * @param attribute
     * @return
     */
    public List<CallTreeNode<String>> getVariableUsages(UMLAttribute attribute){
        if (variableTable.get(attribute) == null){
            return new ArrayList<>();
        }else {
            return variableTable.get(attribute);
        }
    }

    public boolean isVariableDeclarationLocal(UMLAttribute umlAttribute){
        if (localVariableDecls.contains(umlAttribute)){
            return true;
        }
        return false;
    }

    public String buildParamsForPlantUMLDiagram(){
        StringBuilder s = new StringBuilder();
        //we have params
        for (int i = 0; i < stringParameters.size(); i++){
            Pair<String, String> param = stringParameters.get(i);
            if (i == stringParameters.size() - 1){
                if (param.getKey() == ""){
                    //no params - this will happen during the first iteration of the loop and only if there are no params.
                    return "";
                }else {
                    //last param in param list
                    s.append(param.getKey() + " " + param.getValue());
                }
            }else{
                //defualt case in a sense; multiple params and we are not on the last one
                s.append(param.getKey() + " " + param.getValue() + ", ");
            }
        }
        return s.toString();
    }

    public List<UMLAttribute> getParameters(){
        if (this.parameters == null){
            this.parameters = new ArrayList<>();
        }
        return this.parameters;
    }

    public void printVariableTable(){
        for (UMLAttribute umlAttribute : variableTable.keySet()){
            System.out.println(umlAttribute);
            for (CallTreeNode<String> usage : variableTable.get(umlAttribute)){
                System.out.print("\t");
                if (usage != null) {
                    usage.printTree();
                }else{
                    System.out.println("  () ");
                }
            }
            System.out.println();
        }
    }

}
