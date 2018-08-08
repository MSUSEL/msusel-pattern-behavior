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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObserverPattern extends PatternMapper {

    //the 3 below are extracted from the pattern4 tool
    private Pair<String, UMLClassifier> observerClassifier;
    private Pair<String, UMLClassifier> subjectClassifier;
    private List<Pair<String, UMLOperation>> notifyOperations;

    //these data structs are coalesced.
    private List<Pair<String, UMLOperation>> updateOperations;
    private List<Pair<String, UMLOperation>> getStateOperations;
    private List<Pair<String, UMLOperation>> setStateOperations;
    private List<Pair<String, UMLClassifier>> observerFamily;
    private List<Pair<String, UMLClassifier>> subjectFamily;

    public ObserverPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        observerClassifier = new ImmutablePair<>("Observer", getOneMajorRole(pi));
        subjectClassifier = new ImmutablePair<>("Subject", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> notifyOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("Notify()")){
                notifyOperationsString.add(new ImmutablePair<>("Notify", p.getValue()));
            }
        }
        notifyOperations= new ArrayList<>();
        for (Pair<String, String> s : notifyOperationsString){
            notifyOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(subjectClassifier.getValue(), s.getValue())));
        }
        coalescePattern();
    }

    private void coalescePattern(){
        //get families of subjects and observers
        coalesceObservers();
        coalesceSubjects();
        //I need to coalesce the methods AFTER I do the classes. This is because the process of coalescing methods involves
        //iterating through all the classes.
        coalesceUpdateOperation();
        coalesceGetStateOperation();
        coalesceSetStateOperation();
    }

    private void coalesceObservers(){
        List<UMLClassifier> observerFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(observerClassifier.getRight());
        observerFamily = new ArrayList<>();
        for (UMLClassifier observerFamilyClassifier : observerFamilyClassifiers){
            if (observerFamilyClassifier.getIdentifier().equals("class")){
                //concrete observer
                observerFamily.add(new ImmutablePair<>("ConcreteObserver", observerFamilyClassifier));
            }else {
                observerFamily.add(new ImmutablePair<>("Observer", observerFamilyClassifier));
            }
        }
    }

    /***
     * attempts to find and assign the update operation for all observers, if it exists.
     */
    private void coalesceUpdateOperation(){
        updateOperations = new ArrayList<>();
        //first use a string search to look for 'update' in observers. addAll to first one, will add others as they appear.
        updateOperations.addAll(stringSearchCoalesce());

        //consider refactoring this below to a separate method.

        //second pass for update coalescence will be looking for (1) declarations of variables within a subject
        //that make calls to any observers. There is a chance this could be grime, and also a chance it is the correct update operation.
        //though I am doing grime checks later.. so I don't think it matters at this point.
        for (Pair<String, UMLClassifier> subject : this.getAllSubjects()){
            for (Pair<String, UMLClassifier> observer : this.getAllObservers()) {
                List<UMLAttribute> classAtts = coalescerUtility.getInterPatternAttributes(subject.getRight(), observer.getRight());
                //classAtts at this point are all class level associations from subject -> observer
                for (UMLOperation operation : subject.getRight().getOperations()){
                    List<String> tempVars = coalescerUtility.getInterMethodPatternAttriubtes(operation.getCallTreeString().convertMeToOrderedList(), observer.getRight());
                    //at this point I have all class lvl variables (classAtts), and all temporary or method lvl vars (tempVars).
                    //I need to iterate through the call tree of operation and see if the vars are ever used (ie., call tag in call tree)
                    //if it is ever used, I need to assign presence. (or update op)
                    for (CallTreeNode<String> node : operation.getCallTreeString().convertMeToOrderedList()){
                        //need to look through temp vars first
                        for (String s : tempVars){
                            if (node.isCall()){
                                if (s.equals(node.parseVarNameFromCall())){
                                    //found a match. this call tree node is an update op, or at least is a relationship from subj -> obs
                                    //need to find correct operation from observers.
                                    UMLOperation correctOp = connectOperation(node.parseCallNameFromCall(), observerFamily);
                                    if (correctOp != null){
                                        //might be null if we find a relationship between roles that isn't the one we are looking for.
                                        updateOperations.add(new ImmutablePair<>("Update", correctOp));
                                    }
                                }
                            }
                        }
                        for (UMLAttribute umlAttribute : classAtts){
                            if (node.isCall()){
                                if (umlAttribute.getName().equals(node.parseVarNameFromCall())){
                                    UMLOperation correctOp = connectOperation(node.parseCallNameFromCall(), observerFamily);
                                    if (correctOp != null){
                                        //might be null if we find a relationship between roles that isn't the one we are looking for.
                                        updateOperations.add(new ImmutablePair<>("Update", correctOp));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * tries to find update operations based on string search. plain and simple. Actually, the string search is based on 'equals', not 'contains'.
     * @return
     */
    private List<Pair<String, UMLOperation>> stringSearchCoalesce(){
        List<Pair<String, UMLOperation>> stringSearchUpdates = new ArrayList<>();
        for (Pair<String, UMLClassifier> observerPair : this.getAllObservers()) {
            coalescenceStringSearch("Update", observerPair.getRight(), stringSearchUpdates);
        }
        return stringSearchUpdates;
    }

    private void coalesceSubjects(){
        List<UMLClassifier> subjectFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(subjectClassifier.getRight());
        subjectFamily = new ArrayList<>();
        for (UMLClassifier subjectFamilyClassifier : subjectFamilyClassifiers){
            if (subjectFamilyClassifier.getIdentifier().equals("class")){
                //concrete subject
                subjectFamily.add(new ImmutablePair<>("ConcreteSubject", subjectFamilyClassifier));
            }else {
                subjectFamily.add(new ImmutablePair<>("Subject", subjectFamilyClassifier));
            }
        }

    }

    private void coalesceGetStateOperation(){
        getStateOperations = new ArrayList<>();
        //first use a string search to look for 'update' in observers.
        for (Pair<String, UMLClassifier> subjectPair : this.getAllSubjects()) {
            coalescenceStringSearch("GetState", subjectPair.getRight(), getStateOperations);
        }
    }

    private void coalesceSetStateOperation(){
        setStateOperations = new ArrayList<>();
        //first use a string search to look for 'update' in observers.
        for (Pair<String, UMLClassifier> subjectPair : this.getAllSubjects()) {
            coalescenceStringSearch("SetState", subjectPair.getRight(), setStateOperations);
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks(){
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        for (Pair<String, UMLClassifier> observerPair : observerFamily){
            if (observerPair.getLeft().equals("ConcreteObserver")){
                modelBlocks.add(observerPair);
            }
        }
        for (Pair<String, UMLClassifier> subjectPair : subjectFamily){
            if (subjectPair.getLeft().equals("ConcreteSubject")){
                modelBlocks.add(subjectPair);
            }
        }
        return modelBlocks;
    }

    /***
     * get classifier model blocks only returns the pattern classes that match to a classifier role. (not a class role).
     * I might need to chagne this in the future because class roles are technically children of classifier roles..
     * Meaning my implementation makes sense from an RBML perspective of how I have implemented spses and ipses,
     * but not from a uml meta-model perspective
     *
     * @return
     */
    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        modelBlocks.add(observerClassifier);
        modelBlocks.add(subjectClassifier);
        for (Pair<String, UMLClassifier> observerPair : observerFamily){
            if (observerPair.getLeft().equals("Observer")){
                modelBlocks.add(observerPair);
            }
        }
        for (Pair<String, UMLClassifier> subjectPair : subjectFamily){
            if (subjectPair.getLeft().equals("Subject")){
                modelBlocks.add(subjectPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(notifyOperations);
        toRet.addAll(updateOperations);
        toRet.addAll(getStateOperations);
        toRet.addAll(setStateOperations);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return new ArrayList<>();
    }

    public List<Pair<String, UMLClassifier>> getAllObservers(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(observerClassifier);
        toRet.addAll(observerFamily);
        return toRet;
    }

    public List<Pair<String, UMLClassifier>> getAllSubjects(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(subjectClassifier);
        toRet.addAll(subjectFamily);
        return toRet;
    }

    @Override
    public void printSummary() {
        System.out.println("Subject role: " + subjectClassifier.getValue().getName());
        System.out.println("Observer role: " + observerClassifier.getValue().getName());
        for (Pair<String, UMLOperation> op : notifyOperations){
            System.out.println("Notify() role (operation): " + op.getValue().getName());
        }
    }

    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(getAllObservers());
        toRet.addAll(getAllSubjects());
        return toRet;
    }
}
