package com.derek.grime;

import com.derek.*;
import com.derek.rbml.IPS;
import com.derek.rbml.InteractionRole;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@Getter
public class GrimeSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private List<RBMLMapping> rbmlBehavioralMappings;

    private BehaviorMappingUtils behaviorMappingUtils;

    private SPS sps;
    private IPS ips;


    //list of class grime instances for a single pattern (pattern mapper obj)
    private Map<UMLClassifier, ClassGrimeMeasurements> classGrimeMeasurementList;


    //structural modular grime instances (persistent grime). When I want to calculate I will just call this.size();
    private List<Relationship> piGrimeInstances;
    private List<Relationship> peaGrimeInstances;
    private List<Relationship> peeGrimeInstances;

    //structural modular grime instances (temporary grime). When I want to calculate I will just call this.size();
    private List<Relationship> tiGrimeInstances;
    private List<Relationship> teaGrimeInstances;
    private List<Relationship> teeGrimeInstances;

    private List<CallTreeNode> peaoGrimeInstances;
    private List<CallTreeNode> peeoGrimeInstances;
    private List<CallTreeNode> pioGrimeInstances;
    private List<CallTreeNode> teaoGrimeInstances;
    private List<CallTreeNode> teeoGrimeInstances;
    private List<CallTreeNode> tioGrimeInstances;

    private List<CallTreeNode> pearGrimeInstances;
    private List<CallTreeNode> peerGrimeInstances;
    private List<CallTreeNode> pirGrimeInstances;
    private List<CallTreeNode> tearGrimeInstances;
    private List<CallTreeNode> teerGrimeInstances;
    private List<CallTreeNode> tirGrimeInstances;


    public GrimeSuite(PatternMapper patternMapper, SPS sps, List<RBMLMapping> rbmlStructuralMappings, IPS ips, List<RBMLMapping> rbmlBehavioralMappings) {
        this.patternMapper = patternMapper;
        this.sps = sps;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.ips = ips;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        classGrimeMeasurementList = new HashMap<>();
        behaviorMappingUtils = new BehaviorMappingUtils(sps, rbmlStructuralMappings, ips);
        calculateModularGrime();
        calculateClassGrime();
        calculateOrderGrime();

    }

    private void calculateModularGrime() {
        //class variable initializations
        piGrimeInstances = new ArrayList<>();
        peaGrimeInstances = new ArrayList<>();
        peeGrimeInstances = new ArrayList<>();
        tiGrimeInstances = new ArrayList<>();
        teaGrimeInstances = new ArrayList<>();
        teeGrimeInstances = new ArrayList<>();
        findModularGrime();
    }

    private void calculateOrderGrime(){
        peaoGrimeInstances = new ArrayList<>();
        peeoGrimeInstances = new ArrayList<>();
        pioGrimeInstances = new ArrayList<>();
        teaoGrimeInstances = new ArrayList<>();
        teeoGrimeInstances = new ArrayList<>();
        tioGrimeInstances = new ArrayList<>();
        findOrderGrime();
    }

    private void findModularGrime() {
        List<Relationship> validRBMLMappings = new ArrayList<>();

        for (RBMLMapping structuralMapping : rbmlStructuralMappings) {
            Relationship relationship = structuralMapping.getRelationshipArtifact();
            if (relationship != null) {
                //have a valid relationship
                validRBMLMappings.add(relationship);
            }
        }
        int currentAfferentUsages = 0;

        for (Relationship patternAssociation : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.ASSOCIATION)) {
            if (!isCapturedInRBML(validRBMLMappings, patternAssociation)) {
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getFrom())) {
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getTo())) {
                        //internal grime (pi)
                        piGrimeInstances.add(patternAssociation);
                    } else {
                        //efferent grime (pee)
                        peeGrimeInstances.add(patternAssociation);
                    }
                } else {
                    if (currentAfferentUsages < Main.clientClassAllowances){
                        //only a less than inequality; if its less than/equals then I am doing 1 more case.
                        currentAfferentUsages++;
                    }else{
                        peaGrimeInstances.add(patternAssociation);
                    }
                }
            }
        }
        int currentAfferentTemporaryUsages = 0;
        for (Relationship patternAssociation : patternMapper.getUniqueRelationshipsFromPatternClassifiers(RelationshipType.DEPENDENCY)) {
            if (!isCapturedInRBML(validRBMLMappings, patternAssociation)) {
                if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getFrom())) {
                    if (patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers().contains(patternAssociation.getTo())) {
                        //internal grime (ti)
                        tiGrimeInstances.add(patternAssociation);
                    } else {
                        //efferent grime (tee)
                        teeGrimeInstances.add(patternAssociation);
                    }
                } else {
                    if (currentAfferentTemporaryUsages < Main.clientUsageAllowances){
                        //only a less than inequality; if its less than/equals then I am doing 1 more case.
                        currentAfferentTemporaryUsages++;
                    }else{
                        teaGrimeInstances.add(patternAssociation);
                    }
                }
            }
        }
    }

    private boolean isCapturedInRBML(List<Relationship> validRBMLMappings, Relationship r) {
        boolean relationshipExistsInRBML = false;
        for (Relationship validRBMLMapping : validRBMLMappings) {
            if (r.equals(validRBMLMapping)) {
                relationshipExistsInRBML = true;
            } else if (r.getFrom().isLanguageType() || r.getTo().isLanguageType()) {
                //ignore langauge types... this is a big assumption but if we don't ignore language types then the code would do nothing... as in it would be purely functional,
                //in a paradigm sense.
                relationshipExistsInRBML = true;
            }
        }
        return relationshipExistsInRBML;
    }

    //for a pattern realization every class is subject to class grime.. I need to maintain a mapping from class to grime categoreis (strength, scope, and context)
    private void calculateClassGrime() {
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassesOnlyUMLClassifiers()) {
            ClassGrimeMeasurements grime = new ClassGrimeMeasurements(umlClassifier, rbmlStructuralMappings);
            grime.calculateClassGrimeMeasurements();
            classGrimeMeasurementList.put(umlClassifier, grime);
        }
    }

    private void findOrderGrime(){
        int currentAfferentUsages = 0;
        for (UMLClassifier afferentClassifier : patternMapper.getUniqueAfferentClassifiers()) {
            currentAfferentUsages++;
            if (currentAfferentUsages > Main.clientClassAllowances){
                //candiate for persistent grime.
                for (UMLOperation operation : afferentClassifier.getOperationsIncludingConstructorsIfExists()) {
                    List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                    List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

                    for (UMLAttribute attribute : operation.getVariableTable().keySet()) {
                        //see if class owns (persistent) or operation owns (temporary)
                        if (operation.getOwningClassifier().getAttributes().contains(attribute)) {
                            //class owns, so its persistent
                            //now I need to check order
                            List<CallTreeNode> orderGrime = evaluateOrder(roleMap);
                            for (CallTreeNode callTreeNode : orderGrime){
                                if (!peaoGrimeInstances.contains(callTreeNode)){
                                    peaoGrimeInstances.add(callTreeNode);
                                }
                            }
                        } else {
                            //operation owns (temporary)
                            List<CallTreeNode> orderGrime = evaluateOrder(roleMap);
                            for (CallTreeNode callTreeNode : orderGrime){
                                if (!teaoGrimeInstances.contains(callTreeNode)){
                                    teaoGrimeInstances.add(callTreeNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        int currentEfferentUsages = 0;
        for (UMLClassifier efferentClassifier : patternMapper.getUniqueEfferentClassifiers()) {
            currentEfferentUsages++;
            if (currentEfferentUsages > Main.clientClassAllowances){
                //candiate for persistent grime.
                for (UMLOperation operation : efferentClassifier.getOperationsIncludingConstructorsIfExists()) {
                    List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                    List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

                    for (UMLAttribute attribute : operation.getVariableTable().keySet()) {
                        //see if class owns (persistent) or operation owns (temporary)
                        if (operation.getOwningClassifier().getAttributes().contains(attribute)) {
                            //class owns, so its persistent
                            //now I need to check order
                            List<CallTreeNode> orderGrime = evaluateOrder(roleMap);
                            for (CallTreeNode callTreeNode : orderGrime){
                                if (!peeoGrimeInstances.contains(callTreeNode)){
                                    peeoGrimeInstances.add(callTreeNode);
                                }
                            }
                        } else {
                            //operation owns (temporary)
                            List<CallTreeNode> orderGrime = evaluateOrder(roleMap);
                            for (CallTreeNode callTreeNode : orderGrime){
                                if (!teeoGrimeInstances.contains(callTreeNode)){
                                    teeoGrimeInstances.add(callTreeNode);
                                }
                            }
                        }
                    }
                }
            }
        }

        //internal here:
        for (RBMLMapping behaviorMapping : rbmlBehavioralMappings){
            evaluateOrderForInternal(behaviorMapping);

        }
    }

    private List<CallTreeNode> evaluateOrder(List<Pair<CallTreeNode, InteractionRole>> roleMap){
        List<CallTreeNode> behavioralGrime = new ArrayList<>();
        for (int i = 0; i < ips.getInteractions().size(); i++){
            InteractionRole expectedInteraction = ips.getInteractions().get(i);
            for (int j = 0; j < roleMap.size(); j++){
                InteractionRole mappedRole = roleMap.get(j).getRight();
                if (expectedInteraction.equals(mappedRole)){
                    //found mapping.
                    if (i > j){
                        //standard order grime. in a collapsed role map, there is no way that i (which is the index in the interaction role)
                        //can be larger than j (the index of the role map after its been collapsed)
                        behavioralGrime.add(roleMap.get(j).getLeft());
                    }
                }
            }
        }
        return behavioralGrime;
    }

    private int findLocationInIPS(BehaviorConformance behaviorConformance){
        for (int i = 0; i < ips.getInteractions().size(); i++){
            if (behaviorConformance.getFunctionHeaderMapping().equals(ips.getInteractions().get(i))){
                return i;
            }
        }
        return 0;
    }


    //this part is tough. I need to (1) locate where my behavior sits within the ips, and (2) make sure that no following behaviors violate order.
    private void evaluateOrderForInternal(RBMLMapping behaviorMapping){
        BehaviorConformance behaviorConformance = behaviorMapping.getBehavioralConformance();
        if (behaviorConformance != null) {
            //will nearly always be non null, just making sure.
            int locationWithinIPS = findLocationInIPS(behaviorConformance);

            List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(behaviorConformance.getUmlOperation());
            List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

            for (UMLAttribute attribute : behaviorConformance.getUmlOperation().getVariableTable().keySet()) {
                //see if class owns (persistent) or operation owns (temporary)
                List<CallTreeNode> orderGrime = evaluateOrderInternal(roleMap, locationWithinIPS);
                if (behaviorConformance.getUmlOperation().getOwningClassifier().getAttributes().contains(attribute)) {
                    //class owns, so its persistent
                    for (CallTreeNode callTreeNode : orderGrime){
                        if (!pioGrimeInstances.contains(callTreeNode)){
                            pioGrimeInstances.add(callTreeNode);
                        }
                    }
                } else {
                    //operation owns (temporary)
                    for (CallTreeNode callTreeNode : orderGrime){
                        if (!tioGrimeInstances.contains(callTreeNode)){
                            tioGrimeInstances.add(callTreeNode);
                        }
                    }
                }
            }

        }
    }

    private List<CallTreeNode> evaluateOrderInternal(List<Pair<CallTreeNode, InteractionRole>> roleMap, int locationWithinIPS){
        List<CallTreeNode> behavioralGrime = new ArrayList<>();
        for (int i = 0; i < roleMap.size(); i++) {
            InteractionRole mappedRole = roleMap.get(i).getRight();
            for (int j = 0; j < ips.getInteractions().size(); j++) {
                InteractionRole expectedInteraction = ips.getInteractions().get(j);
                if (mappedRole.equals(expectedInteraction)) {
                    if (j < locationWithinIPS) {
                        //grime I think.
                        behavioralGrime.add(roleMap.get(i).getLeft());
                    }
                }

            }
        }
        return behavioralGrime;
    }
}
