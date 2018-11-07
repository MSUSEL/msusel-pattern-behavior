package com.derek;

import com.derek.model.SoftwareVersion;
import com.derek.uml.Relationship;
import com.derek.uml.UMLClassifier;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.Getter;

import java.util.*;

@Getter
public class GrimeFinder {

    private Table<SoftwareVersion, String, GrimeSuite> grimeTable;

    private Table<SoftwareVersion, String, ModularGrimeCollections> peaGrime;
    private Table<SoftwareVersion, String, ModularGrimeCollections> peeGrime;
    private Table<SoftwareVersion, String, ModularGrimeCollections> piGrime;
    private Table<SoftwareVersion, String, ModularGrimeCollections> teeGrime;
    private Table<SoftwareVersion, String, ModularGrimeCollections> teaGrime;
    private Table<SoftwareVersion, String, ModularGrimeCollections> tiGrime;

    private Table<SoftwareVersion, String, ClassGrime> dipGrime;
    private Table<SoftwareVersion, String, ClassGrime> disGrime;
    private Table<SoftwareVersion, String, ClassGrime> depGrime;
    private Table<SoftwareVersion, String, ClassGrime> desGrime;
    private Table<SoftwareVersion, String, ClassGrime> iipGrime;
    private Table<SoftwareVersion, String, ClassGrime> iisGrime;
    private Table<SoftwareVersion, String, ClassGrime> iepGrime;
    private Table<SoftwareVersion, String, ClassGrime> iesGrime;

    public GrimeFinder(Table<SoftwareVersion, String, GrimeSuite> grimeTable) {
        this.grimeTable = grimeTable;
    }

    public void findModularGrime(){
        peaGrime =  HashBasedTable.create();
        peeGrime = HashBasedTable.create();
        piGrime = HashBasedTable.create();
        teeGrime = HashBasedTable.create();
        teaGrime = HashBasedTable.create();
        tiGrime = HashBasedTable.create();

        for (String patternID : grimeTable.columnKeySet()) {
            Iterator<SoftwareVersion> iter = grimeTable.rowKeySet().iterator();
            List<SoftwareVersion> listIter = Lists.newArrayList(iter);
            for (int i = 0; i < listIter.size(); i++){
                SoftwareVersion version = listIter.get(i);
                GrimeSuite currentGrimeSuite = grimeTable.get(version, patternID);
                if (i != 0){
                    GrimeSuite previousGrimeSuite = grimeTable.get(listIter.get(i-1), patternID);
                    peaGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getPeaGrimeInstances(), currentGrimeSuite.getPeaGrimeInstances()));
                    peeGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getPeeGrimeInstances(), currentGrimeSuite.getPeeGrimeInstances()));
                    piGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getPiGrimeInstances(), currentGrimeSuite.getPiGrimeInstances()));
                    teaGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getTeaGrimeInstances(), currentGrimeSuite.getTeaGrimeInstances()));
                    teeGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getTeeGrimeInstances(), currentGrimeSuite.getTeeGrimeInstances()));
                    tiGrime.put(version, patternID, new ModularGrimeCollections(previousGrimeSuite.getTiGrimeInstances(), currentGrimeSuite.getTiGrimeInstances()));
                }else{
                    peaGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getPeaGrimeInstances()));
                    peeGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getPeeGrimeInstances()));
                    piGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getPiGrimeInstances()));
                    teaGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getTeaGrimeInstances()));
                    teeGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getTeeGrimeInstances()));
                    tiGrime.put(version, patternID, new ModularGrimeCollections(new ArrayList<>(), currentGrimeSuite.getTiGrimeInstances()));
                }
            }
        }
    }

    /***
     * to find class grime I need to find instances where RCI or TCC drops between versions, then look at the method strength/scope..
     */
    public void findClassGrime(){
        dipGrime = HashBasedTable.create();
        disGrime = HashBasedTable.create();
        depGrime = HashBasedTable.create();
        desGrime = HashBasedTable.create();
        iipGrime = HashBasedTable.create();
        iisGrime = HashBasedTable.create();
        iepGrime = HashBasedTable.create();
        iesGrime = HashBasedTable.create();
        for (String patternID : grimeTable.columnKeySet()) {
            Iterator<SoftwareVersion> iter = grimeTable.rowKeySet().iterator();
            List<SoftwareVersion> listIter = Lists.newArrayList(iter);
            for (int i = 0; i < listIter.size(); i++) {
                SoftwareVersion version = listIter.get(i);
                GrimeSuite currentGrimeSuite = grimeTable.get(version, patternID);
                if (i != 0){
                    GrimeSuite previousGrimeSuite = grimeTable.get(listIter.get(i-1), patternID);
                    for (UMLClassifier previousClass : previousGrimeSuite.getClassGrimeList().keySet()){
                        for (UMLClassifier currentClass : currentGrimeSuite.getClassGrimeList().keySet()){
                            if (previousClass.getName().equals(currentClass.getName())){
                                //same class
                                ClassGrime previousGrime = previousGrimeSuite.getClassGrimeList().get(previousClass);
                                ClassGrime currentGrime = currentGrimeSuite.getClassGrimeList().get(currentClass);
                                if (currentGrime.getRCI() < previousGrime.getRCI()){
                                    //rci decrease
                                    if (currentGrime.getInternalMethods().size() > 0){
                                        //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                                        if (currentGrime.getDirectAccess().size() > 0){
                                            //see above.
                                            //if all this holds true, I am pretty sure this is an instance of disGrime.
                                            disGrime.put(version, patternID, currentGrime);
                                        }
                                        if (currentGrime.getIndirectAccess().size() > 0){
                                            iisGrime.put(version, patternID, currentGrime);
                                        }
                                    }
                                    if (currentGrime.getExternalMethods().size() > 0){
                                        if (currentGrime.getDirectAccess().size() > 0){
                                            //see above.
                                            desGrime.put(version, patternID, currentGrime);
                                        }
                                        if (currentGrime.getIndirectAccess().size() > 0){
                                            iesGrime.put(version, patternID, currentGrime);
                                        }
                                    }
                                }
                                if (currentGrime.getTCC() < previousGrime.getTCC()){
                                    //tcc decrease
                                    if (currentGrime.getInternalMethods().size() > 0){
                                        //we have internal methods existing... I do not think I need to assert this is different from previous methods.
                                        if (currentGrime.getDirectAccess().size() > 0){
                                            //see above.
                                            //if all this holds true, I am pretty sure this is an instance of disGrime.
                                            dipGrime.put(version, patternID, currentGrime);
                                        }
                                        if (currentGrime.getIndirectAccess().size() > 0){
                                            iipGrime.put(version, patternID, currentGrime);
                                        }
                                    }
                                    if (currentGrime.getExternalMethods().size() > 0){
                                        if (currentGrime.getDirectAccess().size() > 0){
                                            //see above.
                                            depGrime.put(version, patternID, currentGrime);
                                        }
                                        if (currentGrime.getIndirectAccess().size() > 0){
                                            iepGrime.put(version, patternID, currentGrime);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


}
