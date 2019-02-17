package com.derek.grime;

import com.derek.BehaviorConformance;
import com.derek.BehavioralMapping;
import com.derek.Main;
import com.derek.PatternMapper;
import com.derek.rbml.InteractionRole;
import com.derek.rbml.RBMLMapping;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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
        calculateOrderGrime();

    }

    private void calculateModularGrime() {
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime();
    }

    private void findModularGrime() {
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

    private void calculateOrderGrime(){
        for (Relationship afferentParticipants : patternMapper.getAfferentRelationships()){
            UMLClassifier afferentClassifier = afferentParticipants.getFrom();
            List<UMLAttribute> classVarUsages = afferentClassifier.getAttributes();
            System.out.println(afferentClassifier.getName() + " has vars at class level: " + classVarUsages);
            for (UMLOperation umlOperation : afferentClassifier.getOperationsIncludingConstructorsIfExists()){


                umlOperation.getCallTreeString().printTree();

                List<CallTreeNode<String>> callTree = umlOperation.getCallTreeString().convertMeToOrderedList();
                for (CallTreeNode<String> callTreeNode : callTree){
                    if (callTreeNode.isDecl()){
                        String declTagName = callTreeNode.parseDeclTagName();
                        //got the decl name, now I need to match to pattern classes to see if the variable declaration is indeed an afferent connection. This would be a temporary
                        //grime artifact I think.
                    }if (callTreeNode.isCall()){

                    }
                }
            }

        }
//        for (RBMLMapping rbmlBehaviorMapping : this.rbmlBehavioralMappings){
//            BehaviorConformance bc = rbmlBehaviorMapping.getBehavioralConformance();
//            if (bc != null){
//                bc.printPresenceMap();
//            }
//        }
    }

}
