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
public class TemplateMethodPattern extends PatternMapper {

    private Pair<String, UMLClassifier> templateClassifier;
    private List<Pair<String, UMLOperation>> templateOperations;

    private List<Pair<String, UMLClassifier>> templateFamily;
    private List<Pair<String, UMLClassifier>> templateConcreteFamily;
    private List<Pair<String, UMLOperation>> templateOperationFamily;
    private List<Pair<String, UMLOperation>> partialStepOperationFamily;

    public TemplateMethodPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        templateClassifier = new ImmutablePair<>("AbstractTemplate", getOneMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> templateOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("TemplateMethod()")){
                templateOperationsString.add(new ImmutablePair<>("TemplateMethod", p.getValue()));
            }
        }
        templateOperations= new ArrayList<>();
        for (Pair<String, String> s : templateOperationsString){
            templateOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(templateClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    protected void coalescePattern() {
        //coalesces both abstract and concrete templates
        coalesceTemplates();

        templateOperationFamily = coalesceOperations(getAllAbstractTemplates(), "TemplateMethod");
        partialStepOperationFamily = coalesceOperations(getAllConcreteTemplates(), "PartialStep");
    }

    private void coalesceTemplates(){
        List<UMLClassifier> templateFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(templateClassifier.getRight());
        templateConcreteFamily = new ArrayList<>();
        templateFamily = new ArrayList<>();
        for (UMLClassifier templateFamilyClassifier : templateFamilyClassifiers){
            if (templateFamilyClassifier.getIdentifier().equals("class")){
                //concrete observer
                templateConcreteFamily.add(new ImmutablePair<>("ConcreteTemplate", templateFamilyClassifier));
            }else {
                templateFamily.add(new ImmutablePair<>("AbstractTemplate", templateFamilyClassifier));
            }
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(templateConcreteFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(getAllAbstractTemplates());
        toRet.addAll(getAllConcreteTemplates());
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> blocks = new ArrayList<>();
        blocks.add(templateClassifier);
        //template family is abstract classes.
        blocks.addAll(templateFamily);
        return blocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(templateOperations);
        toRet.addAll(templateOperationFamily);
        toRet.addAll(partialStepOperationFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        //no attributes in template method
        return new ArrayList<>();
    }

    public List<Pair<String, UMLClassifier>> getAllAbstractTemplates(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(templateClassifier);
        toRet.addAll(templateFamily);
        return toRet;
    }

    public List<Pair<String, UMLClassifier>> getAllConcreteTemplates(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(templateConcreteFamily);
        return toRet;
    }

    @Override
    public void printSummary() {
        System.out.println("Template role: " + templateClassifier.getValue().getName());
        for (Pair<String, UMLOperation> op : templateOperations){
            System.out.println("Template() role (operation): " + op.getValue().getName());
        }
    }


    @Override
    protected String getPatternCommonNamesFileName() {
        return "templateMethod.txt";
    }
}
