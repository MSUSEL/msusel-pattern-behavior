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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ObjectAdapterPattern extends PatternMapper {

    private Pair<String, UMLClassifier> adapteeClassifier;
    private Pair<String, UMLClassifier> adapterClassifier;
    private List<Pair<String, UMLAttribute>> adapteeAttributes;
    private List<Pair<String, UMLOperation>> requestOperations;

    public ObjectAdapterPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        adapteeClassifier = new ImmutablePair<>("Adaptee", getOneMajorRole(pi));
        adapterClassifier = new ImmutablePair<>("Adapter", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> adapteeAttributesString = new ArrayList<>();
        List<Pair<String, String>> requestOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("adaptee")){
                adapteeAttributesString.add(new ImmutablePair<>("adaptee", p.getValue()));
            }else if (p.getKey().equals("Request()")){
                requestOperationsString.add(new ImmutablePair<>("Request", p.getValue()));
            }
        }
        adapteeAttributes = new ArrayList<>();
        requestOperations = new ArrayList<>();
        for (Pair<String, String> s : adapteeAttributesString){
            adapteeAttributes.add(new ImmutablePair<>(s.getKey(), getAttributeFromString(adapterClassifier.getValue(), s.getValue())));
        }
        for (Pair<String, String> s : requestOperationsString){
            requestOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(adapterClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks= new ArrayList<>();
        modelBlocks.add(adapteeClassifier);
        modelBlocks.add(adapterClassifier);
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        return requestOperations;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return adapteeAttributes;
    }

    @Override
    public void printSummary() {
        System.out.println("Adaptee role: " + adapteeClassifier.getValue().getName());
        System.out.println("Adapter role: " + adapterClassifier.getValue().getName());
        for (Pair<String, UMLAttribute> at : adapteeAttributes) {
            System.out.println("state role (attribute): " + at.getValue().getName());
        }
        for (Pair<String, UMLOperation> op : requestOperations){
            System.out.println("Request() role (operation): " + op.getValue().getName());
        }
    }

    public List<UMLClassifier> getUMLClassifiers(){
        List<UMLClassifier> umlClassifiers = new ArrayList<>();
        umlClassifiers.add(adapterClassifier.getValue());
        umlClassifiers.add(adapteeClassifier.getValue());

        return umlClassifiers;
    }
}
