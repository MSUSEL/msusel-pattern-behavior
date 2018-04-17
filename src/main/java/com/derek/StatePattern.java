package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.rbml.StructuralRole;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StatePattern extends PatternMapper {

    private UMLClassifier contextClassifier;
    private UMLClassifier stateClassifier;
    private List<UMLAttribute> stateAttributes;
    private List<UMLOperation> requestOperation;

    public StatePattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        contextClassifier = getOneMajorRole(pi);
        stateClassifier = getSecondMajorRole(pi);
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<String> stateAttributesString = new ArrayList<>();
        List<String> requestOperations = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("state")){
                stateAttributesString.add(p.getValue());
            }else if (p.getKey().equals("Request()")){
                requestOperations.add(p.getValue());
            }
        }
        stateAttributes = new ArrayList<>();
        requestOperation = new ArrayList<>();
        for (String s : stateAttributesString){
            stateAttributes.add(getAttributeFromString(contextClassifier, s));
        }
        for (String s : requestOperations){
            requestOperation.add(getOperationFromString(contextClassifier, s));
        }
    }

    @Override
    public List<UMLClassifier> getClassifierModelBlocks() {
        List<UMLClassifier> modelBlocks= new ArrayList<>();
        modelBlocks.add(contextClassifier);
        modelBlocks.add(stateClassifier);
        return modelBlocks;
    }

    @Override
    public List<UMLOperation> getOperationModelBlocks() {
        return requestOperation;
    }

    @Override
    public List<UMLAttribute> getAttributeModelBlocks() {
        return stateAttributes;
    }

    @Override
    public List<RBMLMapping> map(SPS sps) {
        List<RBMLMapping> structuralMappings = new ArrayList<>();
        for (StructuralRole strRole : sps.getClassifierRoles()){
            for (UMLClassifier modelBlock : getClassifierModelBlocks()){
                if (modelBlock.getName().equals(strRole.getType())){

                }
            }
        }


    }

    public void printSummary(){
        System.out.println("Context role: " + contextClassifier.getName());
        System.out.println("State role: " + stateClassifier.getName());
        System.out.println("state role (attribute): " + stateAttribute.getName());
        for (UMLOperation op : requestOperation){
            System.out.println("Request() role (operation): " + op.getName());
        }
    }
}
