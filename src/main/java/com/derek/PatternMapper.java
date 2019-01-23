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
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Getter
public abstract class PatternMapper {

    protected PatternInstance pi;
    protected UMLClassDiagram umlClassDiagram;
    protected CoalescerUtility coalescerUtility;

    //afferent participants refer to other classifiers that use the pattern. Some could call these classes 'clients'
    protected List<UMLClassifier> afferentParticipants;
    //efferent participants refer to other classes this pattern uses.
    protected List<UMLClassifier> efferentParticipants;

    //for both afferent and efferent participants, I will find them after I coalesce the pattern.

    protected Map<String, List<String>> patternCommonNames;

    public PatternMapper(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        this.pi = pi;
        this.umlClassDiagram = umlClassDiagram;
        coalescerUtility = new CoalescerUtility();
        this.parsePatternCommonNames();
        this.mapToUML();
        this.coalescePattern();
        this.fillParticipatingClassifiers();
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
        System.out.println("was not able to match project class: " + umlClassifier.getName() + " and method name: " + operationName);
        System.out.println("This should not happen. The tool will likely crash because of this.");
        System.out.println("Version: " + this.getPi().getSoftwareVersion().getVersionNum());
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
        System.out.println("was not able to match project class: " + umlClassifier.getName() + " and attribute name: " + attributeName);
        System.out.println("This should not happen. The tool will likely crash because of this.");
        System.out.println("Version: " + this.getPi().getSoftwareVersion().getVersionNum());
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

    /***
     * gets only classifier conforming classes (usualyl abstract, not concrete)
     * @return
     */
    public abstract List<Pair<String, UMLClassifier>> getClassifierModelBlocks();
    public abstract List<Pair<String, UMLClassifier>> getClassModelBlocks();
    public abstract List<Pair<String, UMLOperation>> getOperationModelBlocks();
    public abstract List<Pair<String, UMLAttribute>> getAttributeModelBlocks();
    public abstract List<Pair<String, UMLClassifier>> getAllParticipatingClasses();

    public List<Pair<String, UMLClassifier>> getAllClassifierModelBlocks(){
        List<Pair<String, UMLClassifier>> blocks = new ArrayList<>();
        blocks.addAll(getClassifierModelBlocks());
        blocks.addAll(getClassModelBlocks());
        return blocks;
    }

    protected void parsePatternCommonNames(){
        patternCommonNames = new HashMap<>();
        try{
            File f = new File(Main.patternCommonNames + this.getPatternCommonNamesFileName());
            Scanner in = new Scanner(f);
            while(in.hasNext()){
                String line = in.nextLine();
                String key = line.split(":")[0];
                String[] values = (line.split(":")[1]).split(",");
                patternCommonNames.put(key, Arrays.asList(values));
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to parse common names file. Exiting");
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected abstract String getPatternCommonNamesFileName();

    /***
     * this method returns a list of unique relationship pais (of the type specified in the parameter)
     * for all classifiers associated around (pointing to and away from, via uml class diagram) the pattern classifiers.
     *
     * @param relationshipType
     * @return
     */
    public List<Relationship> getUniqueRelationshipsFromPatternClassifiers(RelationshipType relationshipType){
        List<Relationship> relationships = new ArrayList<>();
        for (Pair<String, UMLClassifier> self : getClassifierModelBlocks()) {
            //for all classifiers in teh pattern
            for (UMLClassifier predecessor : umlClassDiagram.getClassDiagram().predecessors(self.getValue())){
                //for all predecessors of all classifiers in the pattern
                for (Relationship r : umlClassDiagram.getClassDiagram().edgesConnecting(predecessor, self.getValue())){
                    //for all relationships between the two (pred -> self)
                    if (r.getRelationshipType() == relationshipType) {
                        //same relationship type
                        boolean hasBeenAdded = false;
                        for (Relationship existingRelationship : relationships) {
                            //check to see if already added
                            if (existingRelationship.equals(r)) {
                                //already here.
                                hasBeenAdded = true;
                            }
                        }
                        if (!hasBeenAdded) {
                            //only add if unique.
                            relationships.add(r);
                        }
                    }
                }
            }
            for (UMLClassifier successor : umlClassDiagram.getClassDiagram().successors(self.getValue())){
                //for all predecessors of all classifiers in the pattern
                for (Relationship r : umlClassDiagram.getClassDiagram().edgesConnecting(self.getValue(), successor)){
                    //for all relationships between the two (pred -> self)
                    if (r.getRelationshipType() == relationshipType) {
                        //same relationship type
                        boolean hasBeenAdded = false;
                        for (Relationship existingRelationship : relationships) {
                            //check to see if already added
                            if (existingRelationship.equals(r)) {
                                //already here.
                                hasBeenAdded = true;
                            }
                        }
                        if (!hasBeenAdded) {
                            //only add if unique.
                            relationships.add(r);
                        }
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

    /***
     * basic and 'hit or miss' method of coalescing pattern operations. Performs a basic string search . Operations only!
     * This method runs through an owning classifier and checks to see if the search string is a method name that exists.
     * Does not check params or return vals.
     *
     * @param searchString search string
     * @param owningClassifier owning classifier
     * @param dataStruct pass by ref return value
     */
    protected void coalescenceStringSearch(String searchString, UMLClassifier owningClassifier, List<Pair<String, UMLOperation>> dataStruct){
        for (UMLOperation umlOperation : owningClassifier.getOperations()){
            if (StringUtils.containsIgnoreCase(umlOperation.getName(), searchString)){
                //match!
                dataStruct.add(new ImmutablePair<>(searchString, umlOperation));
            }
        }
    }

    /***
     * this method iterates through the candidate classes and returns the UMLOperation version of the operationCallName.
     * @param operationCallName
     * @param candidateClasses
     * @return
     */
    protected UMLOperation connectOperation(String operationCallName, List<Pair<String, UMLClassifier>> candidateClasses){
        for (Pair<String, UMLClassifier> candidateClass : candidateClasses){
            for (UMLOperation umlOperation : candidateClass.getRight().getOperations()){
                if (umlOperation.getName().equals(operationCallName)){
                    return umlOperation;
                }
            }
        }

        return null;
    }

    protected abstract void coalescePattern();


    /***
     * method to coalesce a pattern's operations. Provide this method a list of the classifiers belonging to the pattern, and the name of the operation you want coalesced.
     * @param owningClassifier
     * @param operationName
     * @return
     */
    protected List<Pair<String, UMLOperation>> coalesceOperations(List<Pair<String, UMLClassifier>> owningClassifier, String operationName){
        List<Pair<String, UMLOperation>> operationsDataStruct = new ArrayList<>();
        for (Pair<String, UMLClassifier> classifierPair : owningClassifier){
            for (String commonNameValue : patternCommonNames.get(operationName)){
                List<Pair<String, UMLOperation>> tempOperations = new ArrayList<>();
                coalescenceStringSearch(commonNameValue, classifierPair.getRight(), tempOperations);
                for (Pair<String, UMLOperation> op : tempOperations){
                    operationsDataStruct.add(new ImmutablePair<>(operationName, op.getRight()));
                }
            }
        }
        return operationsDataStruct;
    }

    //gets only the classifier objects of the CONCRETE, ABSTRACT classes, and INTERFACES in this pattern
    public List<UMLClassifier> getAllParticipatingClassifiersOnlyUMLClassifiers(){
        List<UMLClassifier> classifiers = new ArrayList<>();
        for (Pair<String, UMLClassifier> classifierPair : this.getAllClassifierModelBlocks()){
            classifiers.add(classifierPair.getRight());
        }
        return classifiers;
    }

    //gets only the classifier objects of all the CONCRETE, ABSTRACT classes in this pattern
    public List<UMLClassifier> getAllParticipatingClassesOnlyUMLClassifiers(){
        List<UMLClassifier> classifiers = new ArrayList<>();
        for (Pair<String, UMLClassifier> classifierPair : this.getAllClassifierModelBlocks()){
            classifiers.add(classifierPair.getRight());
        }
        return classifiers;
    }

    /***
     * method to fill afferent and efferent participating classifiers (the variables)
     */
    public void fillParticipatingClassifiers(){
        this.afferentParticipants = new ArrayList<>();
        this.efferentParticipants = new ArrayList<>();
        for (Relationship r : umlClassDiagram.getClassDiagram().edges()){
            if (this.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(r.getFrom())){
                //our pattern contains a potential efferent link.
                if (!this.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(r.getTo())){
                    //make sure the 'to' link is not in the pattern
                    efferentParticipants.add(r.getTo());
                }
            }
            if (this.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(r.getTo())){
                //check afferent links
                if (!this.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(r.getFrom())){
                    //make sure from link not in pattern
                    afferentParticipants.add(r.getFrom());
                }
            }
        }

    }

}
