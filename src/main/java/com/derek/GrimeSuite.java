package com.derek;

import com.derek.rbml.RBMLMapping;
import com.derek.uml.Relationship;
import com.derek.uml.RelationshipType;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings;

    //count of behavioral repetition grime
    private int repetitionGrimeCount = 0;

    //count of improper order of sequences grime
    private int improperOrderGrimeCount = 0;

    //total behavioral grime.
    private int totalBehavioralGrimeCount = 0;

    private Map<UMLClassifier, ClassGrime> classGrimeList;


    //structural modular grime instances (persistent grime). When I want to calculate I will just call this.size();
    private List<Relationship> piGrimeInstances;
    private List<Relationship> peaGrimeInstances;
    private List<Relationship> peeGrimeInstances;

    //structural modular grime instances (temporary grime). When I want to calculate I will just call this.size();
    private List<Relationship> tiGrimeInstances;
    private List<Relationship> teaGrimeInstances;
    private List<Relationship> teeGrimeInstances;


    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        classGrimeList = new HashMap<>();
        calculateModularGrime();
        calculateClassGrime();
    }

    private void calculateModularGrime(){
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime(piGrimeInstances, peaGrimeInstances, peeGrimeInstances, RelationshipType.ASSOCIATION);
        findModularGrime(tiGrimeInstances, teaGrimeInstances, teeGrimeInstances, RelationshipType.DEPENDENCY);
    }

    private void findModularGrime(List<Relationship> internal, List<Relationship> afferent, List<Relationship> efferent, RelationshipType relationshipType){
        List<Relationship> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings){
            Relationship relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null){
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }

        for (Relationship patternRelationship : patternMapper.getUniqueRelationshipsFromPatternClassifiers(relationshipType)){
            //3 cases - relationship is between pattern classes (could be internal grime) (could be external and efferent) (could be external and afferent)
            //pi first
            boolean relationshipExistsInRBML = false;
            for (Relationship validRBMLMapping : validRBMLMappings){
                if (patternRelationship.equals(validRBMLMapping)){
                    relationshipExistsInRBML = true;
                }
            }
            if (!relationshipExistsInRBML){
                //logic for the 3 cases starts here.
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getFrom())){
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getTo())){
                        //pi.
                        internal.add(patternRelationship);
                        //System.out.println("Added internal grime: " + patternRelationship.getFrom().getName() + " to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    } else {
                        //relationship points from pattern member -> non-pattern member. (efferent)
                        efferent.add(patternRelationship);
                        //System.out.println("Added efferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    }
                } else {
                    //relationship points from non-pattern member -> pattern member (afferent)
                    afferent.add(patternRelationship);
                    //System.out.println("Added afferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                }
            }
        }
    }

    //for a pattern realization every class is subject to class grime.. I need to maintain a mapping from class to grime categoreis (strength, scope, and context)
    private void calculateClassGrime(){
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassesOnlyUMLClassifiers()){
            ClassGrime grime = new ClassGrime(umlClassifier);
            grime.findClassGrime();
            classGrimeList.put(umlClassifier, grime);
            System.out.println("Class " + umlClassifier.getName() + " has TCC: " + classGrimeList.get(umlClassifier).getTCC());
            System.out.println("Class " + umlClassifier.getName() + " has RCI: " + classGrimeList.get(umlClassifier).getRCI());
        }
    }
}
