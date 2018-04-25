package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.*;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
public abstract class PatternMapper {

    protected PatternInstance pi;
    protected UMLClassDiagram umlClassDiagram;
    protected SPS sps;

    public PatternMapper(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        this.pi = pi;
        this.umlClassDiagram = umlClassDiagram;
    }


    /***
     * abstract method that transforms the textual reprensetation of a pattern instance (PI object) to the uml representation.
     *
     */
    protected abstract void mapToUML();


    protected UMLClassifier getClassifierFromString(String majorRoleValue){
        return umlClassDiagram.getPackageTree().getClassifier(convertStringToPackagedString(majorRoleValue), 0, umlClassDiagram.getPackageTree().getRoot());
    }

    protected UMLClassifier getOneMajorRole(PatternInstance pi){
        return getClassifierFromString(pi.getValueOfMajorRole(pi));
    }

    protected UMLClassifier getSecondMajorRole(PatternInstance pi){
        return getClassifierFromString(pi.getValueOfSecondMajorRole(pi));
    }

    protected List<String> convertStringToPackagedString(String value){
        List<String> toRet = new ArrayList<>();
        String[] splitter = value.split("\\.");
        for (String s : splitter){
            toRet.add(s);
        }
        return toRet;
    }

    private UMLOperation matchOperation(UMLClassifier umlClassifier, String operationName, String returnType, List<String> params){
        for (UMLOperation op : umlClassifier.getOperations()){
            if (op.getName().equals(operationName)){
                //will work for most things but we also need ot check params and return type (basically check method signature)
                if (op.getStringReturnDataType().equals(returnType)){
                    boolean foundDifference = false;
                    if (params.size() != op.getParameters().size()){
                        //same method name, but different number of params.. so different method signature. break and check other ops.
                        break;
                    }
                    for (int i = 0; i < params.size(); i++){
                        //param order needs to be preserved too
                        if (!params.get(i).equals(op.getParameters().get(i).getName())){
                            foundDifference = true;
                            break;
                        }
                    }
                    //same method signature!
                    if (foundDifference == false){
                        return op;
                    }
                }
            }
        }
        //should not happen.
        System.out.println("did not find a match between classifier: " + umlClassifier.getName() + " and method name: " + operationName);
        System.exit(0);
        return null;
    }


    //input will look like this: execute(org.openqa.selenium.remote.http.HttpRequest, boolean)
    protected List<String> getParamsFromNameParams(String minorClassOperationNameParams){
        List<String> paramsList = new ArrayList<>();
        String paramsBlock = minorClassOperationNameParams.split("\\(")[1];
        //minus 2 becuase last char is a ')'
        if (paramsBlock.length() == 1){
            return paramsList;
        }
        String[] params = paramsBlock.substring(0, paramsBlock.length()-1).split("\\,");
        for (String s : params){
            s = s.replace(" ", "");//remove spaces
            if (s.contains(".")){
                //package
                String[] splitter = s.split("\\.");
                s = splitter[splitter.length-1];
            }
            paramsList.add(s);
        }
        return paramsList;
    }

    protected UMLAttribute matchAttribute(UMLClassifier umlClassifier, String attributeName){
        for (UMLAttribute attribute : umlClassifier.getAttributes()){
            if (attribute.getName().equals(attributeName)){
                //dont need to check type because attribute names are unique.
                return attribute;
            }
        }
        //should not happen.
        System.out.println("did not find a match between classifier: " + umlClassifier.getName() + " and attribute: " + attributeName);
        System.exit(0);
        return null;
    }

    protected UMLAttribute getAttributeFromString(UMLClassifier umlClassifier, String attributeName){
        String parsedAttributeName = parseRoleName(attributeName);
        UMLAttribute attribute = matchAttribute(umlClassifier, parsedAttributeName);
        return attribute;
    }


    protected UMLOperation getOperationFromString(UMLClassifier umlClassifier, String operationName){
        //operation name is unparsed; need to parse it.
        String roleName = parseRoleName(operationName);
        String roleType = parseRoleType(operationName);
        List<String> params = getParamsFromNameParams(roleName);
        //remove parens from roleName
        roleName = roleName.split("\\(")[0];

        UMLOperation operation = matchOperation(umlClassifier, roleName, roleType, params);
        return operation;
    }


    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return fView or execute()
    private String parseRoleName(String role){
        String ownerRemoved = role.split("\\:\\:")[1];
        String typeRemoved = ownerRemoved.split("\\:")[0];
        return typeRemoved;
    }


    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return CH.ifa.draw.standard.AlignCommand in both cases.
    private String parseRoleOwner(String role){
        String nameRemoved = role.split("\\:\\:")[0];
        return nameRemoved;
    }

    //input will look like: "CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" or
    //"CH.ifa.draw.standard.AlignCommand::execute():void"
    //need to return CH.ifa.draw.framework.DrawingView or void
    private String parseRoleType(String role){
        String ownerRemoved = role.split("\\:\\:")[1];
        String nameRemoved = ownerRemoved.split("\\:")[1];
        if (nameRemoved.contains("\\.")){
            String[] typeSplitter = nameRemoved.split("\\.");
            nameRemoved = typeSplitter[typeSplitter.length-1];
        }
        return nameRemoved;
    }

    public abstract List<Pair<String, UMLClassifier>> getClassifierModelBlocks();
    public abstract List<Pair<String, UMLOperation>> getOperationModelBlocks();
    public abstract List<Pair<String, UMLAttribute>> getAttributeModelBlocks();

    public List<RBMLMapping> mapStructure(SPS sps) {
        this.sps = sps;
        List<RBMLMapping> allMappings = new ArrayList<>();
        List<RBMLMapping> structuralMappings = mapStructuralComponents(sps);
        List<RBMLMapping> relationshipMappings = mapRelationships(sps, structuralMappings);


        allMappings.addAll(structuralMappings);
        allMappings.addAll(relationshipMappings);

        //removed mapping by pass by ref.
        multiplicityChecks(allMappings);
        return allMappings;
    }

    public List<RBMLMapping> mapStructuralComponents(SPS sps){
        List<RBMLMapping> structuralMappings = new ArrayList<>();
        for (StructuralRole strRole : sps.getClassifierRoles()) {
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()) {
                if (modelBlockPair.getKey().equals(strRole.compareName())) {
                    //match found!
                    structuralMappings.add(new RBMLMapping(strRole, modelBlockPair.getValue()));
                }

            }
            for (AttributeRole attributeRole : strRole.getAttributes()) {
                for (Pair<String, UMLAttribute> attributePairBlock : getAttributeModelBlocks()) {
                    if (attributePairBlock.getKey().equals(attributeRole.compareName())) {
                        //attribute match found!
                        structuralMappings.add(new RBMLMapping(attributeRole, attributePairBlock.getValue()));
                    }
                }
            }
            for (OperationRole operationRole : strRole.getOperations()){
                for (Pair<String, UMLOperation> operationPairBlock : getOperationModelBlocks()) {
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

    public List<RBMLMapping> mapRelationships(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> relationshipMappings = new ArrayList<>();
        List<RBMLMapping> associationMappings = mapAssociations(sps, structuralMappings);
        List<RBMLMapping> generalizationMappings = mapGeneralizations(sps, structuralMappings);
        List<RBMLMapping> dependencyMappings = mapDependencies(sps, structuralMappings);
        List<RBMLMapping> realizationMappings = mapRealizations(sps, structuralMappings);

        relationshipMappings.addAll(associationMappings);
        relationshipMappings.addAll(generalizationMappings);
        relationshipMappings.addAll(dependencyMappings);
        relationshipMappings.addAll(realizationMappings);

        return relationshipMappings;
    }

    private List<RBMLMapping> mapGeneralizations(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> generalizationMappings = new ArrayList<>();
        for (RelationshipRole generalizationRole : sps.getGeneralizationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(generalizationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = generalizationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.GENERALIZATION)){
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

    private List<RBMLMapping> mapAssociations(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> associationMappings = new ArrayList<>();
        for (RelationshipRole associationRole : sps.getAssociationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(associationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = associationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.ASSOCIATION)){
                            //omg. found a relationship mapping finally.
                            //need to enter a pair because Relationship as an enum was a dumb design choice 6 months ago.
                            associationMappings.add(new RBMLMapping(associationRole, new Pair<>(modelBlockPair.getValue(), mappedRoleEndpoint)));
                        }
                    }
                }
            }
        }
        return associationMappings;
    }

    private List<RBMLMapping> mapDependencies(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> dependencyMappings = new ArrayList<>();
        for (RelationshipRole dependencyRole : sps.getDependencyRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(dependencyRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = dependencyRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.DEPENDENCY)){
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

    private List<RBMLMapping> mapRealizations(SPS sps, List<RBMLMapping> structuralMappings){
        List<RBMLMapping> realizationMappings = new ArrayList<>();
        for (RelationshipRole realizationRole : sps.getImplementationRoles()){
            for (Pair<String, UMLClassifier> modelBlockPair : getClassifierModelBlocks()){
                if (modelBlockPair.getKey().equals(realizationRole.getConnection1().getKey().compareName())){
                    //rbml association and uml class have same single endpoint. now we check other endpoint.
                    StructuralRole nodeV = realizationRole.getConnection1().getValue();
                    RBMLMapping endpointMap = getMappingIfExists(structuralMappings, nodeV);
                    if (endpointMap != null){
                        //will be null if the role was not mapped in the first place.
                        UMLClassifier mappedRoleEndpoint = (UMLClassifier)endpointMap.getMappedPair().getValue();
                        if (umlClassDiagram.getClassDiagram().edgeValue(modelBlockPair.getValue(), mappedRoleEndpoint).equals(Relationship.REALIZATION)){
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
                mappings = removeMapping(role, mappings);
            }
        }
    }

    private List<RBMLMapping> removeMapping(Role role, List<RBMLMapping> mappings){
        List<RBMLMapping> removedMapping = new ArrayList<>();
        for (RBMLMapping rbmlMapping : mappings){
            if (!rbmlMapping.getRole().equals(role)){
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

    public List<RBMLMapping> mapBehavior(List<RBMLMapping> structureMappings, IPS ips){
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
                operationRoles.add((OperationRole) rbmlMapping.getRole());
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
        for (int i = 0; i < interactionRoles.size(); i++){
            InteractionRole interactionRole = interactionRoles.get(i);
            for (int j = 0; j < callTreeAsList.size(); j++){
                CallTreeNode<String> callTreeNode = callTreeAsList.get(j);
                if (i == 0 && j == 0){
                    //'base case', in a sense. By definition we start here as we enter a method.
                    UMLOperation baseCase = getUMLOperationObjFromName(structuralMappings, callTreeNode.getName());
                    behaviorMappings.add(new RBMLMapping(interactionRole, baseCase));
                    System.out.println("Added ips mapping from: " + interactionRole.getOperationRole().getName() + " " + baseCase.getName());
                }
            }
        }
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

    public abstract void printSummary();

}
