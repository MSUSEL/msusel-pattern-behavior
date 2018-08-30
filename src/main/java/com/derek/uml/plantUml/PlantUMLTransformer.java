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
package com.derek.uml.plantUml;

import com.derek.Main;
import com.derek.uml.*;
import com.google.common.graph.EndpointPair;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlantUMLTransformer {

    private UMLClassDiagram umlClassDiagram;
    private UMLSequenceDiagram umlSequenceDiagram;

    //will add sequence diagram to constructor params in near future
    public PlantUMLTransformer(UMLClassDiagram umlClassDiagram, UMLSequenceDiagram umlSequenceDiagram){
        this.umlClassDiagram = umlClassDiagram;
        this.umlSequenceDiagram = umlSequenceDiagram;
    }


    public void generateSequenceDiagram(){
        StringBuilder behaviorOutput = new StringBuilder();
        behaviorOutput.append("@startuml\n");
        for (UMLLifeline umlLifeline : umlSequenceDiagram.getSequenceDiagram().nodes()){
            //TODO
            //behaviorOutput.append(umlLifeline.plantUMLTransform());
        }

        behaviorOutput.append("@enduml\n");

        //below is for printing to console
        //System.out.println(output.toString());
        printBehaviorToFile(behaviorOutput);
    }

    public void generateClassDiagram(){
        StringBuilder structuralOutput = new StringBuilder();
        structuralOutput.append("@startuml\n");
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()) {
            structuralOutput.append(umlClassifier.plantUMLTransform());
        }
        generateRelationships(structuralOutput);

        structuralOutput.append("@enduml\n");


        //below is for printing to console
        //System.out.println(output.toString());
        printStructureToFile(structuralOutput);
    }

    public void generateRelationships(StringBuilder output){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLClassifier adjacent : umlClassDiagram.getClassDiagram().adjacentNodes(umlClassifier)){
                for (Relationship r : umlClassDiagram.getClassDiagram().edgesConnecting(umlClassifier, adjacent)){
                    output.append(umlClassifier.getName() + " ");
                    output.append(r.plantUMLTransform() + " ");
                    output.append(adjacent.getName() + "\n");
                }
            }
        }
    }
    private void printStructureToFile(StringBuilder structureOutput){
        try{
            File mainDirectory = new File("plantUmlOutput\\");
            if (!mainDirectory.exists()){
                Files.createDirectory(Paths.get("plantUmlOutput\\"));
            }
            File fout = new File("plantUmlOutput\\plantUmlStructure" + Main.currentVersion + ".puml");
            PrintWriter pw = new PrintWriter(fout);

            pw.print(structureOutput);
            pw.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void printBehaviorToFile(StringBuilder behaviorOutput){
        try{
            File mainDirectory = new File("plantUmlOutput\\");
            if (!mainDirectory.exists()){
                Files.createDirectory(Paths.get("plantUmlOutput\\"));
            }
            File fout = new File("plantUmlOutput\\plantUmlBehavior" + Main.currentVersion + ".puml");
            PrintWriter pw = new PrintWriter(fout);
            pw.print(behaviorOutput);
            pw.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
