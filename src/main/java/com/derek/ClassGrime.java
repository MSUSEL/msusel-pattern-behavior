package com.derek;

import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassGrime {

    private double TCC;
    private double RCI;
    private UMLClassifier umlClassifier;


    public ClassGrime(UMLClassifier umlClassifier){
        this.umlClassifier = umlClassifier;
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
        List<Pair<UMLAttribute, UMLClassifier>> ddInterations = new ArrayList();
        List<Pair<UMLAttribute, UMLOperation>> dmInteractions = new ArrayList<>();
        List<Pair<UMLAttribute, UMLOperation>> maxDmInteractions = new ArrayList<>();
        if (umlClassifier.getIdentifier().equals("class")) {
            for (UMLAttribute att : umlClassifier.getAttributes()){
                if (att.getVisibility() == Visibility.PUBLIC){
                    //dd interactions only act on public vars
                    ddInterations.add(new ImmutablePair<>(att, att.getType()));
                }
            }
            //once dd interactions are found, I can find dm interactions
            for (UMLOperation op : umlClassifier.getOperations()){
                //need to do constructors too, I think... though its not specified in the definition.
                for (UMLClassifier param : op.getParameters()){
                    //params are dm Interactions

                }

            }

        }
    }

    public void findClassGrime(){
        calculateTCC();
        calculateRCI();

    }
}
