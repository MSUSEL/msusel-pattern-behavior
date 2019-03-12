package com.derek.grime;

import com.derek.rbml.RBMLMapping;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassGrimeMeasurements {

    private double TCC;
    private double RCI;
    private List<UMLOperation> externalMethods;
    private List<UMLOperation> internalMethods;
    private List<UMLOperation> directAccess;
    private List<UMLOperation> indirectAccess;
    private UMLClassifier umlClassifier;
    private List<RBMLMapping> rbmlStructuralMappings;


    public ClassGrimeMeasurements(UMLClassifier umlClassifier, List<RBMLMapping> rbmlStructuralMappings){
        this.umlClassifier = umlClassifier;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
    }


    private void calculateTCC(){
        int numMethodsInClass = umlClassifier.getOperationsIncludingConstructorsIfExists().size();
        //NP(C) from cohesion and reuse in OO systems paper
        double npc = numMethodsInClass*(numMethodsInClass-1)/2;

        //ND(C) from cohesion and reuse in OO systems paper
        int ndc = 0;

        for (UMLAttribute att : umlClassifier.getAttributes()){
            int usages = 0;
            for (UMLOperation op : umlClassifier.getOperationsIncludingConstructorsIfExists()){
                if (op.getVariableTable().get(att) != null){
                    //we use this variable, which is declared at the class level and used by a method.
                    usages++;
                }
            }
            if (usages >= 2){
                ndc++;
            }
        }

        //TCC(C) from cohesion and reuse in OO systems paper
        TCC = ndc/npc;
    }

    private void calculateRCI(){
        List<Pair<UMLClassifier, UMLClassifier>> ddInterations = new ArrayList();
        List<Pair<UMLClassifier, UMLClassifier>> maxDmInteractions = new ArrayList<>();


        if (umlClassifier.getIdentifier().equals("class")) {
            for (UMLAttribute att : umlClassifier.getAttributes()){
                if (att.getVisibility() == Visibility.PUBLIC){
                    //dd interactions only act on public vars - and the correct phrasing is: ' att.getType dd interacts with this (umlClassifier)
                    ddInterations.add(new ImmutablePair<>(att.getType(), umlClassifier));
                }
            }
            //once dd interactions are found, I can find dm interactions
            for (UMLOperation op : umlClassifier.getOperationsIncludingConstructorsIfExists()){
                //need to do constructors too, I think... though its not specified in the definition.
                for (UMLAttribute param : op.getParameters()){
                    //params are dd interactions, but specifically dm Interactions (dm contained in set dd)
                    ddInterations.add(new ImmutablePair<>(param.getType(), umlClassifier));
                }
                if (op.getType() != null){
                    //has return type.
                    ddInterations.add(new ImmutablePair<>(op.getType(), umlClassifier));
                }
            }

            maxDmInteractions.addAll(ddInterations);
            //now I need to get max dd interactions (all def uses within a method.)
            for (UMLOperation op : umlClassifier.getOperationsIncludingConstructorsIfExists()){
                for (UMLAttribute umlAttribute : op.getLocalVariableDecls()){
                    //get the call tree nodes from only local variable declarations.
                    for (CallTreeNode callTreeNode : op.getVariableUsages(umlAttribute)){
                        maxDmInteractions.add(new ImmutablePair<>(umlAttribute.getType(), umlClassifier));
                    }
                }
            }
        }
        RCI = ((double) ddInterations.size())/(double)maxDmInteractions.size();
    }

    private void findScope(){
        internalMethods = new ArrayList<>();
        externalMethods = new ArrayList<>();
        for (UMLOperation op : umlClassifier.getOperationsIncludingConstructorsIfExists()){
            boolean potentiallyInternal = false;
            boolean potentiallyExternal = false;
            for (RBMLMapping structureMapping : rbmlStructuralMappings){
                if (structureMapping.getUMLOperationArtifact() instanceof UMLOperation){
                    if (umlClassifier.getOperationsIncludingConstructorsIfExists().contains(structureMapping.getUMLOperationArtifact())){
                        potentiallyInternal = true;
                    }else{
                        potentiallyExternal = true;
                    }
                }
            }
            boolean operationUsesClassAttribute = operationUsesClassAttributeOnCall(op);
            if (potentiallyInternal || potentiallyExternal){
                if (operationUsesClassAttribute) {
                    internalMethods.add(op);
                }
            }
            if (potentiallyExternal && !potentiallyInternal){
                if (operationUsesClassAttribute) {
                    externalMethods.add(op);
                }
            }
        }
    }

    /***
     * utility method to return a boolean flag if a method input as parameter uses a class variable.
     * @param op
     * @return
     */
    private boolean operationUsesClassAttributeOnCall(UMLOperation op){
        for (UMLAttribute classAttribute : umlClassifier.getAttributes()){
            if (!op.getVariableTable().get(classAttribute).isEmpty()){
                return true;
            }
        }
        return false;
    }

    private boolean operationUsesClassAttributeOnOperator(UMLOperation op){
        //TODO  - I need to re-write this code (as it is the same as the operationUsesClassAttributeOnCall
        //I need to look at how CallTreeNodes store operators vs. storing calls based on object references (keyword '.')
        for (UMLAttribute classAttribute : umlClassifier.getAttributes()){
            if (!op.getVariableTable().get(classAttribute).isEmpty()){
                return true;
            }
        }
        return false;
    }

    private void findStrength(){
        directAccess = new ArrayList<>();
        indirectAccess = new ArrayList<>();
        for (UMLOperation op : umlClassifier.getOperations()){
            if (operationUsesClassAttributeOnCall(op)){
                //because of how this method finds attributes (based on callTree.isCall()), this operation is sufficient for accessor (indirect) method sets..
                //while its probably amazingly difficult to get EVERY accessor method access, (things like foo.bar.foo.set(x)), it will cover the most popular
                //accessor uses.
                indirectAccess.add(op);
            }
            if(operationUsesClassAttributeOnOperator(op)){
                //need to find when non-accessor uses are used.. things like int x = y, where x is a class attribute..
                //this might require refactoring of the srml expression nodes.... again..
                directAccess.add(op);
            }

        }

    }

    public void calculateClassGrimeMeasurements(){
        calculateTCC();
        calculateRCI();
        findScope();
        findStrength();
    }

}
