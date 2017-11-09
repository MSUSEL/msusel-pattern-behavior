package com.derek.model;

import com.derek.model.patterns.PatternInstance;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PatternInstanceEvolution{

    private SoftwareVersion startingPoint;
    private SoftwareVersion endingPoint;

    private PatternType patternType;
    //index in the list points at pattern software version
    private List<Pair<SoftwareVersion, PatternInstance>> patternLifetime;

    public PatternInstanceEvolution(SoftwareVersion startingPoint, SoftwareVersion endingPoint, PatternType patternType, List<Pair<SoftwareVersion, PatternInstance>> patternLifetime) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.patternType = patternType;
        this.patternLifetime = patternLifetime;
    }

    public PatternInstanceEvolution(List<Pair<SoftwareVersion, PatternInstance>> patternLifetime){
        this.patternLifetime = patternLifetime;
    }

    public PatternInstanceEvolution(SoftwareVersion startingPoint, PatternType patternType, PatternInstance patternInstance){
        this.startingPoint = startingPoint;

        this.patternType = patternType;
        patternLifetime = new ArrayList<>();
        for (int i = 0; i < startingPoint.getVersionNum(); i++){
            //this signifies that a pattern did not exist at a particular time.
            //
            patternLifetime.add(new Pair<SoftwareVersion, PatternInstance>(new SoftwareVersion(i), null));
        }
        patternLifetime.add(new Pair<SoftwareVersion, PatternInstance>(startingPoint, patternInstance));
    }


    public void addPatternInstanceToEvolution(PatternInstance pi, SoftwareVersion v){
        //add pattern instance to a particular version number.
        patternLifetime.add(new Pair<SoftwareVersion, PatternInstance>(v, pi));
    }

    public PatternInstance getFirstPatternInstance(){
        return patternLifetime.get(startingPoint.getVersionNum()).getValue();
    }

    public SoftwareVersion getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(SoftwareVersion startingPoint) {
        this.startingPoint = startingPoint;
    }

    public SoftwareVersion getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(SoftwareVersion endingPoint) {
        this.endingPoint = endingPoint;
    }

    public PatternType getPatternType() {
        return patternType;
    }

    public void setPatternType(PatternType patternType) {
        this.patternType = patternType;
    }

    public List<Pair<SoftwareVersion, PatternInstance>> getPatternLifetime() {
        return patternLifetime;
    }

    public void setPatternLifetime(List<Pair<SoftwareVersion, PatternInstance>> patternLifetime) {
        this.patternLifetime = patternLifetime;
    }

    public String getCSVVersions(){
        String toRet = "";
        for (Pair<SoftwareVersion, PatternInstance> pairs : patternLifetime){
            if (pairs.getValue() != null){
                toRet += pairs.getKey().getVersionNum() + ", ";
            }
        }
        return toRet;
    }
}
