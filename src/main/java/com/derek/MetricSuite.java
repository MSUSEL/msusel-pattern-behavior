package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.RBMLMapping;
import com.derek.uml.Relationship;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;

import java.util.List;

public class MetricSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlMappings;

    //number of participating classes
    private int numParticipatingClasses = 0;

    //number of conforming roles, and nonconforming roles, respectively
    private int numConformingRoles = 0;
    private int numNonConformingRoles =0;

    public MetricSuite(List<RBMLMapping> rbmlMappings, PatternMapper patternMapper){
        this.patternMapper = patternMapper;
        this.rbmlMappings = rbmlMappings;
        calculate();
    }


    private void calculate(){
        calcNumParticipatingClasses();
        calcNumConformingRoles();
    }

    private void calcNumParticipatingClasses(){
        numParticipatingClasses = patternMapper.getClassifierModelBlocks().size();
    }

    private void calcNumConformingRoles(){
        for (RBMLMapping rbmlMapping : rbmlMappings){
            for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())){
                    //found conforming classifier role
                    numConformingRoles++;
                    System.out.println(classifier.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(operation.getValue())){
                    //found conforming operation role
                    numConformingRoles++;
                    System.out.println(operation.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())){
                    //found conforming attribute role
                    numConformingRoles++;
                    System.out.println(attribute.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> association : patternMapper.getRelationships(Relationship.ASSOCIATION)){
                if (rbmlMapping.getUmlArtifact().equals(association)){
                    //will be a pair.
                    numConformingRoles++;
                    System.out.println("Association pair " + association.getKey() + " to " + association.getValue() + " has a mappting to " + rbmlMapping.getUmlArtifact());
                }
            }
        }
    }

}
