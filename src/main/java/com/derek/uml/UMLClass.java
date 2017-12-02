package com.derek.uml;

import java.util.List;

public class UMLClass {

    private String name;
    private Visibility visibility;
    private boolean isAbstract;
    private List<UMLAttribute> attributes;
    private List<UMLOperation> operations;
    private List<Constructor> constructors;

    public UMLClass(String name, List<UMLAttribute> attributes, List<UMLOperation> operations, List<Constructor> constructors, boolean isAbstract) {
        this.name = name;
        this.attributes = attributes;
        this.operations = operations;
        this.constructors = constructors;
        this.isAbstract = isAbstract;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
