package com.derek.uml;

import lombok.Getter;

import java.util.List;

//base class that represents a uml classifier.
@Getter
public abstract class UMLClassifier {
    protected String name;

    public UMLClassifier(String name){
        this.name = name;
    }

    public abstract String plantUMLTransform();
    //returns operations
    public abstract List<UMLOperation> getOperations();
    //returns attributes or null if the holding class does not have any attributes
    public abstract List<UMLAttribute> getAttributes();
}
