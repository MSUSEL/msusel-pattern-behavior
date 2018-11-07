package com.derek;

import com.derek.model.SoftwareVersion;
import com.derek.uml.Relationship;
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

}
