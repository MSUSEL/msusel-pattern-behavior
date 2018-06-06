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
import javafx.util.Pair;
import lombok.Getter;

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
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getClassifierModelBlocks()) {
                if (modelBlockPair.getKey().equals(strRole.compareName())) {
                    //match found!
                    structuralMappings.add(new RBMLMapping(strRole, modelBlockPair.getValue()));
                }
            }
            for (AttributeRole attributeRole : strRole.getAttributes()) {
                for (Pair<String, UMLAttribute> attributePairBlock : patternInstance.getAttributeModelBlocks()) {
                    if (attributePairBlock.getKey().equals(attributeRole.compareName())) {
                        //attribute match found!
                        structuralMappings.add(new RBMLMapping(attributeRole, attributePairBlock.getValue()));
                    }
                }
            }
            for (OperationRole operationRole : strRole.getOperations()){
                for (Pair<String, UMLOperation> operationPairBlock : patternInstance.getOperationModelBlocks()) {
                    if (operationPairBlock.getKey().equals(operationRole.compareName())) {
                        //operation match found!
                        RBMLMapping mapping = new RBMLMapping(operationRole, operationPairBlock.getValue());
                        structuralMappings.add(mapping);
                    }
                }
            }
        }
        return structuralMappings;
    }

    public List<RBMLMapping> mapRelationships(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> relationshipMappings = new ArrayList<>();
        List<RBMLMapping> associationMappings = mapAssociations(structuralMappings);
        List<RBMLMapping> generalizationMappings = mapGeneralizations(structuralMappings);
        List<RBMLMapping> dependencyMappings = mapDependencies(structuralMappings);
        List<RBMLMapping> realizationMappings = mapRealizations(structuralMappings);

        relationshipMappings.addAll(associationMappings);
        relationshipMappings.addAll(generalizationMappings);
        relationshipMappings.addAll(dependencyMappings);
        relationshipMappings.addAll(realizationMappings);

        return relationshipMappings;
    }

    private List<RBMLMapping> mapGeneralizations(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> generalizationMappings = new ArrayList<>();
        for (RelationshipRole generalizationRole : sps.getGeneralizationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(generalizationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = generalizationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).get().equals(Relationship.GENERALIZATION)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            generalizationMappings.add(new RBMLMapping(generalizationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                    }
                    }
                }
            }
        }
        return generalizationMappings;
    }

    private List<RBMLMapping> mapAssociations(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> associationMappings = new ArrayList<>();
        for (RelationshipRole associationRole : sps.getAssociationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(associationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = associationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().hasEdgeConnecting(modelBlockPair.getValue(), mappedRoleEndpoint)) {
                            if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).get().equals(Relationship.ASSOCIATION)) {
                                //omg. found a relationship mapping finally.
                                //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                                associationMappings.add(new RBMLMapping(associationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                            }
                        } else {
                            //I get here when a pattern class has a wildcard or generics definition AND the pattern4 tool does not properly
                            //identify the correct data type ... idk how to properly fix this at this point in time, so I might just
                            //consider tossing the pattern out if this is ever the case....
                        }

                    }
                }
            }
        }
        return associationMappings;
    }

    private List<RBMLMapping> mapDependencies(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> dependencyMappings = new ArrayList<>();
        for (RelationshipRole dependencyRole : sps.getDependencyRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(dependencyRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = dependencyRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).get().equals(Relationship.DEPENDENCY)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            dependencyMappings.add(new RBMLMapping(dependencyRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                        }
                    }
                }
            }
        }
        return dependencyMappings;
    }

    private List<RBMLMapping> mapRealizations(List<RBMLMapping> structuralMappings){
        List<RBMLMapping> realizationMappings = new ArrayList<>();
        for (RelationshipRole realizationRole : sps.getImplementationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : patternInstance.getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(realizationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = realizationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).get().equals(Relationship.REALIZATION)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            realizationMappings.add(new RBMLMapping(realizationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                        }
                    }
                }
            }
        }
        return realizationMappings;
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
        List<RBMLMapping> behaviorMappings = new ArrayList<>();
        for (OperationRole operationRole : getOperationsFromMappings(structureMappings)){
            List<UMLOperation> umlOperations = getOperationsFromMapping(operationRole, structureMappings);
            for (UMLOperation mappedOperation : umlOperations) {
                CallTreeNode<String> callTree = mappedOperation.getCallTreeString();
                List<CallTreeNode<String>> callTreeAsList = callTree.convertMeToOrderedList();
                orderedListSubsetComparison(callTreeAsList, ips.getInteractions(), structureMappings);
            }
        }
        return behaviorMappings;
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

    /***
     * method to perform the ordered list subset comparison. Initially takes in a list representation of the call tree and a list of interaction roles.
     * Will iteratively traverse interactions and also call tree nodes, matching the two if they map.
     *
     * Note that this will not focus on variable semantics (names), but rather focus on types that are called from teh callTree.
     *
     * @param callTreeAsList
     * @param interactionRoles
     */
    private List<RBMLMapping> orderedListSubsetComparison(List<CallTreeNode<String>> callTreeAsList, List<InteractionRole> interactionRoles, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> behaviorMappings = new ArrayList<>();
        //go to size -1 because im doing look-ahead checking.
        for (int i = 0; i < interactionRoles.size()-1; i++){
            InteractionRole interactionRole = interactionRoles.get(i);
            //sequentially the next interaction
            InteractionRole nextInteractionRole = interactionRoles.get(i+1);
            for (int j = 0; j < callTreeAsList.size(); j++){
                CallTreeNode<String> callTreeNode = callTreeAsList.get(j);
                if (i == 0 && j == 0){
                    //'base case', in a sense. By definition we start here as we enter a method.
                    UMLOperation baseCase = getUMLOperationObjFromName(structuralMappings, callTreeNode.getName());
                    behaviorMappings.add(new RBMLMapping(interactionRole, baseCase));
                    System.out.println("Added ips mapping from: " + interactionRole.getOperationRole().getName() + " " + baseCase.getName());
                    //tricky because technically we can map the same role to more than one callTree node.
                }else{
                    if (callTreeNode.getTagName().contains("decl{")){
                        //declaration, indicates a use dependency (declaration) and potentially a behavioral mapping
                        //TODO - behavior mappings
                        //String typeOfDecl = getTypeFromCallTreeTagDecl(callTreeNode.getTagName());
                        //UMLClassifier nextClassifierName = getClassifierFromMappingValue();

                    }
                }
            }
        }
        for (RBMLMapping rbmlMapping : behaviorMappings){
            rbmlMapping.printSummary();
            if (rbmlMapping.getUmlArtifact() instanceof UMLOperation){
                ((UMLOperation)rbmlMapping.getUmlArtifact()).getCallTreeString().printTree();
            }
        }
        if (Main.counter == 99){
            System.exit(0);
        }

        Main.counter++;
        return behaviorMappings;
    }

    private UMLOperation getUMLOperationObjFromName(List<RBMLMapping> structionalMappings, String opName){
        for (RBMLMapping rbmlMapping : structionalMappings){
            if (rbmlMapping.getUmlArtifact() instanceof UMLOperation){
                UMLOperation mappedOperation = (UMLOperation) rbmlMapping.getUmlArtifact();
                if (mappedOperation.getName().equals(opName)){
                    //found a map!
                    return mappedOperation;
                }
            }
        }
        //might return null here.. would happen if an operation is called that is not mapped structurally.
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
