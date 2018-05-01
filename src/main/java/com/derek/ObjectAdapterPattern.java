package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.uml.*;
import javafx.util.Pair;

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
        adapteeClassifier = new Pair<>("Adaptee", getOneMajorRole(pi));
        adapterClassifier = new Pair<>("Adapter", getSecondMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> adapteeAttributesString = new ArrayList<>();
        List<Pair<String, String>> requestOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("adaptee")){
                adapteeAttributesString.add(new Pair<>("adaptee", p.getValue()));
            }else if (p.getKey().equals("Request()")){
                requestOperationsString.add(new Pair<>("Request", p.getValue()));
            }
        }
        adapteeAttributes = new ArrayList<>();
        requestOperations = new ArrayList<>();
        for (Pair<String, String> s : adapteeAttributesString){
            adapteeAttributes.add(new Pair<>(s.getKey(), getAttributeFromString(adapterClassifier.getValue(), s.getValue())));
        }
        for (Pair<String, String> s : requestOperationsString){
            requestOperations.add(new Pair<>(s.getKey(), getOperationFromString(adapterClassifier.getValue(), s.getValue())));
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
