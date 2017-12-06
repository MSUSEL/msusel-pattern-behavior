package com.derek.uml;

//base class that represents a uml classifier.
public abstract class UMLClassifier {
    protected String name;

    public UMLClassifier(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String plantUMLTransform();
}
