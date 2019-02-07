package com.derek.grime;

import com.derek.BehaviorConformance;
import com.derek.Main;
import com.derek.PatternMapper;
import com.derek.rbml.RBMLMapping;
import com.derek.uml.Relationship;
import com.derek.uml.RelationshipType;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private List<RBMLMapping> rbmlBehavioralMappings;

    //list of class grime instances for a single pattern (pattern mapper obj)
    private Map<UMLClassifier, ClassGrimeMeasurements> classGrimeMeasurementList;


    //structural modular grime instances (persistent grime). When I want to calculate I will just call this.size();
    private List<Relationship> piGrimeInstances;
    private List<Relationship> peaGrimeInstances;
    private List<Relationship> peeGrimeInstances;

    //structural modular grime instances (temporary grime). When I want to calculate I will just call this.size();
    private List<Relationship> tiGrimeInstances;
    private List<Relationship> teaGrimeInstances;
    private List<Relationship> teeGrimeInstances;


    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, List<RBMLMapping> rbmlBehavioralMappings) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        classGrimeMeasurementList = new HashMap<>();
        calculateModularGrime();
        calculateClassGrime();
    }

    private void calculateModularGrime() {
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime2();
        //findModularGrime(piGrimeInstances, peaGrimeInstances, peeGrimeInstances, RelationshipType.ASSOCIATION);
        //findModularGrime(tiGrimeInstances, teaGrimeInstances, teeGrimeInstances, RelationshipType.DEPENDENCY);
    }

    private void findModularGrime2() {
        List<Relationship> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings) {
            Relationship relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null) {
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }
        int currentAfferentUsages = 0;
        for (Relationship patternAssociation : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.ASSOCIATION)) {
            if (patternAssociation.getFrom().getName().equals("Client")){
                System.out.println();
            }
            if (!isCapturedInRBML(validRBMLMappings, patternAssociation)) {
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getFrom())) {
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getTo())) {
                        //internal grime (pi)
                        piGrimeInstances.add(patternAssociation);
                    } else {
                        //efferent grime (pee)
                        peeGrimeInstances.add(patternAssociation);
                    }
                } else {
                    if (patternAssociation.getFrom().getName().equals("External") || patternAssociation.getFrom().getName().equals("Client")){
                        System.out.println("hi");
                    }
                    if (currentAfferentUsages < Main.clientClassAllowances){
                        //only a less than inequality; if its less than/equals then I am doing 1 more case.
                        currentAfferentUsages++;
                    }else{
                        peaGrimeInstances.add(patternAssociation);
                    }
                }
            }
        }
        int currentAfferentTemporaryUsages = 0;
        for (Relationship patternAssociation : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.DEPENDENCY)) {
            if (!isCapturedInRBML(validRBMLMappings, patternAssociation)) {
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getFrom())) {
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getTo())) {
                        //internal grime (ti)
                        tiGrimeInstances.add(patternAssociation);
                    } else {
                        //efferent grime (tee)
                        teeGrimeInstances.add(patternAssociation);
                    }
                } else {
                    if (currentAfferentTemporaryUsages < Main.clientUsageAllowances){
                        //only a less than inequality; if its less than/equals then I am doing 1 more case.
                        currentAfferentTemporaryUsages++;
                    }else{
                        teaGrimeInstances.add(patternAssociation);
                    }
                }
            }
        }
    }

    private void findModularGrime(List<Relationship> internal, List<Relationship> afferent, List<Relationship> efferent, RelationshipType relationshipType) {
        List<Relationship> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings) {
            Relationship relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null) {
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }
        int currentAfferentClassifierUsages = patternMapper.getUniqueAfferentParticipants().size();
        int currentAfferentMethodUsages = 0;
        for (Relationship patternRelationship : patternMapper.getUniqueRelationshipsFromPatternClassifiers(relationshipType)) {
            //3 cases - relationship is between pattern classes (could be internal grime) (could be external and efferent) (could be external and afferent)
            //pi first
            if (!isCapturedInRBML(validRBMLMappings, patternRelationship)) {
                //logic for the 3 cases starts here.
                if (patternMapper.getUniqueAfferentParticipants().size() > Main.clientClassAllowances) {
                    //afferent grime, this means we have too many classes referencing our pattern.
                    //afferent.add(patternRelationship);
                    //System.out.println("Afferent grime (unique): " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                }

                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getFrom())) {
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getTo())) {
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
//                    if (currentAfferentMethodUsages <= Main.clientUsageAllowances){
//                        //not grime yet, because we allow a configurable number of client usages
//                        currentAfferentMethodUsages++;
//                    }else {
                    //though if we get here it really is grime, because we have surpassed the number of allowances for usages.
                    afferent.add(patternRelationship);
                    System.out.println("Added afferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    //}
                }
            }
        }
    }

    private boolean isCapturedInRBML(List<Relationship> validRBMLMappings, Relationship r) {
        boolean relationshipExistsInRBML = false;
        for (Relationship validRBMLMapping : validRBMLMappings) {
            if (r.equals(validRBMLMapping)) {
                relationshipExistsInRBML = true;
            } else if (r.getFrom().isLanguageType() || r.getTo().isLanguageType()) {
                //ignore langauge types... this is a big assumption but if we don't ignore language types then the code would do nothing...
                relationshipExistsInRBML = true;
            }
        }
        return relationshipExistsInRBML;
    }

    //for a pattern realization every class is subject to class grime.. I need to maintain a mapping from class to grime categoreis (strength, scope, and context)
    private void calculateClassGrime() {
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassesOnlyUMLClassifiers()) {
            ClassGrimeMeasurements grime = new ClassGrimeMeasurements(umlClassifier, rbmlStructuralMappings);
            grime.calculateClassGrimeMeasurements();
            classGrimeMeasurementList.put(umlClassifier, grime);
        }
    }
}
