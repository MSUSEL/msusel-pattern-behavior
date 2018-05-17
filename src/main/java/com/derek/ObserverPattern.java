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
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObserverPattern extends PatternMapper {

    private Pair<String, UMLClassifier> observerClassifier;
    private Pair<String, UMLClassifier> subjectClassifier;
    private List<Pair<String, UMLOperation>> notifyOperations;

    public ObserverPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        observerClassifier = new Pair<>("Observer", getOneMajorRole(pi));
        subjectClassifier = new Pair<>("Subject", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> notifyOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("Notify()")){
                notifyOperationsString.add(new Pair<>("Notify", p.getValue()));
            }
        }
        notifyOperations= new ArrayList<>();
        for (Pair<String, String> s : notifyOperationsString){
            notifyOperations.add(new Pair<>(s.getKey(), getOperationFromString(subjectClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks= new ArrayList<>();
        modelBlocks.add(observerClassifier);
        modelBlocks.add(subjectClassifier);
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
