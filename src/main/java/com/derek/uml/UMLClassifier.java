package com.derek.uml;

import lombok.Getter;

import java.util.List;

//base class that represents a uml classifier.
@Getter
public abstract class UMLClassifier {
    protected String name;
    protected List<String> residingPackage;
    protected List<List<String>> imports;

    public UMLClassifier(String name, List<String> residingPackage, List<List<String>> imports){
        this.name = name;
        this.residingPackage = residingPackage;
        this.imports = imports;
    }


    public abstract String plantUMLTransform();
    //returns operations
    public abstract List<UMLOperation> getOperations();
    //returns attributes or null if the holding class does not have any attributes
    public abstract List<UMLAttribute> getAttributes();

    //used for uml class diagram generation
    public abstract List<String> getExtendsParents();
    public abstract List<String> getImplementsParents();
}
