package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;

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

    protected void printSummary(){
        System.out.println("Reciever role: " + receiverClassifier.getName());
        System.out.println("Concrete Command role: " + concreteCommand.getName());
        System.out.println("receiver role (attribute): " + receiverAttribute.getName());
        System.out.println("execute() role (operation): " + executeOperation.getName());
    }




}
