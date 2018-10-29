package com.derek;

import com.derek.rbml.RBMLMapping;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class ClassGrime {

    private double TCC;
    private double RCI;
    private List<UMLOperation> externalMethods;
    private List<UMLOperation> internalMethods;
    private UMLClassifier umlClassifier;
    private List<RBMLMapping> rbmlStructuralMappings;


    public ClassGrime(UMLClassifier umlClassifier, List<RBMLMapping> rbmlStructuralMappings){
        this.umlClassifier = umlClassifier;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
    }


    private void calculateTCC(){
        int numMethodsInClass = umlClassifier.getOperations().size();
        //NP(C) from cohesion and reuse in OO systems paper
        double npc = numMethodsInClass*(numMethodsInClass-1)/2;

        //ND(C) from cohesion and reuse in OO systems paper
        int ndc = 0;

        for (UMLAttribute att : umlClassifier.getAttributes()){
            int usages = 0;
            for (UMLOperation op : umlClassifier.getOperations()){
                if (op.getVariableUsages().contains(att.getName())){
                    //we use this varaible.
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
            for (UMLOperation op : umlClassifier.getOperations()){
                //need to do constructors too, I think... though its not specified in the definition.
                for (UMLClassifier param : op.getParameters()){
                    //params are dd interactions, but specifically dm Interactions (dm contained in set dd)
                    ddInterations.add(new ImmutablePair<>(param, umlClassifier));
                }
                if (op.getType() != null){
                    //has return type.
                    ddInterations.add(new ImmutablePair<>(op.getType(), umlClassifier));
                }
            }
            if (umlClassifier instanceof UMLClass){
                //definitely a class - now check constructors.
                UMLClass asClass = (UMLClass) umlClassifier;
                for (UMLOperation constructor : asClass.getConstructors()){
                    for (UMLClassifier param : constructor.getParameters()){
                        ddInterations.add(new ImmutablePair<>(param, umlClassifier));
                    }
                }
            }

            maxDmInteractions.addAll(ddInterations);
            //now I need to get max dd interactions (all def uses within a method.)
            for (UMLOperation op : umlClassifier.getOperations()){
                for (UMLAttribute umlAttribute : op.getLocalAttributeDecls()){
                    maxDmInteractions.add(new ImmutablePair<>(umlAttribute.getType(), umlClassifier));
                }
                for (UMLClassifier localUsage : op.getLocalVariableTypeUsages()){
                    maxDmInteractions.add(new ImmutablePair<>(localUsage, umlClassifier));
                }
            }
            if (umlClassifier instanceof UMLClass){
                //definitely a class - now check constructors.
                UMLClass asClass = (UMLClass) umlClassifier;
                for (UMLOperation constructor : asClass.getConstructors()){
                    for (UMLClassifier localUsage : constructor.getLocalVariableTypeUsages()){
                        maxDmInteractions.add(new ImmutablePair<>(localUsage, umlClassifier));
                    }
                }
            }
        }
//        System.out.println("Class: " + umlClassifier.getName());
//        for (Pair<UMLClassifier, UMLClassifier> ddInter : ddInterations){
//            System.out.println(ddInter.getLeft().getName() + " dd interacts with: " + ddInter.getRight().getName());
//        }
//        for (Pair<UMLClassifier, UMLClassifier> dmMax : maxDmInteractions){
//            System.out.println(dmMax.getLeft().getName() + " dm interacts with: " + dmMax.getRight().getName());
//        }
        //System.out.println("Finished calculating RCI... ddInteractions: " + ddInterations.size() + "     dmInteractions: " + maxDmInteractions.size());
        RCI = ((double) ddInterations.size())/(double)maxDmInteractions.size();
    }

    private void findScope(){
        internalMethods = new ArrayList<>();
        externalMethods = new ArrayList<>();
        for (UMLOperation op : umlClassifier.getOperations()){
            boolean potentiallyInternal = false;
            boolean potentiallyExternal = false;
            for (RBMLMapping structureMapping : rbmlStructuralMappings){
                if (structureMapping.getUMLOperationArtifact() instanceof UMLOperation){
                    if (umlClassifier.getOperations().contains(structureMapping.getUMLOperationArtifact())){
                        potentiallyInternal = true;
                    }else{
                        potentiallyExternal = true;
                    }
                }
            }
            if (potentiallyInternal || potentiallyExternal){
                utilityAddScopeBasedOnUsage(op, internalMethods);
            }
            if (potentiallyExternal && !potentiallyInternal){
                utilityAddScopeBasedOnUsage(op, externalMethods);
            }
        }
    }

    private void utilityAddScopeBasedOnUsage(UMLOperation op, List<UMLOperation> scope){
        for (String s : op.getVariableUsages()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                if (s.equals(umlAttribute.getName())){
                    //same name, likely the same attribute. Though if a variables of the same name in methods and classes might conflict here.
                    scope.add(op);
                    return;
                }
            }
        }

    }

    public void findClassGrime(){
        calculateTCC();
        calculateRCI();
        findScope();
        System.out.println("Class: " + umlClassifier.getName());
        for (UMLOperation op : internalMethods){
            System.out.println(op.getName() + " is internal.");
        }
        for (UMLOperation op : externalMethods){
            System.out.println(op.getName() + " is external.");
        }
    }
}
