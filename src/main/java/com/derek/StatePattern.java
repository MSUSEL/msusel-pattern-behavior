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
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StatePattern extends PatternMapper {

    private Pair<String, UMLClassifier> contextClassifier;
    private Pair<String, UMLClassifier> stateClassifier;
    private List<Pair<String, UMLAttribute>> stateAttributes;
    private List<Pair<String, UMLOperation>> requestOperations;

    public StatePattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        contextClassifier = new Pair<>("Context", getOneMajorRole(pi));
        stateClassifier = new Pair<>("State", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> stateAttributesString = new ArrayList<>();
        List<Pair<String, String>> requestOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("state")){
                stateAttributesString.add(new Pair<>("state", p.getValue()));
            }else if (p.getKey().equals("Request()")){
                requestOperationsString.add(new Pair<>("Request", p.getValue()));
            }
        }
        stateAttributes = new ArrayList<>();
        requestOperations = new ArrayList<>();
        for (Pair<String, String> s : stateAttributesString){
            stateAttributes.add(new Pair<>(s.getKey(), getAttributeFromString(contextClassifier.getValue(), s.getValue())));
        }
        for (Pair<String, String> s : requestOperationsString){
            requestOperations.add(new Pair<>(s.getKey(), getOperationFromString(contextClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks= new ArrayList<>();
        modelBlocks.add(contextClassifier);
        modelBlocks.add(stateClassifier);
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>>  getOperationModelBlocks() {
        return requestOperations;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return stateAttributes;
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

}
