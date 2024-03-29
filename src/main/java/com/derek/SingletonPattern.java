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
public class SingletonPattern extends PatternMapper {

    private Pair<String, UMLClassifier> singletonClassifier;
    private List<Pair<String, UMLAttribute>> singletonAttributes;


    private List<Pair<String, UMLOperation>> getInstanceOperationFamily;
    private List<Pair<String, UMLOperation>> operationFamily;
    private List<Pair<String, UMLOperation>> getDataOperationFamily;
    private List<Pair<String, UMLAttribute>> uniqueInstanceAttributeFamily;

    public SingletonPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        singletonClassifier = new ImmutablePair<>("Singleton", getOneMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        //should only be 1 attribute for a singleton.
        singletonAttributes = new ArrayList<>();
        for (Pair<String, String> minorRole : minorRoles) {
            singletonAttributes.add(new ImmutablePair<>("UniqueInstance", getAttributeFromString(singletonClassifier.getValue(), minorRole.getValue())));
        }
    }

    @Override
    protected void coalescePattern() {
        List<Pair<String, UMLClassifier>> classifierAsList = new ArrayList<>();
        classifierAsList.add(singletonClassifier);
        getInstanceOperationFamily = coalesceOperations(classifierAsList, "GetInstance");
        operationFamily = coalesceOperations(classifierAsList, "Operation");
        getDataOperationFamily = coalesceOperations(classifierAsList, "GetData");

        uniqueInstanceAttributeFamily = coalesceAttributes(classifierAsList,"UniqueInstance");
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks(){
        List<Pair<String, UMLClassifier>> classifierAsList = new ArrayList<>();
        classifierAsList.add(singletonClassifier);
        return classifierAsList;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(singletonClassifier);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> blocks = new ArrayList<>();
        blocks.add(singletonClassifier);
        return blocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(getInstanceOperationFamily);
        toRet.addAll(operationFamily);
        toRet.addAll(getDataOperationFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        List<Pair<String, UMLAttribute>> atts = new ArrayList<>();
        atts.addAll(singletonAttributes);
        atts.addAll(uniqueInstanceAttributeFamily);
        return atts;
    }


    @Override
    public void printSummary() {
        System.out.println("Singleton role: " + singletonClassifier.getValue().getName());
        System.out.println("Singleton attribute role (attribute): " + singletonAttributes.get(0).getValue().getName());
    }

    @Override
    protected String getPatternCommonNamesFileName() {
        return "singleton.txt";
    }
}
