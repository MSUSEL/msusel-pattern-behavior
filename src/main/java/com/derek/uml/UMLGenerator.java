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
package com.derek.uml;

import com.derek.uml.plantUml.PlantUMLTransformer;
import com.derek.uml.srcML.SrcMLBlock;
import com.derek.uml.srcML.SrcMLClass;
import com.derek.uml.srcML.SrcMLEnum;
import com.derek.uml.srcML.SrcMLInterface;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLGenerator {
    private List<SrcMLBlock> rootBlocks;
    private UMLClassDiagram umlClassDiagram;
    private UMLSequenceDiagram umlSequenceDiagram;
    @Getter
    protected static ExternalPartyDataTypeSignature languageTypes;
    private List<ExternalPartyDataTypeSignature> thirdPartyTypes;

    public UMLGenerator(List<SrcMLBlock> rootBlocks){

        this.rootBlocks = rootBlocks;
        umlClassDiagram = new UMLClassDiagram();
        umlSequenceDiagram = new UMLSequenceDiagram();

        //pass 1 is finding all classes/ops/attributes
        pass1();

        pass2();
        //pass 2 is connecting all classes/ops/attributes with inter-project types (building package tree)
        //pass2();
        //apss 3 is building relationship types.
        pass3();

        //pass 4 is building sequence diagram stuff
        pass4();

        PlantUMLTransformer pltTransformer = new PlantUMLTransformer(umlClassDiagram, umlSequenceDiagram);
        //used to print plantuml
        pltTransformer.generateClassDiagram();
        pltTransformer.generateSequenceDiagram();
    }

    private void pass1(){
        buildClasses();
        buildLanguageTypes();
        buildLanguageClasses();
    }

    private void pass2(){
        connectPackageStructure();
        //if I wanted to connect 3rd party library signatures, I would add a method here that does that.
        //I have done this now. it is in the connectPackageStructure() and then inter calls. See the ExternalPArtyDataTypeSignature class.
    }

    private void pass3(){
        placeRelationships();
    }

    private void pass4(){
        //
        buildSequenceDiagram();

    }

    private void buildSequenceDiagram(){
        //TODO - once I flesh out behavior I will have to uncomment this and do some debugging.
        //System.out.println("Generating sequence diagram");
        //UMLBehaviorGenerator behaviorGenerator = new UMLBehaviorGenerator(umlClassDiagram);
    }

    private void connectPackageStructure(){
        umlClassDiagram.buildPackageTree();
        //initial pass building the inheritance hierarchy.
        buildInheritanceHierarchy();

        //at this point we have knowledge of all built-in classes, language types, and 3rd party classes if applicable (at time of this no 3rd parties are included)
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                //generics are not being handled - though I don't knwo if they need to be.
                //right nwo I am doing nothing for many of these.
                UMLClassifier potentialMatch = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, umlClassifier, umlAttribute.getStringDataType());
                    //type within the project

                umlAttribute.setType(potentialMatch);
            }
            //the issue is that language types (Object, etc.,) do not have their operations imported. so it looks like they are null.
            for (UMLOperation umlOperation : umlClassifier.getOperations()){
                if (!umlOperation.getStringReturnDataType().equals("void")){
                    UMLClassifier potentialMatch = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, umlClassifier, umlOperation.getStringReturnDataType());
                    //type within the project
                    umlOperation.setType(potentialMatch);
                    umlOperation.setParameters(getParamsFromString(umlClassifier, umlOperation));
                }
            }
        }
    }

    private void buildInheritanceHierarchy(){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            List<UMLClassifier> extendsParents = new ArrayList<>();
            for (String extendsString : umlClassifier.getExtendsParentsString()){
                UMLClassifier match = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, umlClassifier, extendsString);
                if (match != null){
                    //might  be null, especially if its a 3rd party lib.
                    extendsParents.add(match);
                }
            }
            umlClassifier.setExtendsParents(extendsParents);
            List<UMLClassifier> implementsParents = new ArrayList<>();
            for (String implementsString : umlClassifier.getImplementsParentsString()){
                UMLClassifier match = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, umlClassifier, implementsString);
                if (match != null) {
                    implementsParents.add(match);
                }
            }
            umlClassifier.setImplementsParents(implementsParents);
        }
    }

    private List<UMLClassifier> getParamsFromString(UMLClassifier owningClassifier, UMLOperation umlOperation){
        List<UMLClassifier> params = new ArrayList<>();
        for (Pair<String, String> stringParam : umlOperation.getStringParameters()){

            UMLClassifier potentialMatch = UMLMessageGenerationUtils.getUMLClassifierFromStringType(umlClassDiagram, owningClassifier, stringParam.getKey());
            params.add(potentialMatch);
        }
        return params;
    }

    private void buildLanguageTypes(){
        //if this is the first time parsing langaugeTypes, load the file and parse it
        languageTypes = new ExternalPartyDataTypeSignature("resources/javaTypes.txt");
    }

    private void buildClasses(){
        for (SrcMLBlock rootBlock : rootBlocks) {
            List<SrcMLClass> srcMLClasses = rootBlock.getClasses();
            List<String> residingPackage = UMLGenerationUtils.getResidingPackage(rootBlock);
            List<List<String>> imports = UMLGenerationUtils.getImports(rootBlock);
            for (SrcMLClass srcMLClass : srcMLClasses) {
                umlClassDiagram.addClassToDiagram(UMLGenerationUtils.getUMLClass(srcMLClass, residingPackage, imports));
            }
            List<SrcMLInterface> srcMLInterfaces = rootBlock.getInterfaces();
            for (SrcMLInterface srcMLInterface : srcMLInterfaces) {
                umlClassDiagram.addClassToDiagram(UMLGenerationUtils.getUMLInterface(srcMLInterface, residingPackage, imports));
            }
            List<SrcMLEnum> srcMLEnums = rootBlock.getEnums();
            for (SrcMLEnum srcMLEnum : srcMLEnums) {
                umlClassDiagram.addClassToDiagram(UMLGenerationUtils.getUMLEnum(srcMLEnum, residingPackage, imports));
            }
        }
    }

    private void buildLanguageClasses(){
        for (UMLClassifier languageType : languageTypes.getDataTypes()){
            umlClassDiagram.addClassToDiagram(languageType);
        }
    }

    private void placeRelationships(){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                //standard attribute relationships

                if (umlAttribute.getType() != null){
                    if (umlClassDiagram.getClassDiagram().nodes().contains(umlAttribute.getType())) {
                        umlClassDiagram.addRelationshipToDiagram(umlClassifier, umlAttribute.getType(), Relationship.ASSOCIATION);
                    }
                }
            }
            for (UMLOperation operation : umlClassifier.getOperations()){

                //standard operation relationships
                if (operation.getType() != null) {
                    if (umlClassDiagram.getClassDiagram().nodes().contains(operation.getType())){
                        umlClassDiagram.addRelationshipToDiagram(umlClassifier, operation.getType(), Relationship.ASSOCIATION);
                    }
                }
                //set params
                operation.setParameters(getParamsFromString(umlClassifier, operation));
                for (UMLClassifier param : operation.getParameters()){
                    if (param != null) {
                        if (umlClassDiagram.getClassDiagram().nodes().contains(param)){
                            umlClassDiagram.addRelationshipToDiagram(umlClassifier, param, Relationship.ASSOCIATION);
                        }
                    }
                }
            }
            for (UMLClassifier parent : umlClassifier.getExtendsParents()){
                if (umlClassDiagram.getClassDiagram().nodes().contains(parent)){
                    umlClassDiagram.addRelationshipToDiagram(umlClassifier, parent, Relationship.GENERALIZATION);
                }
            }
            for (UMLClassifier parent : umlClassifier.getImplementsParents()){
                if (umlClassDiagram.getClassDiagram().nodes().contains(parent)){
                    umlClassDiagram.addRelationshipToDiagram(umlClassifier, parent, Relationship.REALIZATION);
                }
            }
        }
    }

}
