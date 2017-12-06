package com.derek.uml.plantUml;

import com.derek.uml.*;
import javafx.util.Pair;

import java.util.List;

public class PlantUMLTransformer {

    private UMLClassDiagram umlClassDiagram;

    //will add sequence diagram to constructor params in near future
    public PlantUMLTransformer(UMLClassDiagram umlClassDiagram){
        this.umlClassDiagram = umlClassDiagram;
    }

    public void generateClassDiagram(){
        StringBuilder output = new StringBuilder();
        output.append("@startuml\n");
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()) {
            output.append(umlClassifier.plantUMLTransform());
        }

        output.append("@enduml\n");

        System.out.println(output.toString());
    }

}
