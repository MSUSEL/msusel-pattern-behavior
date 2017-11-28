package com.derek.uml;

public class UMLAttribute {

    private String name;

    //keeping as a String and not an enum because the datatype this takes on could be a class, and I want to
    //dynamically build them and not have static via an enum.
    private String dataType;
    private Visibility visibility;
    private boolean isStatic;
    private boolean isFinal;


    public UMLAttribute(String name, String dataType, Visibility visibility) {
        this.name = name;
        this.dataType = dataType;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
}
