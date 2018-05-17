/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek;

import com.derek.model.*;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Comparatizer {

    private Map<PatternType, RBMLSpec> rbml;
    private Model model;
    private Map<SoftwareVersion, UMLClassDiagram> umlClassDiagrams;
    private StringBuilder outputter;


    public Comparatizer(Model model){
        this.model = model;
        this.umlClassDiagrams = model.getClassDiagramMap();
        rbml = new HashMap<>();
        outputter = new StringBuilder();
    }

    public void runAnalysis(){
        int uniqueID = 0;
        List<PatternType> typesToAnalyze = new ArrayList<>();
        typesToAnalyze.add(PatternType.OBJECT_ADAPTER);
        typesToAnalyze.add(PatternType.STATE);
        typesToAnalyze.add(PatternType.TEMPLATE_METHOD);
        typesToAnalyze.add(PatternType.OBSERVER);
        typesToAnalyze.add(PatternType.SINGLETON);
        for (PatternType type : typesToAnalyze) {
            if (model.getPatternEvolutions().get(type) != null) {
                //will be null if that pattern type does not exist in the project ever.
                for (PatternInstanceEvolution pie : model.getPatternEvolutions().get(type)) {
                    if (pie.hasMinVersions()) {
                        for (Pair<SoftwareVersion, PatternInstance> pair : pie.getPatternLifetime()) {
                            if (pair.getValue() != null) {
                                //will happen when a pattern instance first appears after the first version number under analysis.
                                pair.getValue().setUniqueID(uniqueID);
                                testComparisons(umlClassDiagrams.get(pair.getKey()), pair.getValue());
                                //System.out.println(pair.getKey() + " and " + pair.getValue());
                            }
                        }
                        uniqueID++;
                    }
                }
            }
        }


//        for (SoftwareVersion version : umlClassDiagrams.keySet()){
//            testComparisons(version, umlClassDiagrams.get(version));
//        }
        output();
    }

    public void testComparisons(UMLClassDiagram umlClassDiagram, PatternInstance pi){

        switch(pi.getPatternType()){
            case OBJECT_ADAPTER:
                compareObjectAdapter(pi, umlClassDiagram);
                break;
            case STATE:
                compareState(pi, umlClassDiagram);
                break;
            case OBSERVER:
                compareObserver(pi, umlClassDiagram);
                break;
            case TEMPLATE_METHOD:
                compareTemplateMethod(pi, umlClassDiagram);
                break;
            case SINGLETON:
                compareSingleton(pi, umlClassDiagram);
                break;
        }
    }

    public void compareCommand(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        CommandPattern commandPattern = new CommandPattern(pi, umlClassDiagram);
        commandPattern.mapToUML();
        SPS strictCommandSPS = new SPS("configs/sps/commandPatternSPS_strict.txt");
        IPS strictCommandIPS = new IPS("configs/sps/commandPatternIPS_strict.txt", strictCommandSPS);
        verifyConformance(strictCommandSPS, strictCommandIPS, commandPattern, umlClassDiagram);
    }

    public void compareState(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        StatePattern statePattern = new StatePattern(pi, umlClassDiagram);
        SPS strictStateSPS = new SPS("configs/sps/statePatternSPS_strict.txt");
        IPS strictStateIPS = null;//new IPS("resources/ips/statePatternIPS_strict.txt", strictStateSPS);
        verifyConformance(strictStateSPS, strictStateIPS, statePattern, umlClassDiagram);
    }

    public void compareObjectAdapter(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObjectAdapterPattern objectAdapterPattern = new ObjectAdapterPattern(pi, umlClassDiagram);
        SPS strictObjectAdapterSPS = new SPS("configs/sps/objectAdapterPatternSPS_strict.txt");
        IPS strictObjectAdapterIPS = null;//new IPS("resources/ips/objectAdapterPatternIPS_strict.txt", strictObjectAdapterSPS);
        verifyConformance(strictObjectAdapterSPS, strictObjectAdapterIPS, objectAdapterPattern, umlClassDiagram);
    }

    public void compareObserver(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObserverPattern observerPattern = new ObserverPattern(pi, umlClassDiagram);
        SPS strictObserverSPS = new SPS("configs/sps/observerPatternSPS_strict.txt");
        IPS strictObserverIPS = null;//new IPS("resources/ips/observerPatternIPS_strict.txt", strictObserverSPS);
        verifyConformance(strictObserverSPS, strictObserverIPS, observerPattern, umlClassDiagram);
    }

    public void compareTemplateMethod(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern(pi, umlClassDiagram);
        SPS strictTemplateMethodSPS = new SPS("configs/sps/templateMethodPatternSPS_strict.txt");
        IPS strictTemplateMethodIPS = null;// new IPS("resources/sps/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        verifyConformance(strictTemplateMethodSPS, strictTemplateMethodIPS, templateMethodPattern, umlClassDiagram);
    }

    public void compareSingleton(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        SingletonPattern singletonPattern = new SingletonPattern(pi, umlClassDiagram);
        SPS strictSingletonSPS = new SPS("configs/sps/singletonPatternSPS_strict.txt");
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
        if (Main.verboseLog) {
            for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()) {
                for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                    if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())) {
                        System.out.println(classifier.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                    }
                }
            }
            for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()) {
                for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                    if (rbmlMapping.getUmlArtifact().equals(operation.getValue())) {
                        System.out.println(operation.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                    }
                }
            }
            for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()) {
                for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                    if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())) {
                        System.out.println(attribute.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                    }
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> attribute : patternMapper.getRelationships(Relationship.ASSOCIATION)) {
                System.out.println("Association exists from: " + attribute.getKey().getName() + " to " + attribute.getValue().getName());
            }
            for (Pair<UMLClassifier, UMLClassifier> attribute : patternMapper.getRelationships(Relationship.GENERALIZATION)) {
                System.out.println("Generalization exists from: " + attribute.getKey().getName() + " to " + attribute.getValue().getName());
            }
            printViolatedRoles(sps, rbmlStructureMappings);
        }
        MetricSuite ms = new MetricSuite(rbmlStructureMappings, patternMapper, sps);
        outputter.append(ms.getSummary());
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

    private void output() {
        try {
            File f = new File(Main.outputFileName);
            BufferedWriter bf = new BufferedWriter(new FileWriter(f));
            bf.write(getOutputHeader());
            bf.write(outputter.toString());
            bf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getOutputHeader(){
        String separator = ", ";
        StringBuilder header = new StringBuilder();
        header.append("Project_ID" + separator);
        header.append("Software_Version" + separator);
        header.append("Pattern_Type" + separator);
        header.append("Pattern_ID" + separator);
        header.append("Num_Participating_Classes" + separator);
        header.append("Num_Conforming_Roles" + separator);
        header.append("Num_NonConforming_Roles" + separator);
        header.append("SSize2" + separator);
        header.append("Afferent_Coupling" + separator);
        header.append("Efferent_Coupling" + separator);
        header.append("Coupling_Between_Pattern_Classes" + separator);
        header.append("Pattern_Integrity" + separator);
        header.append("Pattern_Instability");
        header.append("\n");
        return header.toString();
    }


}
