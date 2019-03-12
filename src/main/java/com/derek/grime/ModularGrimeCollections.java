package com.derek.grime;

import com.derek.uml.Relationship;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModularGrimeCollections<T> {

    //T will be a List<Relationship> for modular grime, and a List<CallTreeNode> for Order grime
    //above is a tod o comment. It would be great to combine the grime collections for order and modular, but
    //I don't have time to figure out how to refactor to allow a 'equals', given a generic T.
    private List<Relationship> grimeInstancesInThisVersion;
    private List<Relationship> additionsFromLastVersion;
    private List<Relationship> removalsFromLastVersion;

    public ModularGrimeCollections(List<Relationship> grimeInstancesInPreviousVersion, List<Relationship> grimeInstancesInThisVersion){
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
        System.out.println("Order Grime Collection: ");
        System.out.println("Cardinality: " + getCardinality());
        System.out.println("Number grime additions: " + this.getAdditionsFromLastVersion().size());
        System.out.println("Number grime removals: " + this.getRemovalsFromLastVersion().size());
        System.out.println("");
    }

    public String getTabDelimSummary(){
        String delim = "\t";
        StringBuilder toRet = new StringBuilder();
        toRet.append(this.getCardinality() + delim);
        toRet.append(this.getAdditionsFromLastVersion().size() + delim);
        toRet.append(this.getRemovalsFromLastVersion().size() + delim);
        return toRet.toString();
    }

}
