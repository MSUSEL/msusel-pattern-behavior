package com.derek.uml;

import javafx.util.Pair;

import java.util.List;

public class UMLOperation {

    private String name;
    //list of pairs, where each pair corresponds to a datatype and a name.
    private List<Pair<String,String>> parameters;

    //datatype of return value, empty string if void.
    private String returnDataType;
    private Visibility visibility;
    private boolean isStatic;

    public UMLOperation(String name, List<Pair<String, String>> parameters, String returnDataType, Visibility visibility) {
        this.name = name;
        this.parameters = parameters;
        this.returnDataType = returnDataType;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pair<String, String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Pair<String, String>> parameters) {
        this.parameters = parameters;
    }

    public String getReturnDataType() {
        return returnDataType;
    }

    public void setReturnDataType(String returnDataType) {
        this.returnDataType = returnDataType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
