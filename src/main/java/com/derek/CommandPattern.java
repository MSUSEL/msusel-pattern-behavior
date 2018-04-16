package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.*;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandPattern extends PatternMapper{

    private UMLClassifier receiverClassifier;
    private UMLClassifier concreteCommand;
    private UMLAttribute receiverAttribute;
    private UMLOperation executeOperation;

    //I think I want to store relationships here too... which will be gathered from teh uml class diagram.
    //well, this is all stored implicitly in each role's object.
    //generalizations: umlClassifier.getExtendsParents()
    //implementations: umlClassifier.getImplementsParents()
    //associations: umlClassifier.getAttributes().getType() & umlClassifier.getOperations().getType()
    //dependencies can be done by digging into executeOperation, but are not done as of now. (4/12)



    public CommandPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram){
        super(pi, umlClassDiagram);
    }

    /***
     * example input:
     * 		<instance>
     * 			<role name="Receiver" element="CH.ifa.draw.framework.DrawingView" />
     * 			<role name="ConcreteCommand" element="CH.ifa.draw.standard.AlignCommand" />
     * 			<role name="receiver" element="CH.ifa.draw.standard.AlignCommand::fView:CH.ifa.draw.framework.DrawingView" />
     * 			<role name="Execute()" element="CH.ifa.draw.standard.AlignCommand::execute():void" />
     * 		</instance>
     */
    @Override
    protected void mapToUML(){
        receiverClassifier = getOneMajorRole(pi);
        concreteCommand = getSecondMajorRole(pi);
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        Pair<String, String> recieverRoleString = minorRoles.get(0);
        Pair<String, String> executeRoleString = minorRoles.get(1);

        this.receiverAttribute = getAttributeFromString(concreteCommand, recieverRoleString.getValue());
        this.executeOperation = getOperationFromString(concreteCommand, executeRoleString.getValue());
    }

    @Override
    public List<UMLClassifier> getClassifierModelBlocks() {
        List<UMLClassifier> modelBlocks= new ArrayList<>();
        modelBlocks.add(receiverClassifier);
        modelBlocks.add(concreteCommand);
        return modelBlocks;
    }
    @Override
    public List<UMLOperation> getOperationModelBlocks() {
        List<UMLOperation> modelBlocks= new ArrayList<>();
        modelBlocks.add(executeOperation);
        return modelBlocks;
    }
    @Override
    public List<UMLAttribute> getAttributeModelBlocks() {
        List<UMLAttribute> modelBlocks= new ArrayList<>();
        modelBlocks.add(receiverAttribute);
        return modelBlocks;
    }

    @Override
    public List<RBMLMapping> map(SPS sps) {
        List<RBMLMapping> knownMappings = new ArrayList<>();
        knownMappings.addAll(structureMap(sps));
        knownMappings.addAll(associationMap(sps));





        return knownMappings;
    }

    private List<RBMLMapping> structureMap(SPS sps){
        List<RBMLMapping> knownStructuralMappings = new ArrayList<>();
        for (StructuralRole strRole : sps.getClassifierRoles()){
            switch(strRole.getType()){
                case "|Receiver":
                    knownStructuralMappings.add(new RBMLMapping(strRole, receiverClassifier));
                    break;
                case "|ConcreteCommand":
                    knownStructuralMappings.add(new RBMLMapping(strRole, concreteCommand));
                    for (RoleAttribute attrRole : strRole.getAttributes()){
                        if (attrRole.getType().equals("|Receiver")) {
                            knownStructuralMappings.add(new RBMLMapping(attrRole, receiverAttribute));
                        }
                    }
                    for (RoleOperation opRole : strRole.getOperations()){
                        if (opRole.getName().equals("|Execute()")){
                            knownStructuralMappings.add(new RBMLMapping(opRole, executeOperation));
                        }
                    }
                    break;
            }
        }
        return knownStructuralMappings;
    }
    private List<RBMLMapping> associationMap(SPS sps){
        List<RBMLMapping> knownRelationshipMappings = new ArrayList<>();
        for (RelationshipRole relationshipRole : sps.getAssociationRoles()){
            switch(relationshipRole.getName()){
                case "|Stores":
                    if (umlClassDiagram.getClassDiagram().hasEdgeConnecting(receiverClassifier, concreteCommand)){
                        //map exists here.


                    }
                    break;

            }
        }
        return knownRelationshipMappings;
    }

    protected void printSummary(){
        System.out.println("Reciever role: " + receiverClassifier.getName());
        System.out.println("Concrete Command role: " + concreteCommand.getName());
        System.out.println("receiver role (attribute): " + receiverAttribute.getName());
        System.out.println("execute() role (operation): " + executeOperation.getName());
    }




}
