package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import com.google.common.graph.EndpointPair;
import javafx.util.Pair;
import lombok.Getter;
import org.omg.CORBA.SystemException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
