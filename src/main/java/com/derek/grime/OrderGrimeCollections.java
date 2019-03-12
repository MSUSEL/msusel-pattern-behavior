package com.derek.grime;

import com.derek.uml.CallTreeNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderGrimeCollections {


    //T will be a List<Relationship> for modular grime, and a List<CallTreeNode> for Order grime
    //above is a tod o comment. It would be great to combine the grime collections for order and modular, but
    //I don't have time to figure out how to refactor to allow a 'equals', given a generic T.
    private List<CallTreeNode> grimeInstancesInThisVersion;
    private List<CallTreeNode> additionsFromLastVersion;
    private List<CallTreeNode> removalsFromLastVersion;

    public OrderGrimeCollections(List<CallTreeNode> grimeInstancesInPreviousVersion, List<CallTreeNode> grimeInstancesInThisVersion){
        this.grimeInstancesInThisVersion = grimeInstancesInThisVersion;
        differentiate(grimeInstancesInPreviousVersion, grimeInstancesInThisVersion);
    }

    private int getCardinality(){
        return grimeInstancesInThisVersion.size();
    }

    private void differentiate(List<CallTreeNode> previous, List<CallTreeNode> current) {
        additionsFromLastVersion = new ArrayList<>();
        removalsFromLastVersion = new ArrayList<>();
        for (CallTreeNode previousGrime : previous) {
            boolean hasFound = false;
            for (CallTreeNode currentGrime : current) {
                if (previousGrime.getName().equals(currentGrime.getName())) {
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
        for (CallTreeNode currentGrime : current){
            boolean hasFound = false;
            for (CallTreeNode previousGrime : previous){
                if (previousGrime.getName().equals(currentGrime.getName())){
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
