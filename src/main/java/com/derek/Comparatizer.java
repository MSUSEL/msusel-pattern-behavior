package com.derek;

import com.derek.model.*;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comparatizer {

    private Map<PatternType, RBMLSpec> rbml;
    private Model model;
    private Map<SoftwareVersion, UMLClassDiagram> umlClassDiagrams;

    public Comparatizer(Model model){
        this.model = model;
        this.umlClassDiagrams = model.getClassDiagramMap();
        rbml = new HashMap<>();
    }

    public void runAnalysis(){
        for (PatternInstanceEvolution pie : model.getPatternEvolutions().get(PatternType.OBJECT_ADAPTER)){
            for (Pair<SoftwareVersion, PatternInstance> pair : pie.getPatternLifetime()){
                System.out.println(pair.getKey() + " and " + pair.getValue());
            }
        }

//        for (SoftwareVersion version : umlClassDiagrams.keySet()){
//            testComparisons(version, umlClassDiagrams.get(version));
//        }
    }

    public void testComparisons(SoftwareVersion version, UMLClassDiagram umlClassDiagram){

        //command tests
//        PatternType patternType = PatternType.COMMAND;
//        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, patternType);
//        PatternInstance pi = patternInstances.get(0);
//        compareCommand(pi);

        //state tests
//        PatternType statePatternType = PatternType.STATE;
//        List<PatternInstance> statePatternInstances = model.getPatternSummaryTable().get(version, statePatternType);
//        PatternInstance piState = statePatternInstances.get(0);
//        compareState(piState);

        //object adapter tests
        PatternType objectAdapterPatternType = PatternType.OBJECT_ADAPTER;
        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, objectAdapterPatternType);
        PatternInstance piObjectAdapter = patternInstances.get(0);
        compareObjectAdapter(piObjectAdapter, umlClassDiagram);

        //observer tests
//        PatternType observerPatternType = PatternType.OBSERVER;
//        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, observerPatternType);
//        PatternInstance piObserver = patternInstances.get(2);
//        compareObserver(piObserver);

        //Template method tests
//        PatternType templateMethodPatternType = PatternType.TEMPLATE_METHOD;
//        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, templateMethodPatternType);
//        PatternInstance piTemplateMethod = patternInstances.get(1);
//        compareTemplateMethod(piTemplateMethod);

        //Singleton tests
//        PatternType singletonType = PatternType.SINGLETON;
//        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, singletonType);
//        PatternInstance piSingleton = patternInstances.get(0);
//        compareSingleton(piSingleton);
    }

    public void compareCommand(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        CommandPattern commandPattern = new CommandPattern(pi, umlClassDiagram);
        commandPattern.mapToUML();
        SPS strictCommandSPS = new SPS("resources/sps/commandPatternSPS_strict.txt");
        IPS strictCommandIPS = new IPS("resources/sps/commandPatternIPS_strict.txt", strictCommandSPS);
        verifyConformance(strictCommandSPS, strictCommandIPS, commandPattern, umlClassDiagram);
    }

    public void compareState(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        StatePattern statePattern = new StatePattern(pi, umlClassDiagram);
        SPS strictStateSPS = new SPS("resources/sps/statePatternSPS_strict.txt");
        IPS strictStateIPS = null;//new IPS("resources/ips/statePatternIPS_strict.txt", strictStateSPS);
        verifyConformance(strictStateSPS, strictStateIPS, statePattern, umlClassDiagram);
    }

    public void compareObjectAdapter(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObjectAdapterPattern objectAdapterPattern = new ObjectAdapterPattern(pi, umlClassDiagram);
        SPS strictObjectAdapterSPS = new SPS("resources/sps/objectAdapterPatternSPS_strict.txt");
        IPS strictObjectAdapterIPS = null;//new IPS("resources/ips/objectAdapterPatternIPS_strict.txt", strictObjectAdapterSPS);
        verifyConformance(strictObjectAdapterSPS, strictObjectAdapterIPS, objectAdapterPattern, umlClassDiagram);
    }

    public void compareObserver(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObserverPattern observerPattern = new ObserverPattern(pi, umlClassDiagram);
        SPS strictObserverSPS = new SPS("resources/sps/observerPatternSPS_strict.txt");
        IPS strictObserverIPS = null;//new IPS("resources/ips/observerPatternIPS_strict.txt", strictObserverSPS);
        verifyConformance(strictObserverSPS, strictObserverIPS, observerPattern, umlClassDiagram);
    }

    public void compareTemplateMethod(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern(pi, umlClassDiagram);
        SPS strictTemplateMethodSPS = new SPS("resources/sps/templateMethodPatternSPS_strict.txt");
        IPS strictTemplateMethodIPS = null;// new IPS("resources/sps/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        verifyConformance(strictTemplateMethodSPS, strictTemplateMethodIPS, templateMethodPattern, umlClassDiagram);
    }

    public void compareSingleton(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        SingletonPattern singletonPattern = new SingletonPattern(pi, umlClassDiagram);
        SPS strictSingletonSPS = new SPS("resources/sps/singletonPatternSPS_strict.txt");
        IPS strictSingletonIPS = null;// new IPS("resources/sps/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        verifyConformance(strictSingletonSPS, strictSingletonIPS, singletonPattern, umlClassDiagram);
    }

    /***
     * algorithm that checks conformance according to Kim's "Evaluating Pattern Conformance..." paper
     *
     * @param sps
     * @param patternMapper
     */
    public void verifyConformance(SPS sps, IPS ips, PatternMapper patternMapper, UMLClassDiagram umlClassDiagram){
        Conformance conformance = new Conformance(sps, ips, patternMapper, umlClassDiagram);
        List<RBMLMapping> rbmlStructureMappings = conformance.mapStructure();
        //TODO  - uncomment and fix behavior mappings once I move onto beahvior.
        //List<RBMLMapping> rbmlBehaviorMappings = conformance.mapBehavior(rbmlStructureMappings);
//        for (RBMLMapping rbmlMapping : rbmlStructureMappings){
//            rbmlMapping.printSummary();
//        }
        for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()){
             for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())){
                    System.out.println(classifier.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
        }
        for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()){
            for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                if (rbmlMapping.getUmlArtifact().equals(operation.getValue())){
                    System.out.println(operation.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
        }
        for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()){
            for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())){
                    System.out.println(attribute.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
        }
        for (Pair<UMLClassifier, UMLClassifier> attribute : patternMapper.getRelationships(Relationship.ASSOCIATION)){
            System.out.println("Association exists from: " + attribute.getKey().getName() + " to " + attribute.getValue().getName());
        }
        for (Pair<UMLClassifier, UMLClassifier> attribute : patternMapper.getRelationships(Relationship.GENERALIZATION)){
            System.out.println("Generalization exists from: " + attribute.getKey().getName() + " to " + attribute.getValue().getName());
        }
        printViolatedRoles(sps, rbmlStructureMappings);
        MetricSuite ms = new MetricSuite(rbmlStructureMappings, patternMapper, sps);
        ms.printSummary();
    }

    private void printViolatedRoles(SPS sps, List<RBMLMapping> rbmlStructureMappings){
        //print all things that don't conform.
        List<Role> conformingRoles = new ArrayList<>();
        for (Role role : sps.getAllRoles()){
            for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                if (rbmlMapping.getRole().equals(role) && !conformingRoles.contains(role)){
                    conformingRoles.add(role);
                }
            }
        }
        //sps conformance checks - but this is printing; it does not include any logic.
        for (Role role : sps.getAllRoles()){
            if (conformingRoles.contains(role)){
                System.out.println("Role " + role.getName() + " is satisfied.");
            }else{
                System.out.println("Role " + role.getName() + " is violated.");
            }
        }
    }


}
