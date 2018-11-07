package com.derek;

import com.derek.model.SoftwareVersion;
import com.derek.uml.Relationship;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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
        List<Pair<SoftwareVersion, ModularGrimeCollections>> piGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> teeGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> teaGrime = new ArrayList<>();
        List<Pair<SoftwareVersion, ModularGrimeCollections>> tiGrime = new ArrayList<>();

        for (String patternID : grimeTable.columnKeySet()) {
            Iterator<SoftwareVersion> iter = grimeTable.rowKeySet().iterator();
            List<SoftwareVersion> listIter = Lists.newArrayList(iter);
            for (int i = 0; i < listIter.size(); i++){
                GrimeSuite previousGrimeSuite = null;
                if (i != 0){
                    previousGrimeSuite = grimeTable.get(listIter.get(i-1), patternID);
                }
                SoftwareVersion version = listIter.get(i);
                if (previousGrimeSuite != null) {
                    GrimeSuite currentGrimeSuite = grimeTable.get(version, patternID);
                    System.out.println("Tee Grime counts, version: " + version + " and cardinality: " + currentGrimeSuite.getTeeGrimeInstances().size());
                    peaGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getPeaGrimeInstances(), currentGrimeSuite.getPeaGrimeInstances(), version)));
                    peeGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getPeeGrimeInstances(), currentGrimeSuite.getPeeGrimeInstances(), version)));
                    piGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getPiGrimeInstances(), currentGrimeSuite.getPiGrimeInstances(), version)));
                    teaGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getTeaGrimeInstances(), currentGrimeSuite.getTeaGrimeInstances(), version)));
                    teeGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getTeeGrimeInstances(), currentGrimeSuite.getTeeGrimeInstances(), version)));
                    tiGrime.add(new ImmutablePair<>(version, new ModularGrimeCollections(previousGrimeSuite.getTiGrimeInstances(), currentGrimeSuite.getTiGrimeInstances(), version)));
                }
            }
        }

        System.out.println("TEE GRIME in pair number: " + teeGrime.size());
        for (Pair<SoftwareVersion, ModularGrimeCollections> pair : teeGrime){
            pair.getRight().printSummary();
        }
    }


}
