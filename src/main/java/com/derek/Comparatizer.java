package com.derek;

import com.derek.model.Model;
import com.derek.model.PatternType;
import com.derek.model.RBMLSpec;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
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
        PatternType patternType = PatternType.FACTORY_METHOD;
        List<PatternInstance> patternInstances = model.getPatternSummaryTable().get(version, patternType);
        PatternInstance pi = patternInstances.get(0);
        compareFactory(pi);
    }


    public void compareFactory(PatternInstance pi){
        //factory has "Creator" as the major role name.
        UMLClassifier creatorMajorRole = getClassifierFromString(pi.getValueOfMajorRole(pi));
        List<Pair<UMLClassifier, UMLOperation>> factoryMinorRoles = new ArrayList<>();
        for (Pair<String, String> p : pi.getMinorRoles()){
            String[] minorValueSplitter = p.getValue().split("\\:\\:");
            String minorClassName = minorValueSplitter[0];
            UMLClassifier minorClass = getClassifierFromString(minorClassName);
            String minorClassOperationNameParams = minorValueSplitter[1].split("\\:")[0];
            String minorClassOperationName = getNameFromNameParams(minorClassOperationNameParams);
            List<String> minorClassOperationParams = getParamsFromNameParams(minorClassOperationNameParams);

            String minorClassReturnType = minorValueSplitter[1].split("\\:")[1];
            //there is a really strange case here... so the minorClassReturnType will be a string pointing at a type in a project
            //e.g.: "org.openqa.selenium.Http". Now, I think I can just strip the package header off and match based on return type.
            //The only case where this wouldn't work is if 2 or more operations within the same class return a type with the same name
            //where the name points to different class files within the projec.t. But I also think that cannot happen because of erasure
            //rules in java. So I am just going to strip the leading package directions.
            //Same is true for params.
            if (minorClassReturnType.contains(".")){
                String[] stripper = minorClassReturnType.split("\\.");
                minorClassReturnType = stripper[stripper.length-1];
            }
            List<String> minorClassOperationParamsStripped = new ArrayList<>();
            for (String param : minorClassOperationParams){
                if (param.contains(".")) {
                    String[] stripper = param.split("\\.");
                    minorClassOperationParamsStripped.add(stripper[stripper.length-1]);
                }else{
                    //primitive type, e.g.
                    minorClassOperationParamsStripped.add(param);
                }
            }

            UMLOperation minorClassOperation = getOperationFromString(minorClass, minorClassOperationName, minorClassReturnType, minorClassOperationParamsStripped);
            Pair<UMLClassifier, UMLOperation> minorRoleRealization = new Pair<>(minorClass, minorClassOperation);
            factoryMinorRoles.add(minorRoleRealization);
        }
        System.out.println("matched pattern and classes!");

    }

    //input will look like this: execute(org.openqa.selenium.remote.http.HttpRequest, boolean)
    private String getNameFromNameParams(String minorClassOperationNameParams){
        return minorClassOperationNameParams.split("\\(")[0];
    }

    //input will look like this: execute(org.openqa.selenium.remote.http.HttpRequest, boolean)
    private List<String> getParamsFromNameParams(String minorClassOperationNameParams){
        List<String> paramsList = new ArrayList<>();
        String paramsBlock = minorClassOperationNameParams.split("\\(")[1];
        //minus 2 becuase last char is a ')'
        if (paramsBlock.length() == 1){
            return paramsList;
        }
        String[] params = paramsBlock.substring(0, paramsBlock.length()-2).split("\\,");
        for (String s : params){
            paramsList.add(s);
        }
        return paramsList;
    }

    private UMLOperation getOperationFromString(UMLClassifier umlClassifier, String operationName, String returnType, List<String> params){
        for (UMLOperation op : umlClassifier.getOperations()){
            if (op.getName().equals(operationName)){
                //will work for most things but we also need ot check params and return type (basically check method signature)
                if (op.getStringReturnDataType().equals(returnType)){
                    boolean foundDifference = false;
                    if (params.size() != op.getParameters().size()){
                        //same method name, but different number of params.. so different method signature. break and check other ops.
                        break;
                    }
                    for (int i = 0; i < params.size(); i++){
                        //param order needs to be preserved too
                        if (!params.get(i).equals(op.getParameters().get(i).getName())){
                            foundDifference = true;
                            break;
                        }
                    }
                    //same method signature!
                    if (foundDifference == false){
                        return op;
                    }
                }
            }
        }
        System.out.println("did not find a match between classifier: " + umlClassifier.getName() + " and method name: " + operationName);
        System.exit(0);
        return null;
    }

    private UMLClassifier getClassifierFromString(String majorRoleValue){
        return umlClassDiagram.getPackageTree().getClassifier(convertStringToPackagedString(majorRoleValue), 0, umlClassDiagram.getPackageTree().getRoot());
    }

    private List<String> convertStringToPackagedString(String value){
        List<String> toRet = new ArrayList<>();
        String[] splitter = value.split("\\.");
        for (String s : splitter){
            toRet.add(s);
        }
        return toRet;
    }
}
