package com.derek;

import com.derek.model.SoftwareVersion;
import com.derek.uml.Relationship;
import com.google.common.collect.Table;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GrimeFinder {

    private Table<SoftwareVersion, String, GrimeSuite> grimeTable;

//    private List<> modularGrimeInstances;
//    private List<> classGrimeInstances;
//    private List<> improperOrderGrimeInstances;
//    private List<> repetitionGrimeInstances;

    public GrimeFinder(Table<SoftwareVersion, String, GrimeSuite> grimeTable) {
        this.grimeTable = grimeTable;
    }

    public void findModularGrime(){
        List<Pair<SoftwareVersion, ModularGrimeCollections>> peaGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> peeGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> teeGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> teaGrime = new ArrayList<>();

        for (String patternID : grimeTable.columnKeySet()) {
            List<Relationship> previousPeaGrime = new ArrayList<>();
            List<Relationship> previousPeeGrime = new ArrayList<>();
            List<Relationship> previousTeeGrime = new ArrayList<>();
            List<Relationship> previousTeaGrime = new ArrayList<>();
            for (SoftwareVersion version : grimeTable.rowKeySet()) {
                GrimeSuite grimeSuite = grimeTable.get(version, patternID);
                System.out.println("Tee Grime counts, version: " + version + " and cardinality: " + grimeSuite.getTeeGrimeInstances().size());
                peaGrime.add(new ImmutablePair<>(version, fillModularGrimeCollectionModel(previousPeaGrime, grimeSuite.getPeaGrimeInstances(), version)));
                peeGrime.add(new ImmutablePair<>(version, fillModularGrimeCollectionModel(previousPeeGrime, grimeSuite.getPeeGrimeInstances(), version)));
                teeGrime.add(new ImmutablePair<>(version, fillModularGrimeCollectionModel(previousTeeGrime, grimeSuite.getTeeGrimeInstances(), version)));
                teaGrime.add(new ImmutablePair<>(version, fillModularGrimeCollectionModel(previousTeaGrime, grimeSuite.getTeaGrimeInstances(), version)));
            }
        }

        System.out.println("TEE GRIME in pair number: " + teeGrime.size());
        for (Pair<SoftwareVersion, ModularGrimeCollections> pair : teeGrime){
            pair.getRight().printSummary();
        }
    }

    private ModularGrimeCollections fillModularGrimeCollectionModel(List<Relationship> previousGrimeCategory, List<Relationship> currentGrimeCategory, SoftwareVersion version){
        ModularGrimeCollections collections;
        //bug here I think.
        if (previousGrimeCategory.isEmpty()){
            //first time through
            previousGrimeCategory.addAll(currentGrimeCategory);
        }
        collections = new ModularGrimeCollections(version, previousGrimeCategory, currentGrimeCategory);
        return collections;
    }


}
