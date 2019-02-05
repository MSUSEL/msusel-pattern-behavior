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
    private List<RBMLMapping>  rbmlBehavioralMappings;

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
        int currentAfferentClassifierUsages = patternMapper.getAfferentParticipants().size();
        int currentAfferentMethodUsages = 0;
        for (Relationship patternRelationship : patternMapper.getUniqueRelationshipsFromPatternClassifiers(relationshipType)){
            //3 cases - relationship is between pattern classes (could be internal grime) (could be external and efferent) (could be external and afferent)
            //pi first
            boolean relationshipExistsInRBML = false;
            for (Relationship validRBMLMapping : validRBMLMappings){
                if (patternRelationship.equals(validRBMLMapping)){
                    relationshipExistsInRBML = true;
                }
                if (patternRelationship.getFrom().isLanguageType() || patternRelationship.getTo().isLanguageType()){
                    //ignore langauge types... this is a big assumption but if we don't ignore language types then the code would do nothing...
                    relationshipExistsInRBML = true;
                }
                if (currentAfferentClassifierUsages <= Main.clientClassAllowances){
                    //if we are under the number of client class allowances, its not grime. Yet if its over then its def grime.
                    relationshipExistsInRBML = true;
                }
            }
            if (!relationshipExistsInRBML){
                //logic for the 3 cases starts here.
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getFrom())){
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternRelationship.getTo())){
                        //pi.
                        internal.add(patternRelationship);
                        System.out.println("Added internal grime: " + patternRelationship.getFrom().getName() + " to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    } else {
                        //relationship points from pattern member -> non-pattern member. (efferent)
                        efferent.add(patternRelationship);
                        System.out.println("Added efferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    }
                } else {
                    //relationship points from non-pattern member -> pattern member (afferent)
                    if (currentAfferentMethodUsages <= Main.clientUsageAllowances){
                        //not grime yet, because we allow a configurable number of client usages
                        currentAfferentMethodUsages++;
                    }else {
                        //though if we get here it really is grime, because we have surpassed the number of allowances for usages.
                        afferent.add(patternRelationship);
                        System.out.println("Added afferent grime: " + patternRelationship.getFrom().getName() + "  to " + patternRelationship.getTo().getName() + " as " + relationshipType);
                    }
                }
            }
        }
    }

    //for a pattern realization every class is subject to class grime.. I need to maintain a mapping from class to grime categoreis (strength, scope, and context)
    private void calculateClassGrime(){
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassesOnlyUMLClassifiers()){
            ClassGrimeMeasurements grime = new ClassGrimeMeasurements(umlClassifier, rbmlStructuralMappings);
            grime.calculateClassGrimeMeasurements();
            classGrimeMeasurementList.put(umlClassifier, grime);
        }
    }
}
