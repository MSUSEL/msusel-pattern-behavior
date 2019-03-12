package com.derek.grime;

import com.derek.uml.UMLClassifier;

import java.util.ArrayList;
import java.util.List;

public class ClassGrimeCollections {

    private ClassGrimeType classGrimeType;
    private List<UMLClassifier> grimeInstancesInThisVersion;
    private List<UMLClassifier> additionsFromLastVersion;
    private List<UMLClassifier> removalsFromLastVersion;

    private GrimeSuite previousGrimeSuite;
    private GrimeSuite currentGrimeSuite;

    public ClassGrimeCollections(GrimeSuite previousGrimeSuite, GrimeSuite curentGrimeSuite, ClassGrimeType classGrimeType) {
        this.previousGrimeSuite = previousGrimeSuite;
        this.currentGrimeSuite = curentGrimeSuite;
        this.classGrimeType = classGrimeType;
        findGrime();
    }

    private void findGrime() {
        grimeInstancesInThisVersion = new ArrayList<>();
        additionsFromLastVersion = new ArrayList<>();
        removalsFromLastVersion = new ArrayList<>();
        switch (classGrimeType) {
            case DIPGRIME:
                findDIPGrime();
                break;
            case DISGRIME:
                findDISGrime();
                break;
            case DEPGRIME:
                findDEPGrime();
                break;
            case DESGRIME:
                findDESGrime();
                break;
            case IIPGRIME:
                findIIPGrime();
                break;
            case IISGRIME:
                findIISGrime();
                break;
            case IEPGRIME:
                findIEPGrime();
                break;
            case IESGRIME:
                findIESGrime();
                break;
        }
    }

    private void findDIPGrime() {
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getTCC() < previousGrimeMeasurements.getTCC()) {
                        if (currentGrimeMeasurements.getInternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getDirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findDISGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getRCI() < previousGrimeMeasurements.getRCI()) {
                        if (currentGrimeMeasurements.getInternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getDirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findDEPGrime() {
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getTCC() < previousGrimeMeasurements.getTCC()) {
                        if (currentGrimeMeasurements.getExternalMethods().size() > 0) {
                            if (currentGrimeMeasurements.getDirectAccess().size() > 0) {
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findDESGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getRCI() < previousGrimeMeasurements.getRCI()) {
                        if (currentGrimeMeasurements.getExternalMethods().size() > 0) {
                            if (currentGrimeMeasurements.getDirectAccess().size() > 0) {
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findIIPGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getTCC() < previousGrimeMeasurements.getTCC()) {
                        if (currentGrimeMeasurements.getInternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getIndirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findIISGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getRCI() < previousGrimeMeasurements.getRCI()) {
                        if (currentGrimeMeasurements.getInternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getIndirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }
    private void findIEPGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getTCC() < previousGrimeMeasurements.getTCC()) {
                        if (currentGrimeMeasurements.getExternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getIndirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }
    private void findIESGrime(){
        for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeMeasurementList().keySet()) {
            for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeMeasurementList().keySet()) {
                if (previousClass.getName().equals(currentClass.getName())) {
                    //same class
                    ClassGrimeMeasurements previousGrimeMeasurements = previousGrimeSuite.getClassGrimeMeasurementList().get(previousClass);
                    ClassGrimeMeasurements currentGrimeMeasurements = currentGrimeSuite.getClassGrimeMeasurementList().get(currentClass);
                    //first check for tcc decrease
                    if (currentGrimeMeasurements.getRCI() < previousGrimeMeasurements.getRCI()) {
                        if (currentGrimeMeasurements.getExternalMethods().size() > 0){
                            //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                            if (currentGrimeMeasurements.getIndirectAccess().size() > 0){
                                //see above.
                                grimeInstancesInThisVersion.add(currentClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getCardinality(){
        return grimeInstancesInThisVersion.size();
    }

    private double getPercentageOfClassesWithGrime(){
        return ((double)grimeInstancesInThisVersion.size()) / currentGrimeSuite.getClassGrimeMeasurementList().keySet().size();
    }

    public String getTabDelimSummary(){
        String delim = "\t";
        StringBuilder toRet = new StringBuilder();
        toRet.append(this.getCardinality() + delim);
        toRet.append(this.getPercentageOfClassesWithGrime() + delim);
        return toRet.toString();
    }


}
