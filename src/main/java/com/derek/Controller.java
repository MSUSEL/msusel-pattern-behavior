package com.derek;

import com.derek.patterns.*;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import javafx.util.Pair;
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
import java.util.*;

public class Controller {

    private Table<SoftwareVersion, PatternType, List<PatternInstance>> patternSummaryTable;

    private Map<PatternType, List<PatternInstanceEvolution>> patternEvolutions;

    public Controller(){

        patternEvolutions = new TreeMap<>();
        patternSummaryTable = TreeBasedTable.create();

        parseDataIntoPatternDataStructure("pattern_detections/");

        //TODO - rename Pattern instances within data structure to represent individual patterns, matching on roles.

        //buildPatternEvolutions();

        //printPatternDataStructure();
        //showAllFactoryMethodEvolutions();

        printPatternTable();

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

                SoftwareVersion version = new SoftwareVersion(getVersionNumberFromFileName(f.getName()));


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

                            patternInstances.add(buildPatternInstance(listOfRoles, patternType));
                        }

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

    private PatternInstance buildPatternInstance(List<Pair<String, String>> listOfRoles, PatternType patternType){
        switch(patternType){
            //patterns with one major role
            case FACTORY_METHOD:
            case CHAIN_OF_RESPONSIBILITY:
            case TEMPLATE_METHOD:
            case SINGLETON:
                return new PatternInstance(listOfRoles, patternType);

            //patterns with two major roles
            case OBJECT_ADAPTER:
            case DECORATOR:
            case STATE:
            case STRATEGY:
            case BRIDGE:
            case PROXY:
            case PROXY2:
                return new PatternInstanceTwoRoles(listOfRoles, patternType);

            //patterns that have not been detected yet.
            case PROTOTYPE:return null;//TODO;
            case COMMAND:return null;//TODO
            case COMPOSITE:return null;//TODO
            case OBSERVER:return null;//TODO
            case VISITOR:return null; //TODO
            default:
                System.out.println("Did not match a pattern type with a built-in class of that type. Exiting and fix ");
                System.exit(0);
        }
        return null;
    }


    /*
    expects the filename as a string for input. forms are: "pattern_detector{\d}+.xml"
     */
    private int getVersionNumberFromFileName(String s){
        //remove .xml
        return Integer.parseInt(s.substring(16, s.lastIndexOf('.')));
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
}
