package com.derek.grime;

import com.derek.model.SoftwareVersion;
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

    private Table<SoftwareVersion, String, ClassGrimeCollections> dipGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> disGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> depGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> desGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> iipGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> iisGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> iepGrime;
    private Table<SoftwareVersion, String, ClassGrimeCollections> iesGrime;

    private Table<SoftwareVersion, String, BehavioralGrimeCollections> improperOrderGrime;
    private Table<SoftwareVersion, String, BehavioralGrimeCollections> repetitionGrime;



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
                GrimeSuite previousGrimeSuite = currentGrimeSuite;
                if (i != 0) {
                    previousGrimeSuite = grimeTable.get(listIter.get(i - 1), patternID);
                }
                dipGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.DIPGRIME));
                disGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.DISGRIME));
                depGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.DEPGRIME));
                desGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.DESGRIME));
                iipGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.IIPGRIME));
                iisGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.IISGRIME));
                iepGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.IEPGRIME));
                iesGrime.put(version, patternID, new ClassGrimeCollections(previousGrimeSuite, currentGrimeSuite, ClassGrimeType.IESGRIME));
            }
        }
    }

    //TODO - I might need to build a separate class/data structure to track behavioral grime types. Especially tracking behavioral grime across versions.
    public void findBehavioralGrime(){
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


}
