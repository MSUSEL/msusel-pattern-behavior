package com.derek;

import com.derek.rbml.IPS;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.Relationship;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private SPS sps;
    private List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings;
    private IPS ips;

    //count of behavioral repetition grime
    private int repetitionGrimeCount = 0;

    //count of improper order of sequences grime
    private int improperOrderGrimeCount = 0;

    //total behavioral grime.
    private int totalBehavioralGrimeCount = 0;

    //structural class grime counts
    private int dipgGrimeCount = 0;
    private int disgGrimeCount = 0;
    private int depgGrimeCount = 0;
    private int desgGrimeCount = 0;
    private int iipgGrimeCount = 0;
    private int iisgGrimeCount = 0;
    private int iepgGrimeCount = 0;
    private int iesgGrimeCount = 0;

    //structural modular grime instances (persistent grime). When I want to calculate I will just call this.size();
    private List<Pair<UMLClassifier, UMLClassifier>> piGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> peaGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> peeGrimeInstances;

    //structural modular grime instances (temporary grime). When I want to calculate I will just call this.size();
    private List<Pair<UMLClassifier, UMLClassifier>> tiGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> teaGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> teeGrimeInstances;


    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, SPS sps, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, IPS ips) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.sps = sps;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        this.ips = ips;
        calculateModularGrime();
    }

    private void calculateModularGrime(){
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime(piGrimeInstances, peaGrimeInstances, peeGrimeInstances, Relationship.ASSOCIATION);
        findModularGrime(tiGrimeInstances, teaGrimeInstances, teeGrimeInstances, Relationship.DEPENDENCY);
    }

    private void findModularGrime(List<Pair<UMLClassifier, UMLClassifier>> internal, List<Pair<UMLClassifier, UMLClassifier>> afferent, List<Pair<UMLClassifier, UMLClassifier>> efferent, Relationship relationshipType){
        List<Pair<UMLClassifier, UMLClassifier>> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings){
            Pair<UMLClassifier, UMLClassifier> relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null){
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }

        for (Pair<UMLClassifier, UMLClassifier> patternRelationship : patternMapper.getRelationships(relationshipType)){
            //3 cases - relationship is between pattern classes (could be internal grime) (could be external and efferent) (could be external and afferent)
            //pi first
            boolean relationshipExistsInRBML = false;
            for (Pair<UMLClassifier, UMLClassifier> validRBMLMapping : validRBMLMappings){
                if (patternMapper.areRelationshipsEqual(patternRelationship, validRBMLMapping)){
                    relationshipExistsInRBML = true;
                }
            }
            if (!relationshipExistsInRBML){
                //logic for the 3 cases starts here.
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getLeft())){
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getRight())){
                        //pi.
                        internal.add(patternRelationship);
                        System.out.println("Added internal grime: " + patternRelationship.getLeft().getName() + " to " + patternRelationship.getRight().getName() + " as " + relationshipType);
                    } else {
                        //relationship points from pattern member -> non-pattern member. (efferent)
                        efferent.add(patternRelationship);
                        System.out.println("Added efferent grime: " + patternRelationship.getLeft().getName() + "  to " + patternRelationship.getRight().getName() + " as " + relationshipType);
                    }
                } else {
                    //relationship points from non-pattern member -> pattern member (afferent)
                    afferent.add(patternRelationship);
                    System.out.println("Added afferent grime: " + patternRelationship.getLeft().getName() + "  to " + patternRelationship.getRight().getName() + " as " + relationshipType);
                }
            }
        }
    }
}
