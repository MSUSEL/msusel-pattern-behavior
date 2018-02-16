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

import com.derek.model.ExternalPartyDataTypeSignature;
import com.derek.uml.plantUml.PlantUMLTransformer;
import com.derek.uml.srcML.SrcMLBlock;
import com.derek.uml.srcML.SrcMLClass;
import com.derek.uml.srcML.SrcMLEnum;
import com.derek.uml.srcML.SrcMLInterface;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class UMLGenerator {
    private List<SrcMLBlock> rootBlocks;
    private UMLClassDiagram umlClassDiagram;
    private ExternalPartyDataTypeSignature languageTypes;
    private List<ExternalPartyDataTypeSignature> thirdPartyTypes;

    public UMLGenerator(List<SrcMLBlock> rootBlocks){

        this.rootBlocks = rootBlocks;
        umlClassDiagram = new UMLClassDiagram();

        //pass 1 is finding all classes/ops/attributes
        pass1();

        pass2();
        //pass 2 is connecting all classes/ops/attributes with inter-project types (building package tree)
        //pass2();
        //apss 3 is building relationship types.
        //pass3();

        PlantUMLTransformer pltTransformer = new PlantUMLTransformer(umlClassDiagram);
        //used to print plantuml
        pltTransformer.generateClassDiagram();
    }

    private void buildStructuralUML(){
        buildRelationships();
    }

    private void pass1(){
        buildClasses();
    }
    private void pass2(){
        connectPackageStructure();
        //if I wanted to connect 3rd party library signatures, I would add a method here that does that.
    }

    private void connectPackageStructure(){
        PackageTree tree = new PackageTree();
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            tree.addEntirePackage(umlClassifier);
        }
        umlClassDiagram.setPackageTree(tree);

        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                if (interProjectDataType(umlAttribute.getStringDataType())){
                    System.out.println(umlAttribute.getStringDataType() + " is a project type");
                }else if (languageProjectDataType(umlAttribute.getStringDataType())){
                    System.out.println(umlAttribute.getStringDataType() + " is a language type");

                }else if (thirdPartyDataType(umlAttribute.getStringDataType())){

                }
            }
        }
    }

    private boolean interProjectDataType(String s){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            if (s.equals(umlClassifier.getName())){
                return true;
            }
        }
        return false;
    }

    private boolean languageProjectDataType(String s){
        //language types are interesting because these are any types that can be used WITHOUT importing them.
        //as an example, String is a type in this category. Any java.lang type is in here.
        //things like Scanner or List are NOT included here.
        try{
            if (languageTypes == null) {
                //if this is the first time parsing langaugeTypes, load the file and parse it
                languageTypes = new ExternalPartyDataTypeSignature("resources/javaTypes.txt");
            }
            for (String languageType : languageTypes.getStringDataTypes()){
                if (languageType.equals(s)){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }




    private void pass3(){
        buildRelationships();
    }

    private void buildClasses(){
        for (SrcMLBlock rootBlock :  rootBlocks) {
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

    private void buildRelationships(){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                //standard attribute relationships
                String type = umlAttribute.getStringDataType();
                placeAssociation(umlClassifier, type);
            }
            for (UMLOperation operation : umlClassifier.getOperations()){
                //standard operation relationships
                String returnType = operation.getStringReturnDataType();
                placeAssociation(umlClassifier, returnType);
                for (Pair<String, String> param : operation.getParameters()){
                    //params in operation
                    String paramType = param.getKey();
                    placeAssociation(umlClassifier, paramType);
                }
            }
            for (UMLClassifier parent : findExtendsParentsObjs(umlClassifier)){
                umlClassDiagram.addRelationshipToDiagram(umlClassifier, parent, Relationship.GENERALIZATION);
            }
            for (UMLClassifier parent : findImplementsParentsObjs(umlClassifier)){
                umlClassDiagram.addRelationshipToDiagram(umlClassifier, parent, Relationship.REALIZATION);
            }
        }
    }
    private List<UMLClassifier> findExtendsParentsObjs(UMLClassifier umlClassifier){
        List<UMLClassifier> parents = new ArrayList<>();
        for (String parent : umlClassifier.getExtendsParentsString()){
            for (UMLClassifier potentialParent : umlClassDiagram.getClassDiagram().nodes()){
                if (parent.equals(potentialParent.getName())){
                    //found it!
                    parents.add(potentialParent);
                    umlClassifier.addExtendsParents(potentialParent);
                }
            }
        }
        return parents;
    }
    private List<UMLClassifier> findImplementsParentsObjs(UMLClassifier umlClassifier){
        List<UMLClassifier> parents = new ArrayList<>();
        for (String parent : umlClassifier.getImplementsParentsString()) {
            for (UMLClassifier potentialParent : umlClassDiagram.getClassDiagram().nodes()) {
                if (parent.equals(potentialParent.getName())) {
                    //found it!
                    parents.add(potentialParent);
                    umlClassifier.addImplementsParents(potentialParent);
                }
            }
        }
        return parents;
    }

    /**
     * utility method to find and assign types. Because this logic is identical for attributes and operations, I moved it here.
     * @param umlClassifier
     * @param type
     */
    private void placeAssociation(UMLClassifier umlClassifier, String type){
        //this logic is wrong.. or at least not entirely correct.
        //I need to do multiple ordered checks to make sure the correct type is found...
        //start with files at this package, then move up, etc.
        //once type is not found in entirety of project files, check libs.

        if (isPrimitiveType(type)){
            //dont' care if this is the case.
        } else {
            List<UMLClassifier> relationships = isInterClassType(type);
            for (UMLClassifier relationship : relationships){
                //class is declared in this project.
                umlClassDiagram.addRelationshipToDiagram(umlClassifier, relationship, Relationship.ASSOCIATION);
            }
            List<UMLClassifier> nonProjectRelationships = isIntraClassType(type);
            for (UMLClassifier nonProjectRelationship : nonProjectRelationships){
                //in the future if I want to implement associations to non-project types, use the code below to find non-project types
                //and then incrementally implmeent the missing ones.
                //I don't think I care about this though...
                //System.out.println("non project type: " + type);
            }
        }
    }

    private List<UMLClassifier> isInterClassType(String type){
        List<UMLClassifier> classifiers = new ArrayList<>();
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (String s : typeSplitter(type)) {
                if (s.equals(umlClassifier.getName())) {
                    //match!
                    classifiers.add(umlClassifier);
                }
            }
        }
        return classifiers;
    }

    private List<UMLClassifier> isIntraClassType(String type){
        List<UMLClassifier> classifiers = new ArrayList<>();
        for (String s : typeSplitter(type)) {
            switch(s){
                //something like this, but make these classes be unique objects.
                case "Object":
                case "Integer":
                    classifiers.add(null);
            }
        }
        return classifiers;
    }

    private List<String> typeSplitter(String type){
        //because I have generics (foo<bar>), combo generics (foo<bar1,bar2>), and ownership (foo.bar)
        List<String> types = new ArrayList<>();

        //initial case
        String[] splitter = type.split("<");
        types.add(splitter[0]);
        for (int i = 1; i < splitter.length; i++){
            String segment = splitter[i].replaceAll(">", "");
            String[] segmentSplitter = segment.split(",");
            types.add(segmentSplitter[0]);
            for (int j = 1; j < segmentSplitter.length; j++){
                types.add(segmentSplitter[j]);
            }
        }
        return types;
    }


    //returns true if the type is a primitive type (including Strring)
    //flase otherwise
    private boolean isPrimitiveType(String type){
        switch(type){
            case "boolean":
            case "byte":
            case "char":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "String":
                //while String technically isn't a primitive, I think its worth including because its so common and
                //I don't want to pull from the String java class.
                return true;
        }
        return false;
    }

}
