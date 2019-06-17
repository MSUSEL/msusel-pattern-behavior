package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class FactoryMethodPattern extends PatternMapper {

    private Pair<String, UMLClassifier> creatorClassifier;
    private List<Pair<String, UMLOperation>> factoryMethodOperations;

    //dont' konw if I need to do productfamily, because it sin't being detected anyway. Coalescing it might prove difficult.
    private List<Pair<String, UMLClassifier>> creatorFamily;
    private List<Pair<String, UMLClassifier>> productFamily;
    private List<Pair<String, UMLOperation>> factoryMethodOperationFamily;
    private List<Pair<String, UMLOperation>> productMethodOperationFamily;

    public FactoryMethodPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        creatorClassifier = new ImmutablePair<>("Creator", getOneMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> factoryMethodString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles) {
            if (p.getKey().equals("FactoryMethod()")) {
                factoryMethodString.add(new ImmutablePair<>("FactoryMethod", p.getValue()));
            }
        }
        factoryMethodOperations = new ArrayList<>();
        for (Pair<String, String> s : factoryMethodString){
            factoryMethodOperations.add(new ImmutablePair<>(s.getKey(), getOperationFromString(creatorClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    protected void coalescePattern() {
        coalesceCreators();
        coalesceProducts();

        factoryMethodOperationFamily = coalesceOperations(this.getAllCreators(), "FactoryMethod");
        productMethodOperationFamily = coalesceOperations(this.getAllProducts(), "ConcreteProduct");
    }

    private void coalesceCreators(){
        List<UMLClassifier> creatorFamilyClassifiers = umlClassDiagram.getAllGeneralizationHierarchyChildren(creatorClassifier.getRight());
        creatorFamily = new ArrayList<>();
        for (UMLClassifier creatorFamilyClassifier : creatorFamilyClassifiers){
            if (creatorFamilyClassifier.getIdentifier().equals("class")){
                //concrete observer
                creatorFamily.add(new ImmutablePair<>("ConcreteCreator", creatorFamilyClassifier));
            }else {
                creatorFamily.add(new ImmutablePair<>("Creator", creatorFamilyClassifier));
            }
        }
    }

    private void coalesceProducts(){
        productFamily = new ArrayList<>();
        //pull out string ids.
        List<UMLClassifier> creators = new ArrayList<>();
        for (Pair<String, UMLClassifier> fullCreator : getAllCreators()){
            creators.add(fullCreator.getRight());
        }
        List<UMLClassifier> neighbors = umlClassDiagram.getNeighbors(creators);
        for (UMLClassifier neighbor : neighbors){
            for (String potentialName : patternCommonNames.get("Product")){
                if (neighbor.getName().equals(potentialName)){
                    //match found, this is a product.
                    if (neighbor.getIdentifier().equals("class")) {
                        productFamily.add(new ImmutablePair<>("ConcreteProduct", neighbor));
                    }else{
                        productFamily.add(new ImmutablePair<>("Product", neighbor));
                    }
                }
            }
        }
    }

    private List<Pair<String, UMLClassifier>> getAllCreators(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.add(creatorClassifier);
        toRet.addAll(creatorFamily);
        return toRet;
    }

    private List<Pair<String, UMLClassifier>> getAllProducts(){
        List<Pair<String, UMLClassifier>> toRet = new ArrayList<>();
        toRet.addAll(productFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        //class
        for (Pair<String, UMLClassifier> creatorPair : creatorFamily){
            if (creatorPair.getLeft().equals("ConcreteCreator")){
                modelBlocks.add(creatorPair);
            }
        }
        for (Pair<String, UMLClassifier> productPair : productFamily){
            if (productPair.getLeft().equals("ConcreteProduct")){
                modelBlocks.add(productPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> modelBlocks = new ArrayList<>();
        //class
        modelBlocks.add(creatorClassifier);
        for (Pair<String, UMLClassifier> creatorPair : creatorFamily){
            if (creatorPair.getLeft().equals("Creator")){
                modelBlocks.add(creatorPair);
            }
        }
        for (Pair<String, UMLClassifier> productPair : productFamily){
            if (productPair.getLeft().equals("Product")){
                modelBlocks.add(productPair);
            }
        }
        return modelBlocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        List<Pair<String, UMLOperation>> toRet = new ArrayList<>();
        toRet.addAll(factoryMethodOperations);
        toRet.addAll(factoryMethodOperationFamily);
        toRet.addAll(productMethodOperationFamily);
        return toRet;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        //no attributes.
        return new ArrayList<>();
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses() {
        List<Pair<String, UMLClassifier>> classModelBlocks = new ArrayList<>();
        classModelBlocks.addAll(getAllCreators());
        classModelBlocks.addAll(getAllProducts());
        return classModelBlocks;
    }

    @Override
    protected String getPatternCommonNamesFileName() {
        return "factoryMethod.txt";
    }

    @Override
    public void printSummary() {

    }

}
