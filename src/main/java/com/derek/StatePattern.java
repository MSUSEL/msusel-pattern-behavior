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

    @Override
    public List<RBMLMapping> map(SPS sps) {
        List<RBMLMapping> allMappings = new ArrayList<>();
        List<RBMLMapping> structuralMappings = mapStructure(sps);
        List<RBMLMapping> relationshipMappings = mapRelationships(sps, structuralMappings);


        allMappings.addAll(structuralMappings);
        allMappings.addAll(relationshipMappings);
        return allMappings;
    }

    public List<RBMLMapping> mapStructure(SPS sps){
        List<RBMLMapping> structuralMappings = new ArrayList<>();
        for (StructuralRole strRole : sps.getClassifierRoles()) {
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()) {
                if (modelBlockPair.getKey().equals(strRole.compareName())) {
                    //match found!
                    structuralMappings.add(new RBMLMapping(strRole, modelBlockPair.getValue()));
                }

            }
            for (AttributeRole attributeRole : strRole.getAttributes()) {
                for (Pair<String, UMLAttribute> attributePairBlock : getAttributeModelBlocks()) {
                    if (attributePairBlock.getKey().equals(attributeRole.compareName())) {
                        //attribute match found!
                        structuralMappings.add(new RBMLMapping(attributeRole, attributePairBlock.getValue()));
                    }
                }
            }
            for (OperationRole operationRole : strRole.getOperations()){
                for (Pair<String, UMLOperation> operationPairBlock : getOperationModelBlocks()) {
                    if (operationPairBlock.getKey().equals(operationRole.compareName())) {
                        //attribute match found!
                        structuralMappings.add(new RBMLMapping(operationRole, operationPairBlock.getValue()));
                    }
                }
            }
        }
        return structuralMappings;
    }

    public List<RBMLMapping> mapRelationships(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> relationshipMappings = new ArrayList<>();
        List<RBMLMapping> associationMappings = mapAssociations(sps, structuralMappings);
        List<RBMLMapping> generalizationMappings = mapGeneralizations(sps, structuralMappings);

        relationshipMappings.addAll(associationMappings);
        relationshipMappings.addAll(generalizationMappings);
        return relationshipMappings;
    }

    private List<RBMLMapping> mapGeneralizations(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> generalizationMappings = new ArrayList<>();
        for (RelationshipRole generalizationRole : sps.getGeneralizationRoles()){
            System.out.println("entering generalization role comparison: " + generalizationRole.getConnection1().getKey());
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(generalizationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = generalizationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        System.out.println("attempting to add generalization mapping from rbml : " + generalizationRole.getName() + " to classifiers: " + modelBlockPair.getValue() + " to " + mappedRoleEndpoint);
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.GENERALIZATION)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            generalizationMappings.add(new RBMLMapping(generalizationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                            System.out.println("added generalization mapping from rbml : " + generalizationRole.getName() + " to classifiers: " + modelBlockPair.getValue() + " to " + mappedRoleEndpoint);
                        }
                    }
                }
            }
        }
        return generalizationMappings;
    }

    private List<RBMLMapping> mapAssociations(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> associationMappings = new ArrayList<>();
        for (RelationshipRole associationRole : sps.getAssociationRoles()){
            System.out.println("entering association role comparison: " + associationRole.getConnection1().getKey());
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(associationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = associationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        System.out.println("attempting to add association mapping from rbml : " + associationRole.getName() + " to classifiers: " + modelBlockPair.getValue() + " to " + mappedRoleEndpoint);
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.ASSOCIATION)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            associationMappings.add(new RBMLMapping(associationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                            System.out.println("added association mapping from rbml : " + associationRole.getName() + " to classifiers: " + modelBlockPair.getValue() + " to " + mappedRoleEndpoint);
                        }
                    }
                }
            }
        }
        return associationMappings;
    }

    private RBMLMapping getMappingIfExists(List<RBMLMapping> mappings, StructuralRole structuralRole){
        for (RBMLMapping rbmlMapping : mappings){
            if (rbmlMapping.getMappedPair().getKey().equals(structuralRole)){
                return rbmlMapping;
            }
        }
        //might happen if structural role was not mapped in the first place.
        return null;
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
