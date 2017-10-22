package com.derek;

import com.derek.patterns.PatternInstance;

import java.util.List;
import java.util.Map;

public class PatternInstanceEvolution{

    private SoftwareVersion startingPoint;
    private SoftwareVersion endingPoint;

    private PatternType patternType;
    private Map<SoftwareVersion, List<PatternInstance>> patternLifetime;

    public PatternInstanceEvolution(SoftwareVersion startingPoint, SoftwareVersion endingPoint, PatternType patternType, Map<SoftwareVersion, List<PatternInstance>> patternLifetime) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.patternType = patternType;
        this.patternLifetime = patternLifetime;
    }

    public PatternInstanceEvolution(PatternType patternType, Map<SoftwareVersion, List<PatternInstance>> patternLifetime) {
        this.patternType = patternType;
        this.patternLifetime = patternLifetime;
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

    public Map<SoftwareVersion, List<PatternInstance>> getPatternLifetime() {
        return patternLifetime;
    }

    public void setPatternLifetime(Map<SoftwareVersion, List<PatternInstance>> patternLifetime) {
        this.patternLifetime = patternLifetime;
    }

}
