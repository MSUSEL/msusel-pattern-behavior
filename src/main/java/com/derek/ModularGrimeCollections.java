package com.derek;

import com.derek.model.SoftwareVersion;
import com.derek.uml.Relationship;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModularGrimeCollections {

    private SoftwareVersion version;
    private List<Relationship> grimeInstancesInThisVersion;
    private List<Relationship> additionsFromLastVersion;
    private List<Relationship> removalsFromLastVersion;

    public ModularGrimeCollections(List<Relationship> grimeInstancesInPreviousVersion, List<Relationship> grimeInstancesInThisVersion, SoftwareVersion version){
        this.version = version;
        this.grimeInstancesInThisVersion = grimeInstancesInThisVersion;
        differentiate(grimeInstancesInPreviousVersion, grimeInstancesInThisVersion);
    }

    private int getCardinality(){
        return grimeInstancesInThisVersion.size();
    }

    private void differentiate(List<Relationship> previous, List<Relationship> current) {
        additionsFromLastVersion = new ArrayList<>();
        removalsFromLastVersion = new ArrayList<>();
        for (Relationship previousGrime : previous) {
            boolean hasFound = false;
            for (Relationship currentGrime : current) {
                if (previousGrime.equalsFromClassifierName(currentGrime)) {
                    hasFound = true;
                }
            }
            if (hasFound) {
                //nothing changed, previous exists in current.
                //not doing anything now, but might in future.
            } else {
                //exists in previous but not current. Grime was removed.
                removalsFromLastVersion.add(previousGrime);
            }
        }
        for (Relationship currentGrime : current){
            boolean hasFound = false;
            for (Relationship previousGrime : previous){
                if (currentGrime.equalsFromClassifierName(previousGrime)){
                    hasFound = true;
                }
            }
            if (!hasFound){
                //never found grime, meaning this Relationship is a new one
                additionsFromLastVersion.add(currentGrime);
            }
        }
    }

    public void printSummary(){
        System.out.println("Modular Grime Collection: ");
        System.out.println("Version: " + version.getVersionNum());
        System.out.println("Cardinality: " + getCardinality());
        System.out.println("Number grime additions: " + this.getAdditionsFromLastVersion().size());
        System.out.println("Number grime removals: " + this.getRemovalsFromLastVersion().size());
        System.out.println("");
    }

}
