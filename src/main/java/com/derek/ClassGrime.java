package com.derek;

import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import lombok.Getter;

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
                if (false){//if(op.getVarUsages().contains(att)){
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

    }

    public void findClassGrime(){
        calculateTCC();
        calculateRCI();

    }
}
