package com.derek;

import com.derek.rbml.IPS;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private SPS sps;
    private List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings;
    private IPS ips;

    //count of behavioral repetition grime
    private int repetitionGrimeCount = 0;

    //count of improper order of sequences grime
    private int improperOrderGrimeCount = 0;

    //total behavioral grime.
    private int totalBehavioralGrimeCount = 0;

    //structural class grime counts
    private int dipgGrimeCount = 0;
    private int disgGrimeCount = 0;
    private int depgGrimeCount = 0;
    private int desgGrimeCount = 0;
    private int iipgGrimeCount = 0;
    private int iisgGrimeCount = 0;
    private int iepgGrimeCount = 0;
    private int iesgGrimeCount = 0;

    //structural modular grime counts
    private int piGrimeCount = 0;
    private int peaGrimeCount = 0;
    private int peeGrimeCount = 0;
    private int tiGrimeCount = 0;
    private int teaGrimeCount = 0;
    private int teeGrimeCount = 0;

    public GrimeSuite(PatternMapper patternMapper, List<RBMLMapping> rbmlStructuralMappings, SPS sps, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, IPS ips) {
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.sps = sps;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        this.ips = ips;
    }

    private void calculateModularGrime(){
        calculatePIGrime();
    }

    private void calculatePIGrime(){
        //just use structure for this.


    }


}
