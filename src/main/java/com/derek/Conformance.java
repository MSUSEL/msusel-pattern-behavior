/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek;

import com.derek.rbml.*;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Conformance {

    private SPS sps;
    private IPS ips;
    private PatternMapper patternInstance;
    private UMLClassDiagram umlClassDiagram;

    public Conformance(SPS sps, IPS ips, PatternMapper patternMapper, UMLClassDiagram umlClassDiagram){
        this.sps = sps;
        this.ips = ips;
        this.patternInstance = patternMapper;
        this.umlClassDiagram = umlClassDiagram;
    }

    public List<RBMLMapping> mapStructure() {
        List<RBMLMapping> allMappings = new ArrayList<>();
        List<RBMLMapping> structuralMappings = mapStructuralComponents();
        List<RBMLMapping> relationshipMappings = mapRelationships(structuralMappings);

        allMappings.addAll(structuralMappings);
        allMappings.addAll(relationshipMappings);

        //removed mapping by pass by ref.
        multiplicityChecks(allMappings);
        return allMappings;
    }

    public List<RBMLMapping> mapStructuralComponents(){
        List<RBMLMapping> structuralMappings = new ArrayList<>();
        for (StructuralRole strRole : sps.getClassifierRoles()) {
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getAllParticipatingClasses()) {
                if (modelBlockPair.getKey().equals(strRole.compareName())) {
                    //classifier match found
                    structuralMappings.add(new RBMLMapping(strRole, modelBlockPair.getValue()));
                    //attempt to match attributes
                    for (AttributeRole attributeRole : strRole.getAttributes()){
                        for (Pair<String, UMLAttribute> attributeModelBlockPair : patternInstance.getAttributeModelBlocks()){
                            if (modelBlockPair.getValue().getAttributes().contains(attributeModelBlockPair.getValue())) {
                                if (attributeModelBlockPair.getKey().equals(attributeRole.compareName())) {
                                    structuralMappings.add(new RBMLMapping(attributeRole, attributeModelBlockPair.getValue()));
                                }
                            }
                        }
                    }
                }
                //moving operation mappings out of classifier check because operations can be mapped to more than 1 classifier
                //especially in inheritance hierarchy situations.
                //there is still a bug here tho.. only 1 operation object is getting mapped.
                for (OperationRole operationRole : strRole.getOperations()){
                    for (Pair<String, UMLOperation> operationModelBlockPair : patternInstance.getOperationModelBlocks()){
                        if (modelBlockPair.getValue().getOperations().contains(operationModelBlockPair.getValue())) {
                            if (operationModelBlockPair.getKey().equals(operationRole.compareName())) {
                                structuralMappings.add(new RBMLMapping(operationRole, operationModelBlockPair.getValue()));
                            }
                        }
                    }
                }
            }
        }
        return structuralMappings;
    }

    public List<RBMLMapping> mapRelationships(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> relationshipMappings = new ArrayList<>();
        List<RBMLMapping> associationMappings = mapRelationships(structuralMappings, sps.getAssociationRoles(), RelationshipType.ASSOCIATION);
        List<RBMLMapping> generalizationMappings = mapRelationships(structuralMappings, sps.getGeneralizationRoles(), RelationshipType.GENERALIZATION);
        List<RBMLMapping> dependencyMappings = mapRelationships(structuralMappings, sps.getDependencyRoles(), RelationshipType.DEPENDENCY);
        List<RBMLMapping> realizationMappings = mapRelationships(structuralMappings, sps.getImplementationRoles(), RelationshipType.REALIZATION);
        List<RBMLMapping> realizationOrGeneralizationMappings = mapRelationships(structuralMappings, sps.getImplementationOrGeneralizationRoles(), RelationshipType.GENERALIZATION);
        realizationOrGeneralizationMappings.addAll(mapRelationships(structuralMappings, sps.getImplementationOrGeneralizationRoles(), RelationshipType.REALIZATION));

        relationshipMappings.addAll(associationMappings);
        relationshipMappings.addAll(generalizationMappings);
        relationshipMappings.addAll(dependencyMappings);
        relationshipMappings.addAll(realizationMappings);
        relationshipMappings.addAll(realizationOrGeneralizationMappings);

        return relationshipMappings;
    }

    private List<RBMLMapping> mapRelationships(List<RBMLMapping> structuralMappings, List<RelationshipRole> relationshipRoles, RelationshipType relationshipType){
        List<RBMLMapping> associationMappings = new ArrayList<>();
        for (RelationshipRole relationshipRole : relationshipRoles){
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getAllParticipatingClasses()){
                if (modelBlockPair.getKey().equals(relationshipRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = relationshipRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().hasEdgeConnecting(modelBlockPair.getValue(), mappedRoleEndpoint)) {
                            for (Relationship r : umlClassDiagram.getClassDiagram().edgesConnecting(modelBlockPair.getValue(), mappedRoleEndpoint)){
                                if (r.getRelationshipType() == relationshipType){
                                    //omg. found a relationship mapping finally.
                                    associationMappings.add(new RBMLMapping(relationshipRole, umlClassDiagram.getRelationshipFromClassDiagram(modelBlockPair.getValue(), mappedRoleEndpoint, relationshipType)));
                                }else{
                                    //I get here when a pattern class has a wildcard or generics definition AND the pattern4 tool does not properly
                                    //identify the correct data type ... idk how to properly fix this at this point in time, so I might just
                                    //consider tossing the pattern out if this is ever the case....
                                }
                            }
                        }

                    }
                }
            }
        }
        return associationMappings;
    }

    /***
     * This method runs through the current mappings and removes all multiplicities that do not conform.
     * @param mappings
     *  pass by ref return value (mappings)
     */
    private void multiplicityChecks(List<RBMLMapping> mappings){
        for (Role role : sps.getAllRolesWithMultiplicities()){
            int numberOfTimesRoleIsMapped = countNumberOfTimesRoleIsMapped(role, mappings);
            if (!checkBetweeness(role, numberOfTimesRoleIsMapped)){
                //remove mapping from list of mappings
                 for (RBMLMapping toRemove : removeMapping(role, mappings)){
                    mappings.remove(toRemove);
                }
            }
        }
    }

    private List<RBMLMapping> removeMapping(Role role, List<RBMLMapping> mappings){
        List<RBMLMapping> removedMapping = new ArrayList<>();
        for (RBMLMapping rbmlMapping : mappings){
            if (rbmlMapping.getRole().equals(role)){
                //not flagged for removal
                removedMapping.add(rbmlMapping);
            }
        }
        return removedMapping;
    }

    private boolean checkBetweeness(Role role, int numberOfTimesMapped){
        if (numberOfTimesMapped >= role.getMultiplicity().getKey() && numberOfTimesMapped <= role.getMultiplicity().getValue()){
            //is in between
            return true;
        }
        return false;
    }

    private int countNumberOfTimesRoleIsMapped(Role role, List<RBMLMapping> mappings){
        int count = 0;
        for (RBMLMapping mapping : mappings){
            if (mapping.getRole().equals(role)){
                //mapping exists
                count++;
            }
        }
        return count;
    }

    public List<RBMLMapping> mapBehavior(List<RBMLMapping> structureMappings){
        List<Pair<UMLOperation, BehaviorConformance>> behaviorMappings = new ArrayList<>();
        for (OperationRole operationRole : getOperationsFromMappings(structureMappings)){
            List<UMLOperation> umlOperations = getOperationsFromMapping(operationRole, structureMappings);
            for (UMLOperation mappedOperation : umlOperations) {
                CallTreeNode<String> callTree = mappedOperation.getCallTreeString();
                //check for null call tree here - null call tree will happen if the mapped op is an abstract declaration.
                //in this case, the method won't have any behavior, so I can literally just skip over it.
                if (callTree != null) {
                    List<CallTreeNode<String>> callTreeAsList = callTree.convertMeToOrderedList();
                    BehaviorConformance behaviorConformance = new BehaviorConformance(ips, callTreeAsList, structureMappings, mappedOperation);
                    if (behaviorConformance.getFunctionHeaderMapping() != null){
                        //will be null if the IPS does not include this mapping.. and in this case we should ignore it.
                        behaviorConformance.findUnnecessaryActions(mappedOperation);
                        behaviorMappings.add(new ImmutablePair<>(mappedOperation, behaviorConformance));
                    }
                }
            }
        }
        //here I would like to check all behaivoral mappings and coalesce them. The problem is that each operation maps onyl certain elements of a
        //pattern's behavior. For example, in an observer pattern the update() method and the setSTate() method are both required, yet there will
        //maybe never be a single flow that satisfies both at the same time. So, because I am already statically scanning all pertinent methods to
        //find behavior like this, now I need to 'combine them'.

        List<RBMLMapping> rbmlBehaviorMappings = collapseBehaviors(behaviorMappings);


        return rbmlBehaviorMappings;
    }

    private List<RBMLMapping> collapseBehaviors(List<Pair<UMLOperation, BehaviorConformance>> behaviorMappings ){
        List<RBMLMapping> rbmlBehavioralMappings = new ArrayList<>();
        for (Pair<UMLOperation, BehaviorConformance> behaviorMapping : behaviorMappings){
            for (Pair<CallTreeNode, InteractionRole> pair : behaviorMapping.getRight().getRoleMap()){
                if (ips.getInteractions().contains(pair.getRight())){
                    //these behavioral mappings contain an interaction role on the left (of the mapping), and a behaviormapping instance on the right.
                    //this can be read as saying an interaction role maps to a behavioralconformance, which by itself has umloperation information, as well as
                    //call tree info
                    rbmlBehavioralMappings.add(new RBMLMapping(pair.getRight(), behaviorMapping.getRight()));
                }
            }
        }
        return rbmlBehavioralMappings;
    }

    private List<OperationRole> getOperationsFromMappings(List<RBMLMapping> structureMappings){
        List<OperationRole> operationRoles = new ArrayList<>();
        for (RBMLMapping rbmlMapping : structureMappings){
            if (rbmlMapping.getRole() instanceof OperationRole){
                OperationRole operationRole = (OperationRole) rbmlMapping.getRole();
                if (!operationRoles.contains(operationRole)) {
                    operationRoles.add(operationRole);
                }
            }
        }
        return operationRoles;
    }

    /***
     * becasue I can have more than one uml operation mapped to one role, I need to return a list of uml operations
     * @param opRole
     * @param structureMappings
     * @return
     */
    private List<UMLOperation> getOperationsFromMapping(OperationRole opRole, List<RBMLMapping> structureMappings){
        List<UMLOperation> operations = new ArrayList<>();
        for (RBMLMapping rbmlMapping : structureMappings){
            if (rbmlMapping.getRole().equals(opRole)){
                operations.add((UMLOperation)rbmlMapping.getUmlArtifact());
            }
        }
        if (operations.size() != 0){
            return operations;
        }
        System.out.println("was not able to find a mapped uml operation from the list of structural mappings. ");
        System.out.println("likely this means no operation was found that runs the behavior of this pattern.");
        System.out.println("Exiting.. ");
        System.exit(0);
        return null;
    }

    private RBMLMapping getMappingIfExists(List<RBMLMapping> mappings, StructuralRole structuralRole){
        for (RBMLMapping rbmlMapping : mappings){
            if (rbmlMapping.getMappedPair().getKey().equals(structuralRole)){
                return rbmlMapping;
            }
        }
        //might happen if structural role was not mapped in the first place.
        return null;
    }

}
