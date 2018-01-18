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
package com.derek.model;

import com.derek.Main;
import com.derek.model.patterns.*;
import com.derek.uml.UMLClassDiagram;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.jboss.shrinkwrap.descriptor.api.Mutable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

@Getter
@Setter
public class Model {

    private Table<SoftwareVersion, PatternType, List<PatternInstance>> patternSummaryTable;
    private Map<SoftwareVersion, UMLClassDiagram> classDiagramMap;
    private List<SoftwareVersion> versions;

    private Map<PatternType, List<PatternInstanceEvolution>> patternEvolutions;


    private final int numSharedClassesToConstitutePatternCoupling = 1;

    public Model(List<SoftwareVersion> versions){
        this.versions = versions;
        patternEvolutions = new TreeMap<>();
        patternSummaryTable = TreeBasedTable.create();

        parseDataIntoPatternDataStructure(Main.workingDirectory + "pattern_detections/");

        //printPatternTable();

        //holy shit it works.
        buildPatternEvolutions();



        //printPatternDataStructure();
        //showAllFactoryMethodEvolutions();

        //printPatternTable();


        //printFactoryEvolutions();
        //printFactoryAdditions();

        //findPatternCoupling(numSharedClassesToConstitutePatternCoupling);

    }

    //this method searches across all patterns to find classes that fill shared roles across different (or same) pattern types.
    //parameterized by the number of shared classes
    public void findPatternCoupling(int numSharedClasses){
        Map<SoftwareVersion, List<Pair<PatternInstance, PatternInstance>>> coupledPatterns = new HashMap<>();
        for (SoftwareVersion v : patternSummaryTable.rowKeySet()) {

            List<Pair<PatternInstance, PatternInstance>> coupledPatternsForThisVersion = new ArrayList<>();
            for (PatternType outerType : patternSummaryTable.columnKeySet()){
                for (PatternType innerType : patternSummaryTable.columnKeySet()){
                    List<PatternInstance> outerLoop = patternSummaryTable.get(v, outerType);
                    List<PatternInstance> innerLoop = patternSummaryTable.get(v, innerType);
                    for (PatternInstance piOuter : outerLoop){
                        for (Pair<String, String> outerPIClass : piOuter.getListOfPatternRoles()){
                            String outerElement = outerPIClass.getValue();
                            for (PatternInstance piInner : innerLoop) {
                                if (piOuter != piInner) {
                                    //ignore the exact same pattern
                                    int numSameClasses = 0;
                                    for (Pair<String, String> innerPIClass : piInner.getListOfPatternRoles()) {
                                        String innerElement = innerPIClass.getValue();
                                        if (outerElement.equals(innerElement)) {
                                            //same class fulfills two different roles.
                                            numSameClasses++;
                                            if (numSharedClasses <= numSameClasses) {
                                                //the number of pattern-sharing classes in the project is sufficient for our pattern coupling criteria
                                                Pair<PatternInstance, PatternInstance> coupledPatternClasses = new Pair<>(piOuter, piInner);
                                                if (!doesPairExistInList(coupledPatternClasses, coupledPatternsForThisVersion)) {
                                                    coupledPatternsForThisVersion.add(coupledPatternClasses);
                                                }
                                                break;//break out of innerPIClass loop, because we only want to match once.
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            coupledPatterns.put(v, coupledPatternsForThisVersion);
        }

        //printing this too. Refactor this eventually, moving to its own method.
        printCoupledPatterns(coupledPatterns);
    }

    private boolean doesPairExistInList(Pair<PatternInstance, PatternInstance> coupledPatternClasses, List<Pair<PatternInstance, PatternInstance>> coupledPatternsForThisVersion){
        boolean toRet = false;

        for (Pair<PatternInstance, PatternInstance> existingCoupledPatterns : coupledPatternsForThisVersion){
            if (coupledPatternClasses.getKey() == existingCoupledPatterns.getKey() && coupledPatternClasses.getValue() == existingCoupledPatterns.getValue()){
                toRet = true;
            }
            if (coupledPatternClasses.getKey() == existingCoupledPatterns.getValue() && coupledPatternClasses.getValue() == existingCoupledPatterns.getKey()){
                toRet = true;
            }
        }
        return toRet;
    }

    private void printCoupledPatterns(Map<SoftwareVersion, List<Pair<PatternInstance, PatternInstance>>> coupledPatterns){
        try {
            File f = new File("PatternEvolutions.log");
            PrintStream ps = new PrintStream(f);
            for (SoftwareVersion v : coupledPatterns.keySet()) {
                ps.println(v);
                for (Pair<PatternInstance, PatternInstance> coupledPatternPair : coupledPatterns.get(v)) {
                    ps.println('\t' + coupledPatternPair.getKey().toString() + "   " + coupledPatternPair.getValue().toString());
                }
            }
            ps.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void printFactoryEvolutions(){
        List<PatternInstanceEvolution> factories = patternEvolutions.get(PatternType.FACTORY_METHOD);

        System.out.println("Factory Evolution:");
        for (PatternInstanceEvolution pie : factories){
            System.out.println(pie.getFirstPatternInstance().toString() + pie.getCSVVersions());
        }
    }

    public void buildPatternEvolutions(){
        for (SoftwareVersion v : patternSummaryTable.rowKeySet()){
            for (PatternType pt : patternSummaryTable.columnKeySet()){
                for (PatternInstance pi : patternSummaryTable.get(v, pt)){
                    //every pattern instance needs to be a part of at least some evolution
                    //existingPAtternEvolutions is w.r.t. pattern type.
                    List<PatternInstanceEvolution> existingPatternEvolutions = patternEvolutions.get(pt);
                    if (existingPatternEvolutions == null){
                        //first time through loop; we have found no pattern evolution objects for this pattern type
                        existingPatternEvolutions = new ArrayList<>();
                        //in this, pi is a list of patternInstances.
                        existingPatternEvolutions.add(new PatternInstanceEvolution(v, pt, pi));
                        patternEvolutions.put(pt, existingPatternEvolutions);
                    }else{
                        //following method adds a pattern instance to the evolution if it exists (the evolution object).
                        //thats why we check for false.
                        if (!doesPatternInstanceExistInEvolutions(pi, existingPatternEvolutions, v)){
                            existingPatternEvolutions.add(new PatternInstanceEvolution(v, pt, pi));
                        }
                    }
                }
            }
        }
    }

    //checks to see if a pattern instance exists in pattern instance evolutions, and returns false if not.
    //if true. this method also adds the pattern instance to the evolution list.
    private boolean doesPatternInstanceExistInEvolutions(PatternInstance pi, List<PatternInstanceEvolution> pies, SoftwareVersion v){
        boolean toRet = false;
        for (PatternInstanceEvolution pie : pies){
            if (pie.getFirstPatternInstance().isInstanceEqual(pi)){
                //found a pattern instance evolution


                pie.addPatternInstanceToEvolution(pi, v);
                //pie.addPatternInstanceToEvolution(pi, v);
                toRet = true;
                return toRet;
            }
        }
        return toRet;
    }

    private void printPatternTable(){
        System.out.println(patternSummaryTable.rowKeySet());
        System.out.println(patternSummaryTable.columnKeySet());

        for (SoftwareVersion v : patternSummaryTable.rowKeySet()){
            System.out.println(v);
            for (PatternType pt : patternSummaryTable.columnKeySet()){
                System.out.print("\t" + pt + " instances: ");
                for (PatternInstance pi : patternSummaryTable.get(v, pt)){
                    System.out.print(pi.toString() + ", ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void parseDataIntoPatternDataStructure(String fileName){
        try{
            //https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
            File dir = new File(fileName);
            for (File f : dir.listFiles()){
                //assume all files in here are xml

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                SoftwareVersion version = getVersionFromFileName(f.getName());

                NodeList patterns = doc.getElementsByTagName("pattern");

                //iterate through pattern types within the xml
                for (int i = 0; i < patterns.getLength(); i++){
                    Node patternIter = patterns.item(i);
                    if (patternIter.getNodeType() == Node.ELEMENT_NODE){
                        Element patternTypeElement = (Element) patternIter;

                        PatternType patternType = matchPatternEnum(patternTypeElement.getAttribute("name"));

                        NodeList patternInstancesNodeList = patternTypeElement.getElementsByTagName("instance");

                        List<PatternInstance> patternInstances = new ArrayList<>();

                        //iterate through pattern instances of each pattern type
                        for (int j = 0; j < patternInstancesNodeList.getLength(); j++) {
                            Node patternInstanceIter = patternInstancesNodeList.item(j);

                            Element patternInstanceElement = (Element) patternInstanceIter;
                            NodeList patternRoles = patternInstanceElement.getElementsByTagName("role");

                            List<Pair<String, String>> listOfRoles = new ArrayList<>();
                            for (int k = 0; k < patternRoles.getLength(); k++) {
                                Node roleNode = patternRoles.item(k);

                                String role = roleNode.getAttributes().getNamedItem("name").getTextContent();
                                String element = roleNode.getAttributes().getNamedItem("element").getTextContent();
                                Pair<String, String> p = new Pair<>(role, element);
                                listOfRoles.add(p);

                            }
                            //add pattern instance to list of namesake.

                            patternInstances.add(buildPatternInstance(listOfRoles, patternType, version));
                        }
                        System.out.println("Placing pattern instance: " + patternInstances + " into version: " + version.getVersionNum());
                        patternSummaryTable.put(version, patternType, patternInstances);
                    }
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private PatternInstance buildPatternInstance(List<Pair<String, String>> listOfRoles, PatternType patternType, SoftwareVersion version){
        switch(patternType){
            //patterns with one major role
            case FACTORY_METHOD:
            case CHAIN_OF_RESPONSIBILITY:
            case TEMPLATE_METHOD:
            case SINGLETON:

            //patterns with two major roles
            case OBJECT_ADAPTER:
            case COMMAND:
            case DECORATOR:
            case STATE:
            case STRATEGY:
            case BRIDGE:
            case PROXY:
            case PROXY2:
                return new PatternInstance(listOfRoles, patternType, version);


            //patterns that have not been detected yet.
            case PROTOTYPE:return null;//TODO;
            case COMPOSITE:return null;//TODO
            case OBSERVER:return null;//TODO
            case VISITOR:return null; //TODO
            default:
                System.out.println("Did not match a pattern type with a built-in class of that type. Exiting and fix ");
                System.exit(0);
        }
        return null;
    }

    //different from the above mehtod with the same name because this one focuses specifically on a single pattern instance,
    //not on the number of pattern instances in the project.
    public MutableGraph<PatternInstance> buildPatternEvolution(PatternInstance pi){
        MutableGraph<PatternInstance> patternEvolution = GraphBuilder.directed().allowsSelfLoops(false).build();

        for (SoftwareVersion v : patternSummaryTable.rowKeySet()) {
            for (PatternType pt : patternSummaryTable.columnKeySet()) {
                if (pi.getPatternType().equals(pt)){
                    //same pattern type; not necessary but filters some pattern instances
                    for (PatternInstance allPI : patternSummaryTable.get(v, pt)) {
                        if (pi.isInstanceEqual(allPI)) {
                            //same pattern, (hopefully)
                            patternEvolution.addNode(allPI);
                        }
                    }
                }
            }
        }

        List<PatternInstance> orderedPIs = new ArrayList<>();
        //fill nodes
        for (PatternInstance node : patternEvolution.nodes()){
            orderedPIs.add(node);
        }
        orderedPIs.sort(Comparator.comparing(PatternInstance::getSoftwareVersion));
        //sort list
        //https://stackoverflow.com/questions/16252269/how-to-sort-an-arraylist
//        Collections.sort(orderedPIs, new Comparator<PatternInstance>() {
//            @Override
//            public int compare(PatternInstance patternInstance, PatternInstance t1) {
//                return patternInstance.getSoftwareVersion().getVersionNum() - t1.getSoftwareVersion().getVersionNum();
//            }
//        });

        //add edges
        for (int i = 0; i < orderedPIs.size()-1; i++){
            patternEvolution.putEdge(orderedPIs.get(i), orderedPIs.get(i+1));
        }

        return patternEvolution;
    }

    /**
     *   expects the filename as a string for input, and finds the software version from the file name.
     *   forms are: "pattern_detector{\d}+.xml"
     *
     * @param s
     * @return
     */
    private SoftwareVersion getVersionFromFileName(String s){
        s = stripSpecialChars(s);
        int actualVersion = Integer.parseInt(s.substring(16, s.lastIndexOf('.')));
        for (SoftwareVersion version : versions){
            if (version.getVersionNum() == actualVersion){
                return version;
            }
        }
        return null;
    }

    private String stripSpecialChars(String s){
        return s.replaceAll("-","");
    }


    /*
    matches an xml string output from the pattern detector tool with the built-in enums in PAtternType

     */
    private PatternType matchPatternEnum(String s){
    s = s.toLowerCase();
        switch(s){
            case "factory method":
                return PatternType.FACTORY_METHOD;
            case "prototype":
                return PatternType.PROTOTYPE;
            case "singleton":
                return PatternType.SINGLETON;
            case "(object)adapter":
                return PatternType.OBJECT_ADAPTER;
            case "command":
                return PatternType.COMMAND;
            case "composite":
                return PatternType.COMPOSITE;
            case "decorator":
                return PatternType.DECORATOR;
            case "observer":
                return PatternType.OBSERVER;
            case "state":
                return PatternType.STATE;
            case "strategy":
                return PatternType.STRATEGY;
            case "bridge":
                return PatternType.BRIDGE;
            case "template method":
                return PatternType.TEMPLATE_METHOD;
            case "visitor":
                return PatternType.VISITOR;
            case "proxy":
                return PatternType.PROXY;
            case "proxy2":
                return PatternType.PROXY2;
            case "chain of responsibility":
                return PatternType.CHAIN_OF_RESPONSIBILITY;
            default:
                System.out.println("Was not able to match pattern with an enum. Exiting because this should not happen");
                System.exit(0);

        }
        //should never get here because we exit on the default case
        return null;
    }

    public PatternType getPatternTypeFromIndex(int index){
        switch(index){
            case 0:
                return PatternType.FACTORY_METHOD;
            case 1:
                return PatternType.PROTOTYPE;
            case 2:
                return PatternType.SINGLETON;
            case 3:
                return PatternType.OBJECT_ADAPTER;
            case 4:
                return PatternType.COMMAND;
            case 5:
                return PatternType.COMPOSITE;
            case 6:
                return PatternType.DECORATOR;
            case 7:
                return PatternType.OBSERVER;
            case 8:
                return PatternType.STATE;
            case 9:
                return PatternType.STRATEGY;
            case 10:
                return PatternType.BRIDGE;
            case 11:
                return PatternType.TEMPLATE_METHOD;
            case 12:
                return PatternType.VISITOR;
            case 13:
                return PatternType.PROXY;
            case 14:
                return PatternType.PROXY2;
            case 15:
                return PatternType.CHAIN_OF_RESPONSIBILITY;
            default:
                System.out.println("Was not able to match index with an enum. Exiting because this should not happen");
                System.exit(0);

        }
        //should never get here because we exit on the default case
        return null;
    }
}
