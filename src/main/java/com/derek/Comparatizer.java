package com.derek;

import com.derek.model.Model;
import com.derek.model.PatternType;
import com.derek.model.RBMLSpec;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;

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
        PatternType patternType = PatternType.STATE;
        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, patternType);
        PatternInstance pi = patternInstances.get(0);
        compareState(pi);


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
        SPS strictCommand = new SPS("resources/sps/commandPatternSPS_strict.txt");

        //verifyConformance(strictCommand, commandPattern);


    }

    public void compareState(PatternInstance pi){
        StatePattern statePattern = new StatePattern(pi, umlClassDiagram);
        statePattern.mapToUML();
        SPS strictStateSPS = new SPS("resources/sps/statePatternSPS_strict.txt");
        IPS strictStateIPS = new IPS("resources/ips/statePatternIPS_strict.txt", strictStateSPS);

        verifyConformance(strictStateSPS, strictStateIPS, statePattern);
//        for (Pair<String, UMLOperation> behaviors : statePattern.getOperationModelBlocks()){
//            System.out.println("call tree for: " + behaviors.getValue().getName());
//            behaviors.getValue().getCallTreeString().printTree();
//        }
    }

    /***
     * algorithm that checks conformance according to Kim's "Evaluating Pattern Conformance..." paper
     *
     * @param sps
     * @param patternMapper
     */
    public void verifyConformance(SPS sps, IPS ips, PatternMapper patternMapper){
        List<RBMLMapping> rbmlStructureMappings = patternMapper.mapStructure(sps);
        List<RBMLMapping> rbmlBehaviorMappings = patternMapper.mapBehavior(rbmlStructureMappings, ips);
        for (RBMLMapping rbmlMapping : rbmlStructureMappings){
            rbmlMapping.printSummary();
        }
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
