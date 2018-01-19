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

        //below is for printing to console
        //System.out.println(output.toString());
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
