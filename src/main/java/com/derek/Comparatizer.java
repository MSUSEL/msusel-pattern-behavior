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

import com.derek.grime.GrimeFinder;
import com.derek.grime.GrimeSuite;
import com.derek.model.Model;
import com.derek.model.PatternInstanceEvolution;
import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Comparatizer {

    private Model model;
    private Map<SoftwareVersion, UMLClassDiagram> umlClassDiagrams;
    private StringBuilder outputter;

    //this guava table holds grime info. The table keys are SoftwareVersion, refering to the version this grime appears in, and
    // String, refering to the unique id of the pattern, referenced from PatternInstance.getUniqueID().
    // each entry will have a grimeSuite object, refering to all types of grime that exist for that pattern instance at that version.
    private Table<SoftwareVersion, String, GrimeSuite> grimeTable;

    //in addition to tracking a grime table, I think its important to track a metric table so that I can output all at the end.
    private Table<SoftwareVersion, String, MetricSuite> metricTable;


    public Comparatizer(Model model){
        this.model = model;
        this.umlClassDiagrams = model.getClassDiagramMap();
        outputter = new StringBuilder();
        grimeTable = HashBasedTable.create();
        metricTable = HashBasedTable.create();
    }

    public void runAnalysis(){
        String uniqueID = "";
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
                    uniqueID = pie.getFirstPatternInstance().getValueOfMajorRole(pie.getFirstPatternInstance());
                    if (pie.hasMinVersions()) {
                        for (Pair<SoftwareVersion, PatternInstance> pair : pie.getPatternLifetime()) {
                            if (pair.getValue() != null) {
                                //will happen when a pattern instance first appears after the first version number under analysis.
                                pair.getValue().setUniqueID(uniqueID);
                                SoftwareVersion version = pair.getLeft();
                                PatternInstance patternInstance = pair.getRight();
                                ConformanceResults conformanceResults = testComparisons(umlClassDiagrams.get(version), patternInstance);

                                //after we have gathered conformance, build metrics.
                                MetricSuite ms = new MetricSuite(conformanceResults);
                                metricTable.put(version, patternInstance.getUniqueID(), ms);

                                //after metrics, build grime table.
                                grimeTable.put(version, patternInstance.getUniqueID(), conformanceResults.getGrimeSuite());
                            }
                        }
                    }
                }
            }
        }
        //here I will have to do the actual checks for grime, looking at each form of grime across versions and finding changes.
        //this is because the grimesuite obj is just a 'snapshot' of pattern grime analysis, and contains measurements of the grime-related metrics
        //(such as TCC, RCI, TEAG, PEAG, etc.)

        GrimeFinder grimeFinder = new GrimeFinder(grimeTable);
        grimeFinder.findModularGrime();
        grimeFinder.findClassGrime();
        grimeFinder.findBehavioralGrime();

        for (String patternID : metricTable.columnKeySet()){
            for (SoftwareVersion version : metricTable.rowKeySet()){
                outputter.append(metricTable.get(version, patternID).getSummary());
                outputter.append(grimeFinder.getPeaGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getPeeGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getPiGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getTeaGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getTeeGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getTiGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getDipGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getDisGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getDepGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getDesGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getIipGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getIisGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getIepGrime().get(version, patternID).getTabDelimSummary());
                outputter.append(grimeFinder.getIesGrime().get(version, patternID).getTabDelimSummary());
                outputter.append("\n");
            }
        }

        output();
    }

    public ConformanceResults testComparisons(UMLClassDiagram umlClassDiagram, PatternInstance pi){
        ConformanceResults toRet;
        switch(pi.getPatternType()){
            case OBJECT_ADAPTER:
                toRet = compareObjectAdapter(pi, umlClassDiagram);
                break;
            case STATE:
                toRet = compareState(pi, umlClassDiagram);
                break;
            case OBSERVER:
                toRet = compareObserver(pi, umlClassDiagram);
                break;
            case TEMPLATE_METHOD:
                toRet = compareTemplateMethod(pi, umlClassDiagram);
                break;
            case SINGLETON:
                toRet = compareSingleton(pi, umlClassDiagram);
                break;
          default:
                toRet = null;
                break;
        }
        return toRet;
    }

    public ConformanceResults compareState(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        StatePattern statePattern = new StatePattern(pi, umlClassDiagram);
        SPS strictStateSPS = new SPS("configs/sps/statePatternSPS_strict.txt");
        IPS strictStateIPS = new IPS("configs/ips/statePatternIPS_strict.txt", strictStateSPS);
        ConformanceResults stateConformances = verifyConformance(strictStateSPS, strictStateIPS, statePattern, umlClassDiagram);
        return stateConformances;
    }

    public ConformanceResults compareObjectAdapter(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObjectAdapterPattern objectAdapterPattern = new ObjectAdapterPattern(pi, umlClassDiagram);
        SPS strictObjectAdapterSPS = new SPS("configs/sps/objectAdapterPatternSPS_strict.txt");
        IPS strictObjectAdapterIPS = new IPS("configs/ips/objectAdapterPatternIPS_strict.txt", strictObjectAdapterSPS);
        ConformanceResults objectAdapterConformances = verifyConformance(strictObjectAdapterSPS, strictObjectAdapterIPS, objectAdapterPattern, umlClassDiagram);
        return objectAdapterConformances;
    }

    public ConformanceResults compareObserver(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        ObserverPattern observerPattern = new ObserverPattern(pi, umlClassDiagram);
        SPS strictObserverSPS = new SPS("configs/sps/observerPatternSPS_strict.txt");
        IPS strictObserverIPS = new IPS("configs/ips/observerPatternIPS_strict.txt", strictObserverSPS);
        ConformanceResults observerConformances = verifyConformance(strictObserverSPS, strictObserverIPS, observerPattern, umlClassDiagram);
        return observerConformances;
    }

    public ConformanceResults compareTemplateMethod(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern(pi, umlClassDiagram);
        SPS strictTemplateMethodSPS = new SPS("configs/sps/templateMethodPatternSPS_strict.txt");
        IPS strictTemplateMethodIPS = new IPS("configs/ips/templateMethodPatternIPS_strict.txt", strictTemplateMethodSPS);
        ConformanceResults templateMethodConformances = verifyConformance(strictTemplateMethodSPS, strictTemplateMethodIPS, templateMethodPattern, umlClassDiagram);
        return templateMethodConformances;
    }

    public ConformanceResults compareSingleton(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        SingletonPattern singletonPattern = new SingletonPattern(pi, umlClassDiagram);
        SPS strictSingletonSPS = new SPS("configs/sps/singletonPatternSPS_strict.txt");
        IPS strictSingletonIPS = new IPS("configs/ips/singletonPatternIPS_strict.txt", strictSingletonSPS);
        ConformanceResults singletonConformances = verifyConformance(strictSingletonSPS, strictSingletonIPS, singletonPattern, umlClassDiagram);
        return singletonConformances;
    }

    /***
     * algorithm that checks conformance according to Kim's "Evaluating Pattern Conformance..." paper
     *
     * @param sps
     * @param patternMapper
     */
    public ConformanceResults verifyConformance(SPS sps, IPS ips, PatternMapper patternMapper, UMLClassDiagram umlClassDiagram){
        Conformance conformance = new Conformance(sps, ips, patternMapper, umlClassDiagram);
        List<RBMLMapping> rbmlStructureMappings = conformance.mapStructure();
        List<Pair<UMLOperation, BehaviorConformance>> rbmlBehaviorMappings = conformance.mapBehavior(rbmlStructureMappings);




        //grime checks here I think.
        GrimeSuite grimeSuite = new GrimeSuite(patternMapper, rbmlStructureMappings, rbmlBehaviorMappings);

        outputRoles(sps, rbmlStructureMappings, ips, rbmlBehaviorMappings, patternMapper);

        ConformanceResults conformanceResults = new ConformanceResults(patternMapper, sps, rbmlStructureMappings, ips, rbmlBehaviorMappings, grimeSuite);
        return conformanceResults;
    }

    private void printViolatedRoles(SPS sps, List<RBMLMapping> rbmlStructureMappings, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, StringBuilder output){
        output.append("***************************************\n");
        output.append("************Role Conformance***********\n");
        output.append("***************************************\n");
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
                output.append("Role " + role.getName() + " is satisfied.\n");
            }else{
                output.append("Role " + role.getName() + " is violated.\n");
            }
        }

        for (Pair<UMLOperation, BehaviorConformance> rbmlMapping : rbmlBehavioralMappings){
            output.append("Operation: " + rbmlMapping.getLeft().getName() + "\n");
            for (BehavioralMapping behavioralMapping : rbmlMapping.getRight().getBehavioralSatisfactions()){
                output.append("\t" + behavioralMapping.printMe() + "\n");
            }
            for (BehavioralMapping behavioralMapping : rbmlMapping.getRight().getBehavioralGrime()){
                output.append("\t" + behavioralMapping.printMe() + "\n");
            }
            //print violations
            for (InteractionRole interactionRole : rbmlMapping.getRight().getBehavioralViolations()){
                output.append("\t" + interactionRole.getName() + " is violated.\n");
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

    private void outputRoles(SPS sps, List<RBMLMapping> rbmlStructureMappings, IPS ips, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, PatternMapper patternMapper){
        try{
            if (Boolean.parseBoolean(Main.printIndividualRoles)){
                //main directory
                File mainDirectory = new File("roles\\");
                if (!mainDirectory.exists()) {
                    Files.createDirectory(Paths.get("roles\\"));
                }

                //sub-directories.
                File directory = new File("roles\\" + patternMapper.getPi().getPatternType() + "\\");
                if (!directory.exists()) {
                    Files.createDirectory(Paths.get("roles\\" + patternMapper.getPi().getPatternType() + "\\"));
                }

                StringBuilder output = new StringBuilder();
                output.append("Version: " + patternMapper.getPi().getSoftwareVersion().getVersionNum() + "\n");
                output.append("***************************************\n");
                output.append("**************Structure****************\n");
                output.append("***************************************\n");
                for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                        if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())) {
                            output.append("UML classifier: " + classifier.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName() + "\n");
                        }
                    }
                }
                for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                        if (rbmlMapping.getUmlArtifact().equals(operation.getValue())) {
                            output.append("UML operation: " + operation.getValue().getName() + " from classifier: " + operation.getValue().getOwningClassifier() + " has a mapping to " + rbmlMapping.getRole().getName() + "\n");
                        }
                    }
                }
                for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings) {
                        if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())) {
                            output.append("UML attribute: " + attribute.getValue().getName() + " from classifier: " + attribute.getValue().getOwningClassifier() + " has a mapping to " + rbmlMapping.getRole().getName() + "\n");
                        }
                    }
                }
                for (Relationship attribute : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.ASSOCIATION)) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                        Relationship relationship = rbmlMapping.getRelationshipArtifact();
                        if (relationship != null){
                            //this is a relationship
                            if (relationship.equals(attribute)){
                                output.append("Association role mapped from " + relationship.toString());
                            }
                        }
                    }
                }
                for (Relationship attribute : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.GENERALIZATION)) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                        Relationship relationship = rbmlMapping.getRelationshipArtifact();
                        if (relationship != null){
                            //this is a relationship
                            if (relationship.equals(attribute)){
                                output.append("Generalization role mapped from " + relationship.toString());
                            }
                        }
                    }
                }
                for (Relationship attribute : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.REALIZATION)) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                        Relationship relationship = rbmlMapping.getRelationshipArtifact();
                        if (relationship != null){
                            //this is a relationship
                            if (relationship.equals(attribute)){
                                output.append("Realization role mapped from " + relationship.toString());
                            }
                        }
                    }
                }
                for (Relationship attribute : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.DEPENDENCY)) {
                    for (RBMLMapping rbmlMapping : rbmlStructureMappings){
                        Relationship relationship = rbmlMapping.getRelationshipArtifact();
                        if (relationship != null){
                            //this is a relationship
                            if (relationship.equals(attribute)){
                                output.append("Dependency role mapped from " + relationship.toString());
                            }
                        }
                    }
                }
                output.append("***************************************\n");
                output.append("***************Behavior****************\n");
                output.append("***************************************\n");
                for (Pair<UMLOperation, BehaviorConformance> rbmlMapping : rbmlBehavioralMappings){
                    output.append(rbmlMapping.getLeft().getName() + " is mapped to " + rbmlMapping.getRight().getFunctionHeaderMapping().getName() + "\n");
                    for (Pair<CallTreeNode, InteractionRole> behaviorMapping : rbmlMapping.getRight().getRoleMap()){
                        output.append("\tMapped Lifeline: " + behaviorMapping.getLeft().getName() + " has a mapping to " + behaviorMapping.getRight().getName() + "\n");
                    }
                }

                printViolatedRoles(sps, rbmlStructureMappings, rbmlBehavioralMappings, output);

                File outputFile = new File("roles\\" + patternMapper.getPi().getPatternType() + "\\" + patternMapper.getPi().getUniqueID() + ".log");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
                out.println(output.toString());
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getOutputHeader(){
        String delim = "\t";
        StringBuilder header = new StringBuilder();
        header.append("Project_ID" + delim);
        header.append("Software_Version" + delim);
        header.append("Pattern_Type" + delim);
        header.append("Pattern_ID" + delim);
        header.append("Num_Participating_Classes" + delim);
        header.append("Num_Conforming_Structural_Roles" + delim);
        header.append("Num_NonConforming_Structural_Roles" + delim);
        header.append("Num_Conforming_Behavioral_Roles" + delim);
        header.append("Num_NonConforming_Behavioral_Roles" + delim);
        header.append("Num_Conforming_Roles_Total" + delim);
        header.append("Num_NonConforming_Roles_Total" + delim);
        header.append("SSize2" + delim);
        header.append("Afferent_Coupling" + delim);
        header.append("Efferent_Coupling" + delim);
        header.append("Coupling_Between_Pattern_Classes" + delim);
        header.append("Pattern_Structural_Integrity" + delim);
        header.append("Pattern_Behavioral_Integrity" + delim);
        header.append("Pattern_Integrity" + delim);
        header.append("Pattern_Instability" + delim);
        header.append(this.getModularGrimeOutputHeader("MG-PEA") + delim);
        header.append(this.getModularGrimeOutputHeader("MG-PEE") + delim);
        header.append(this.getModularGrimeOutputHeader("MG-PI") + delim);
        header.append(this.getModularGrimeOutputHeader("MG-TEA") + delim);
        header.append(this.getModularGrimeOutputHeader("MG-TEE") + delim);
        header.append(this.getModularGrimeOutputHeader("MG-TI") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-DIP") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-DIS") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-DEP") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-DES") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-IIP") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-IIS") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-IEP") + delim);
        header.append(this.getClassGrimeOutputHeader("CG-IES") + delim);
        header.append("\n");
        return header.toString();
    }

    private String getModularGrimeOutputHeader(String typeOfModularGrime){
        String delim = "\t";
        StringBuilder header = new StringBuilder();
        header.append(typeOfModularGrime + " grime count" + delim);
        header.append(typeOfModularGrime + " grime additions" + delim);
        header.append(typeOfModularGrime + " grime removals");
        return header.toString();
    }

    private String getClassGrimeOutputHeader(String typeOfClassGrime){
        String delim = "\t";
        StringBuilder header = new StringBuilder();
        header.append(typeOfClassGrime + " grime count (across all pattern classes)" + delim);
        header.append(typeOfClassGrime + " grime average (across all pattern classes)");
        return header.toString();
    }


}
