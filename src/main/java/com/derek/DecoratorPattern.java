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

public class DecoratorPattern extends PatternMapper {

    private Pair<String, UMLClassifier> componentClassifier;
    private Pair<String, UMLClassifier> decoratorClassifier;
    private List<Pair<String, UMLAttribute>> componentAttributes;
    private List<Pair<String, UMLOperation>> decoratorOperations;

    //coalesced
    private List<Pair<String, UMLClassifier>> componentFamily;
    private List<Pair<String, UMLClassifier>> decoratorFamily;

    private List<Pair<String, UMLOperation>> decoratorOperationFamily;
    private List<Pair<String, UMLOperation>> componentOperationFamily;
    private List<Pair<String, UMLAttribute>> componentAttributeFamily;

    public DecoratorPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        componentClassifier = new ImmutablePair<>("Component", getOneMajorRole(pi));
        decoratorClassifier = new ImmutablePair<>("Decorator", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> componentsAttributesString = new ArrayList<>();
        List<Pair<String, String>> opOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("component")){
                componentsAttributesString.add(new ImmutablePair<>("component", p.getValue()));
            }else if (p.getKey().equals("Operation()")){
                opOperationsString.add(new ImmutablePair<>("Operation", p.getValue()));
            }
        }
        componentAttributes = new ArrayList<>();
        decoratorOperations = new ArrayList<>();
        for (Pair<String, String> s : componentsAttributesString){
            componentAttributes.add(new ImmutablePair<>(s.getKey(), getAttributeFromString(decoratorClassifier.getValue(), s.getValue())));
        }
        for (Pair<String, String> s : opOperationsString){
            decoratorOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(decoratorClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    protected void coalescePattern() {
        coalesceComponents();
        coalesceDecorators();

        decoratorOperationFamily = coalesceOperations(this.getAllDecorators(), "Operation");
        componentOperationFamily = coalesceOperations(this.getAllComponents(), "Operation");

        //for the attribute state
        componentAttributeFamily = coalesceAttributes(getAllDecorators(), "Component");
    }

    private void coalesceComponents(){
        List<UMLClassifier> componentFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(componentClassifier.getRight());
        componentFamily = new ArrayList<>();
        for (UMLClassifier componentFamilyClassifier : componentFamilyClassifiers){
            if (componentFamilyClassifier.getIdentifier().equals("class")){
                //concrete subject
                componentFamily.add(new ImmutablePair<>("ConcreteComponent", componentFamilyClassifier));
            }else {
                componentFamily.add(new ImmutablePair<>("Component", componentFamilyClassifier));
            }
        }
    }

    private void coalesceDecorators(){
        List<UMLClassifier> decoratorFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(decoratorClassifier.getRight());
        decoratorFamily = new ArrayList<>();
        for (UMLClassifier decoratorFamilyClassifier : decoratorFamilyClassifiers){
            if (decoratorFamilyClassifier.getIdentifier().equals("class")){
                //concrete subject
                decoratorFamily.add(new ImmutablePair<>("ConcreteDecorator", decoratorFamilyClassifier));
            }else {
                decoratorFamily.add(new ImmutablePair<>("Decorator", decoratorFamilyClassifier));
            }
        }
    }

    public List<Pair<String, UMLClassifier>> getAllDecorators(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(decoratorClassifier);
        toRet.addAll(decoratorFamily);
        return toRet;
    }

    public List<Pair<String, UMLClassifier>> getAllComponents(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(componentClassifier);
        toRet.addAll(componentFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        modelBlocks.add(componentClassifier);
        modelBlocks.add(decoratorClassifier);
        for (Pair<String, UMLClassifier> componentPair : componentFamily){
            if (componentPair.getLeft().equals("Component")){
                modelBlocks.add(componentPair);
            }
        }
        for (Pair<String, UMLClassifier> decoratorPair : decoratorFamily){
            if (decoratorPair.getLeft().equals("Decorator")){
                modelBlocks.add(decoratorPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        //add states that are classes
        for (Pair<String, UMLClassifier> componentPair : componentFamily){
            if (componentPair.getLeft().equals("ConcreteComponent")){
                modelBlocks.add(componentPair);
            }
        }
        for (Pair<String, UMLClassifier> decoratorPair : decoratorFamily){
            if (decoratorPair.getLeft().equals("ConcreteDecorator")){
                modelBlocks.add(decoratorPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(decoratorOperations);
        toRet.addAll(decoratorOperationFamily);
        toRet.addAll(componentOperationFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        List<Pair<String, UMLAttribute>> toRet = new ArrayList<>();
        toRet.addAll(componentAttributes);
        toRet.addAll(componentAttributeFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses() {
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(getAllComponents());
        toRet.addAll(getAllDecorators());
        return toRet;
    }

    @Override
    protected String getPatternCommonNamesFileName() {
        return "decorator.txt";
    }

    @Override
    public void printSummary() {

    }


}
