package com.derek;

import com.derek.grime.GrimeSuite;
import com.derek.rbml.IPS;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;

import java.util.List;

@Getter
/***
 * A class to hold and maintain data structures pertaining to conformace.
 */
public class ConformanceResults {

    private PatternMapper patternMapper;
    private SPS sps;
    private List<RBMLMapping> rbmlStructureMappings;
    private IPS ips;
    private List<RBMLMapping> rbmlBehaviorMappings;
    private GrimeSuite grimeSuite;

    public ConformanceResults(PatternMapper patternMapper, SPS sps, List<RBMLMapping> rbmlStructureMappings, IPS ips, List<RBMLMapping> rbmlBehaviorMappings, GrimeSuite grimeSuite) {
        this.patternMapper = patternMapper;
        this.sps = sps;
        this.rbmlStructureMappings = rbmlStructureMappings;
        this.ips = ips;
        this.rbmlBehaviorMappings = rbmlBehaviorMappings;
        this.grimeSuite = grimeSuite;
    }
}
