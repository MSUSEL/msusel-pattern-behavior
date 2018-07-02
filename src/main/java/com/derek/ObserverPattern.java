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

    private Pair<String, UMLClassifier> observerClassifier;
    private Pair<String, UMLClassifier> subjectClassifier;
    private List<Pair<String, UMLOperation>> notifyOperations;

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
        List<UMLClassifier> observerFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(observerClassifier.getRight());
        List<UMLClassifier> subjectFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(subjectClassifier.getRight());
        observerFamily = new ArrayList<>();
        for (UMLClassifier observerFamilyClassifier : observerFamilyClassifiers){
            if (observerFamilyClassifier.getIdentifier().equals("class")){
                //concrete observer
                observerFamily.add(new ImmutablePair<>("ConcreteObserver", observerFamilyClassifier));
            }else {
                observerFamily.add(new ImmutablePair<>("Observer", observerFamilyClassifier));
            }
        }
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
        return notifyOperations;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return new ArrayList<>();
    }

    @Override
    public void printSummary() {
        System.out.println("Subject role: " + subjectClassifier.getValue().getName());
        System.out.println("Observer role: " + observerClassifier.getValue().getName());
        for (Pair<String, UMLOperation> op : notifyOperations){
            System.out.println("Notify() role (operation): " + op.getValue().getName());
        }
    }
}
