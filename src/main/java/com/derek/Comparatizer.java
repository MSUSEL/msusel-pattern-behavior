package com.derek;

import com.derek.model.Model;
import com.derek.model.PatternType;
import com.derek.model.RBMLSpec;
import com.derek.model.SoftwareVersion;
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
    private UMLClassDiagram umlClassDiagram;

    public Comparatizer(Model model, UMLClassDiagram umlClassDiagram){
        this.model = model;
        this.umlClassDiagram = umlClassDiagram;
        rbml = new HashMap<>();
    }

    public void testComparisons(){
        SoftwareVersion version = model.getVersions().get(0);

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
        compareObjectAdapter(piObjectAdapter);

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


        //factory tests
        /*
        PatternType patternType = PatternType.FACTORY_METHOD;
        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, patternType);
        PatternInstance pi = patternInstances.get(0);
        compareFactory(pi);
        */

    }


    public void compareFactory(PatternInstance pi){
//        //factory has "Creator" as the major role name.
//        UMLClassifier creatorMajorRole = getOneMajorRole(pi);
//        List<Pair<UMLClassifier, UMLOperation>> factoryMinorRoles = getMinorRoles(pi);
//
//        List<UMLClassifier> relevantClassifiers = new ArrayList<>();
//        relevantClassifiers.add(creatorMajorRole);
//        for (Pair<UMLClassifier, UMLOperation> p : factoryMinorRoles){
//            if (!relevantClassifiers.contains(p.getKey())) {
//                relevantClassifiers.add(p.getKey());
//            }
//        }
//        //print plantuml of surrounding uml for this pattern instance
//        //printWithRelationships(relevantClassifiers, 1);
//        System.out.println("matched pattern and classes!");

    }

    public void compareCommand(PatternInstance pi){
        CommandPattern commandPattern = new CommandPattern(pi, umlClassDiagram);
        commandPattern.mapToUML();
        SPS strictCommandSPS = new SPS("resources/sps/commandPatternSPS_strict.txt");
        IPS strictCommandIPS = new IPS("resources/sps/commandPatternIPS_strict.txt", strictCommandSPS);

        verifyConformance(strictCommandSPS, strictCommandIPS, commandPattern);


    }

    public void compareState(PatternInstance pi){
        StatePattern statePattern = new StatePattern(pi, umlClassDiagram);
        SPS strictStateSPS = new SPS("resources/sps/statePatternSPS_strict.txt");
        IPS strictStateIPS = new IPS("resources/ips/statePatternIPS_strict.txt", strictStateSPS);

        verifyConformance(strictStateSPS, strictStateIPS, statePattern);
//        for (Pair<String, UMLOperation> behaviors : statePattern.getOperationModelBlocks()){
//            System.out.println("call tree for: " + behaviors.getValue().getName());
//            behaviors.getValue().getCallTreeString().printTree();
//        }
    }

    public void compareObjectAdapter(PatternInstance pi){
        ObjectAdapterPattern objectAdapterPattern = new ObjectAdapterPattern(pi, umlClassDiagram);
        SPS strictObjectAdapterSPS = new SPS("resources/sps/objectAdapterPatternSPS_strict.txt");
        IPS strictObjectAdapterIPS = new IPS("resources/ips/objectAdapterPatternIPS_strict.txt", strictObjectAdapterSPS);
        verifyConformance(strictObjectAdapterSPS, strictObjectAdapterIPS, objectAdapterPattern);

        List<UMLClassifier> classifiers = new ArrayList<>();
        classifiers.addAll(objectAdapterPattern.getUMLClassifiers());
        //printWithRelationships(classifiers, 1);
    }

    public void compareObserver(PatternInstance pi){
        ObserverPattern observerPattern = new ObserverPattern(pi, umlClassDiagram);
        SPS strictObserverSPS = new SPS("resources/sps/observerPatternSPS_strict.txt");
        IPS strictObserverIPS = null;//new IPS("resources/ips/observerPatternIPS_strict.txt", strictObserverSPS);
        verifyConformance(strictObserverSPS, strictObserverIPS, observerPattern);
    }

    public void compareTemplateMethod(PatternInstance pi){
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern(pi, umlClassDiagram);
        SPS strictTemplateMethodSPS = new SPS("resources/sps/templateMethodPatternSPS_strict.txt");
        IPS strictTemplateMethodIPS = null;// new IPS("resources/sps/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        verifyConformance(strictTemplateMethodSPS, strictTemplateMethodIPS, templateMethodPattern);
    }

    public void compareSingleton(PatternInstance pi){
        SingletonPattern singletonPattern = new SingletonPattern(pi, umlClassDiagram);
        SPS strictSingletonSPS = new SPS("resources/sps/singletonPatternSPS_strict.txt");
        IPS strictSingletonIPS = null;// new IPS("resources/sps/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        verifyConformance(strictSingletonSPS, strictSingletonIPS, singletonPattern);
    }

    /***
     * algorithm that checks conformance according to Kim's "Evaluating Pattern Conformance..." paper
     *
     * @param sps
     * @param patternMapper
     */
    public void verifyConformance(SPS sps, IPS ips, PatternMapper patternMapper){
        Conformance conformance = new Conformance(sps, ips, patternMapper, umlClassDiagram);
        List<RBMLMapping> rbmlStructureMappings = conformance.mapStructure();
        //TODO  - uncomment and fix behavior mappings once I move onto beahvior.
        //List<RBMLMapping> rbmlBehaviorMappings = conformance.mapBehavior(rbmlStructureMappings);
//        for (RBMLMapping rbmlMapping : rbmlStructureMappings){
//            rbmlMapping.printSummary();
//        }
        for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()){
            if (classifier.getValue().getName().equals("AbstractFigure")){
                System.out.println("Abstract figure atts: ");
                for (UMLAttribute att : classifier.getValue().getAttributes()){
                    System.out.println(att.getName() + " and type "  + att.getType().getName());
                    if (!umlClassDiagram.getClassDiagram().edgeValue(classifier.getValue(), att.getType()).isPresent()){
                        System.out.println("no edge exists between " + classifier.getValue().getName() + " and " + att.getName() );
                    }
                }

                System.exit(0);
            }
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
        MetricSuite ms = new MetricSuite(rbmlStructureMappings, patternMapper);
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

    private void printWithRelationships(List<UMLClassifier> relevantClassifiers, int expanse){
        StringBuilder output = new StringBuilder();
        StringBuilder relationshipOutput = new StringBuilder();
        List<UMLClassifier> expanded = new ArrayList<>();
        expanded.addAll(relevantClassifiers);
        output.append("@startuml\n");
        for (int i = 0; i < expanse; i++){
            for (UMLClassifier node : umlClassDiagram.getClassDiagram().nodes()){
                List<UMLClassifier> tempClassifiers = new ArrayList<>();
                tempClassifiers.addAll(expanded);
                for (UMLClassifier relevant : expanded) {
                    if (umlClassDiagram.getClassDiagram().hasEdgeConnecting(node, relevant)){
                        if (!expanded.contains(node)){
                            tempClassifiers.add(node);
                            relationshipOutput.append(node.getName() + " ");
                            relationshipOutput.append(umlClassDiagram.getClassDiagram().edgeValue(node, relevant).get().plantUMLTransform() + " ");
                            relationshipOutput.append(relevant.getName() + "\n");
                        }
                    }else if (umlClassDiagram.getClassDiagram().hasEdgeConnecting(relevant, node)){
                        //need both directions because graph is directed.
                        if (!expanded.contains(node)){
                            tempClassifiers.add(node);
                            relationshipOutput.append(relevant.getName() + " ");
                            relationshipOutput.append(umlClassDiagram.getClassDiagram().edgeValue(relevant, node).get().plantUMLTransform() + " ");
                            relationshipOutput.append(node.getName() + "\n");
                        }
                    }
                }
                for (UMLClassifier temp : tempClassifiers){
                    if (!expanded.contains(temp)){
                        expanded.add(temp);
                    }
                }
            }
        }
        for (UMLClassifier expand : expanded){
            output.append(expand.plantUMLTransform());
        }
        output.append(relationshipOutput);
        output.append("@enduml\n");
        System.out.println(output);
    }


}
