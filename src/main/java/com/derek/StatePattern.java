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
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StatePattern extends PatternMapper {

    //provided by pattern4 tool
    private Pair<String, UMLClassifier> contextClassifier;
    private Pair<String, UMLClassifier> stateClassifier;
    private List<Pair<String, UMLAttribute>> stateAttributes;
    private List<Pair<String, UMLOperation>> requestOperations;

    //coalesced members
    private List<Pair<String, UMLOperation>> handleOperations;
    private List<Pair<String, UMLClassifier>> stateFamily;
    private List<Pair<String, UMLClassifier>> contextFamily;
    private List<Pair<String, UMLOperation>> requestOperationFamily;
    private List<Pair<String, UMLAttribute>> stateAttributeFamily;

    public StatePattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        contextClassifier = new ImmutablePair<>("Context", getOneMajorRole(pi));
        stateClassifier = new ImmutablePair<>("State", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> stateAttributesString = new ArrayList<>();
        List<Pair<String, String>> requestOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("state")){
                stateAttributesString.add(new ImmutablePair<>("state", p.getValue()));
            }else if (p.getKey().equals("Request()")){
                requestOperationsString.add(new ImmutablePair<>("Request", p.getValue()));
            }
        }
        stateAttributes = new ArrayList<>();
        requestOperations = new ArrayList<>();
        for (Pair<String, String> s : stateAttributesString){
            stateAttributes.add(new ImmutablePair<>(s.getKey(), getAttributeFromString(contextClassifier.getValue(), s.getValue())));
        }
        for (Pair<String, String> s : requestOperationsString){
            requestOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(contextClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    protected void coalescePattern(){
        //get families of states. no need to get families of contexts because there should only be one context per state pattern.
        coalesceStates();
        coalesceContexts();

        //read this as 'we are looking to coalesce handle family in this.getAllStates, with search keyword "Handle"
        //search keyword is the key value for a row in the state.txt file, in configs/patternNames
        handleOperations = coalesceOperations(this.getAllStates(), "Handle");
        requestOperationFamily = coalesceOperations(this.getAllContexts(), "Request");

        //for the attribute state
        stateAttributeFamily = coalesceAttributes(getAllContexts(), "State");
    }

    private void coalesceStates(){
        List<UMLClassifier> stateFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(stateClassifier.getRight());
        stateFamily = new ArrayList<>();
        for (UMLClassifier stateFamilyClassifier : stateFamilyClassifiers){
            if (stateFamilyClassifier.getIdentifier().equals("class")){
                //concrete subject
                stateFamily.add(new ImmutablePair<>("ConcreteState", stateFamilyClassifier));
            }else {
                stateFamily.add(new ImmutablePair<>("State", stateFamilyClassifier));
            }
        }
    }

    private void coalesceContexts(){
        List<UMLClassifier> contextFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(contextClassifier.getRight());
        contextFamily = new ArrayList<>();
        for (UMLClassifier contextFamilyClassifier : contextFamilyClassifiers){
            if (contextFamilyClassifier.getIdentifier().equals("class")){
                //concrete subject
                contextFamily.add(new ImmutablePair<>("ConcreteContext", contextFamilyClassifier));
            }else {
                contextFamily.add(new ImmutablePair<>("Context", contextFamilyClassifier));
            }
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks(){
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        //add states that are classes
        for (Pair<String, UMLClassifier> statePair : stateFamily){
            if (statePair.getLeft().equals("ConcreteState")){
                modelBlocks.add(statePair);
            }
        }
        for (Pair<String, UMLClassifier> contextPair : contextFamily){
            if (contextPair.getLeft().equals("ConcreteContext")){
                modelBlocks.add(contextPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        modelBlocks.add(stateClassifier);
        modelBlocks.add(contextClassifier);
        for (Pair<String, UMLClassifier> observerPair : stateFamily){
            if (observerPair.getLeft().equals("State")){
                modelBlocks.add(observerPair);
            }
        }
        for (Pair<String, UMLClassifier> subjectPair : contextFamily){
            if (subjectPair.getLeft().equals("Context")){
                modelBlocks.add(subjectPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>>  getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(requestOperations);
        toRet.addAll(handleOperations);
        toRet.addAll(requestOperationFamily);
        toRet.addAll(handleOperations);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        List<Pair<String, UMLAttribute>> toRet = new ArrayList<>();
        toRet.addAll(stateAttributes);
        toRet.addAll(stateAttributeFamily);
        return toRet;
    }

    public List<Pair<String, UMLClassifier>> getAllContexts(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(contextClassifier);
        toRet.addAll(contextFamily);
        return toRet;
    }

    public List<Pair<String, UMLClassifier>> getAllStates(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(stateClassifier);
        toRet.addAll(stateFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(getAllStates());
        toRet.addAll(getAllContexts());
        return toRet;
    }

    public void printSummary(){
        System.out.println("Context role: " + contextClassifier.getValue().getName());
        System.out.println("State role: " + stateClassifier.getValue().getName());
        for (Pair<String, UMLAttribute> at : stateAttributes) {
            System.out.println("state role (attribute): " + at.getValue().getName());
        }
        for (Pair<String, UMLOperation> op : requestOperations){
            System.out.println("Request() role (operation): " + op.getValue().getName());
        }
    }
    @Override
    protected String getPatternCommonNamesFileName() {
        return "state.txt";
    }

}
