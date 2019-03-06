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
    private List<CallTreeNode> pioGrimeInstances;
    private List<CallTreeNode> teaoGrimeInstances;
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
        calculateRepetitionGrime();
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
        pioGrimeInstances = new ArrayList<>();
        teaoGrimeInstances = new ArrayList<>();
        tioGrimeInstances = new ArrayList<>();
        findOrderGrime();
    }
    private void calculateRepetitionGrime(){
        pearGrimeInstances = new ArrayList<>();
        peerGrimeInstances = new ArrayList<>();
        pirGrimeInstances = new ArrayList<>();
        tearGrimeInstances = new ArrayList<>();
        teerGrimeInstances = new ArrayList<>();
        tirGrimeInstances = new ArrayList<>();
        findRepetitionGrime();
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
        for (UMLClassifier afferentClassifier : patternMapper.getUniqueAfferentClassifiers()) {
            //candiate for persistent grime.
            for (UMLOperation operation : afferentClassifier.getOperationsIncludingConstructorsIfExists()) {
                List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

                List<CallTreeNode> orderGrime = evaluateOrder(roleMap);
                for (CallTreeNode callTreeNode : orderGrime) {
                    if (callTreeNode.isCall()) {
                        String varName = callTreeNode.parseVarNameFromCall();
                        UMLAttribute asAttribute = operation.findVariableUsageInTable(varName);
                        if (operation.getOwningClassifier().getAttributes().contains(asAttribute)) {
                            if (!peaoGrimeInstances.contains(callTreeNode)) {
                                peaoGrimeInstances.add(callTreeNode);
                            }
                        } else {
                            //temporary
                            if (!teaoGrimeInstances.contains(callTreeNode)) {
                                teaoGrimeInstances.add(callTreeNode);
                            }
                        }
                    }
                }
            }

        }
        //efferent DNE!!! for order at least.

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
            //internal here:
            //dont need so much.
            //see if class owns (persistent) or operation owns (temporary)
            List<CallTreeNode> orderGrime = evaluateOrderInternal(roleMap, locationWithinIPS);

            for (CallTreeNode callTreeNode : orderGrime){
                UMLAttribute attribute = behaviorConformance.getUmlOperation().findVariableUsageInTable(callTreeNode.parseVarNameFromCall());
                if (attribute != null) {
                    //this line of logic is that attribute can be a variable.call or just a call. (observer.update or notifyObservers).
                    //if notifyObservers is the next call then I think this is just flat out temporary order, because it happens in the scope of the
                    //method.
                    if (behaviorConformance.getUmlOperation().getOwningClassifier().getAttributes().contains(attribute)) {
                        //could be named the same

                        if (behaviorConformance.getUmlOperation().getLocalVariableDecls().contains(attribute)) {
                            //temp too
                            if (!tioGrimeInstances.contains(callTreeNode)) {
                                tioGrimeInstances.add(callTreeNode);
                            }
                        } else {
                            if (!pioGrimeInstances.contains(callTreeNode)) {
                                pioGrimeInstances.add(callTreeNode);
                            }
                        }
                    } else {
                        //temporary
                        if (!tioGrimeInstances.contains(callTreeNode)) {
                            tioGrimeInstances.add(callTreeNode);
                        }
                    }
                }else{
                    //not a valid attribute, so must be temporary grime.
                    if (!tioGrimeInstances.contains(callTreeNode)) {
                        tioGrimeInstances.add(callTreeNode);
                    }
                }
            }
            //internal repetition here.
            List<CallTreeNode> repetitionGrime = evaluateRepetition(roleMap);
            for (CallTreeNode callTreeNode : repetitionGrime){
                if (!pearGrimeInstances.contains(callTreeNode)){
                    pearGrimeInstances.add(callTreeNode);
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
                        behavioralGrime.add(roleMap.get(i-1).getLeft());
                    }
                    locationWithinIPS = j;
                }
            }
        }
        return behavioralGrime;
    }

    private void findRepetitionGrime(){
        for (UMLClassifier afferentClassifier : patternMapper.getUniqueAfferentClassifiers()) {
            //for afferent its gotta be roles.
            for (UMLOperation operation : afferentClassifier.getOperationsIncludingConstructorsIfExists()) {
                List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

                List<CallTreeNode> repetition = evaluateRepetition(roleMap);

                for (CallTreeNode callTreeNode : repetition){
                    if (callTreeNode.isCall()){
                        String varName = callTreeNode.parseVarNameFromCall();
                        UMLAttribute asAttribute = operation.findVariableUsageInTable(varName);
                        if (operation.getOwningClassifier().getAttributes().contains(asAttribute)){
                            if (!pearGrimeInstances.contains(callTreeNode)) {
                                pearGrimeInstances.add(callTreeNode);
                            }
                        }else{
                            //temporary
                            if (!tearGrimeInstances.contains(callTreeNode)) {
                                tearGrimeInstances.add(callTreeNode);
                            }
                        }
                    }
                }
            }
        }
        //external
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers()){
            for (UMLOperation operation : umlClassifier.getOperations()){
                List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                //only need the full role map here. So, for external the repetition that might exist is when a pattern calls
                //an external class more than once.

                //external is a little funky and I am setting repetition sets
                evaluateExternalRepetition(fullRoleMap, operation, patternMapper.getUniqueEfferentClassifiers());

            }
        }

        //internal
        for (UMLClassifier umlClassifier : patternMapper.getAllParticipatingClassifiersOnlyUMLClassifiers()){
            //just pattern classes
            for (UMLOperation operation : umlClassifier.getOperations()){
                List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap = behaviorMappingUtils.getRoleMap(operation);
                List<Pair<CallTreeNode, InteractionRole>> roleMap = behaviorMappingUtils.getCollapsedRoleMap(fullRoleMap);

                List<CallTreeNode> repetition = evaluateRepetition(roleMap);
                for (CallTreeNode callTreeNode : repetition){
                    if (callTreeNode.isCall()) {
                        String varName = callTreeNode.parseVarNameFromCall();
                        UMLAttribute asAttribute = operation.findVariableUsageInTable(varName);
                        if (operation.getOwningClassifier().getAttributes().contains(asAttribute)){
                            if (!pirGrimeInstances.contains(callTreeNode)) {
                                pirGrimeInstances.add(callTreeNode);
                            }
                        }else{
                            //temporary
                            if (!tirGrimeInstances.contains(callTreeNode)) {
                                tirGrimeInstances.add(callTreeNode);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<CallTreeNode> evaluateRepetition(List<Pair<CallTreeNode, InteractionRole>> roleMap){
        List<CallTreeNode> behavioralGrime = new ArrayList<>();
        List<InteractionRole> seenInteractionRoles = new ArrayList<>();
        for (int i = 0; i < roleMap.size(); i++) {
            for (int j = 0; j < ips.getInteractions().size(); j++) {
                if (roleMap.get(i).getRight().equals(ips.getInteractions().get(j))) {
                    //found a match between mapped role and interaction role.
                    if (seenInteractionRoles.contains(roleMap.get(i).getRight())) {
                        behavioralGrime.add(roleMap.get(i).getLeft());
                    }else {
                        seenInteractionRoles.add(roleMap.get(i).getRight());
                    }
                }
            }
        }
        return behavioralGrime;
    }

    private void evaluateExternalRepetition(List<MutablePair<CallTreeNode, InteractionRole>> fullRoleMap, UMLOperation owningOperation, Set<UMLClassifier> externalClassifiers){
        List<CallTreeNode> seenRepetitions = new ArrayList<>();
        for (int i = 0; i < fullRoleMap.size(); i++){
            //I might need to put a check here to amke sure interaction role is not null.. I'm ignoring it for now because
            //the internals of the role map should not be repeated anyways.
            CallTreeNode current = fullRoleMap.get(i).getLeft();
            if (current.isCall()) {
                String varName = current.parseVarNameFromCall();
                UMLAttribute attribute = owningOperation.findVariableUsageInTable(varName);
                if (externalClassifiers.contains(attribute.getType())) {
                    //definitely an external type.
                    if (owningOperation.getOwningClassifier().getAttributes().contains(attribute)) {
                        //persistent candidate
                        if (seenRepetitions.contains(current)){
                            //repetition finally
                            peerGrimeInstances.add(current);
                        }else{
                            //not seen yet.. this is PEE grime but not quite PEER
                            seenRepetitions.add(current);
                        }
                    } else {
                        //temporary candidate
                        if (seenRepetitions.contains(current)){
                            //repetition finally
                            teerGrimeInstances.add(current);
                        }else{
                            //not seen yet.. this is TEE grime but not quite TEER
                            seenRepetitions.add(current);
                        }

                    }
                }
            }
        }
    }
}
