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

    //structural modular grime instances. When I want to calculate I will just call this.size();
    private List<Pair<UMLClassifier, UMLClassifier>> piGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> peaGrimeInstances;
    private List<Pair<UMLClassifier, UMLClassifier>> peeGrimeInstances;

    //temporyar - TODO
    private int tiGrimeCount = 0;
    private int teaGrimeCount = 0;
    private int teeGrimeCount = 0;

    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, SPS sps, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, IPS ips) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.sps = sps;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        this.ips = ips;
        calculateModularGrime();
    }

    private void calculateModularGrime(){
        findPersistentGrime();
    }


    private void findPersistentGrime(){ //-- unwanted association relationships to pattern classes.
        //just use structure for this.
        //strategty: loop through all mapped relationships, if a relationship exists in a pattern that isn't in the mapping, its grime.

        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();

        List<Pair<UMLClassifier, UMLClassifier>> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings){
            Pair<UMLClassifier, UMLClassifier> relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null){
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }

        for (Pair<UMLClassifier, UMLClassifier> patternRelationship : patternMapper.getRelationships(Relationship.ASSOCIATION)){
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
                        piGrimeInstances.add(patternRelationship);
                    } else {
                        //relationship points from pattern member -> non-pattern member. (efferent)
                        peeGrimeInstances.add(patternRelationship);
                    }
                } else {
                    //relationship points from non-pattern member -> pattern member (afferent)
                    peaGrimeInstances.add(patternRelationship);
                }
            }
        }
    }





}
