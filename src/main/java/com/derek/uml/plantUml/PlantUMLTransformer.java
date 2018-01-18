package com.derek.uml.plantUml;

import com.derek.uml.*;
import com.google.common.graph.EndpointPair;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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
        generateRelationships(output);

        output.append("@enduml\n");

        System.out.println(output.toString());
        printToFile(output);
    }

    public void generateRelationships(StringBuilder output){
        for (EndpointPair<UMLClassifier> endpointPair : umlClassDiagram.getClassDiagram().edges()) {
            Relationship r = umlClassDiagram.getClassDiagram().edgeValue(endpointPair.source(), endpointPair.target()).get();
            output.append(endpointPair.nodeU().getName() + " ");
            output.append(r.plantUMLTransform() + " ");
            output.append(endpointPair.nodeV().getName() + "\n");
        }
    }
    private void printToFile(StringBuilder output){
        try{
            File mainDirectory = new File("plantUmlOutput\\");
            if (!mainDirectory.exists()){
                Files.createDirectory(Paths.get("plantUmlOutput\\"));
            }
            File fout = new File("plantUmlOutput\\plantUml.puml");
            PrintWriter pw = new PrintWriter(fout);
            pw.print(output);
            pw.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
