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
    private List<Pair<String, UMLOperation>> attachOperations;
    private List<Pair<String, UMLOperation>> detachOperations;
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
        attachOperations = coalesceOperations(this.getAllSubjects(), "Attach");
        detachOperations = coalesceOperations(this.getAllSubjects(), "Detach");
        updateOperations = coalesceOperations(this.getAllObservers(), "Update");
        getStateOperations = coalesceOperations(this.getAllSubjects(), "GetState");
        setStateOperations = coalesceOperations(this.getAllSubjects(), "SetState");
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

    private List<Pair<String, UMLOperation>> coalesceOperations(List<Pair<String, UMLClassifier>> owningClassifier, String operationName){
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
        toRet.addAll(attachOperations);
        toRet.addAll(detachOperations);
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

    @Override
    protected String getPatternCommonNamesFileName() {
        return "observer.txt";
    }
}
